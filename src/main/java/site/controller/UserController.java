package site.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import site.config.Globals;
import site.facade.MailService;
import site.facade.ResetPasswordService;
import site.model.User;
import site.repository.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.io.IOException;

import static site.controller.ResourceAsString.resourceAsString;

@Controller
public class UserController {

	public static final String RESET_PASSWORD_JSP = "/resetPassword.jsp";
	public static final String CREATE_NEW_PASSWORD_JSP = "/createNewPassword.jsp";
	public static final String SUCCESS_SCREEN_JSP = "/successScreen.jsp";

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	protected MailService mailService;
	
	@Autowired
	private ResetPasswordService resetPassService;
	
	private static final Logger logger = LogManager.getLogger(UserController.class);

	@Value("${site.url.reset.password:https://jprime.io/createNewPassword?tokenId=}")
	private  String createNewPasswordUrl;
	
	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("user", new User());
		return "/signup.jsp";
	}

	@PostMapping("/signup")
	public String signip(@Valid final User user, BindingResult bindingResult, HttpServletRequest request) {
		if (bindingResult.hasErrors()) {
			return "/signup.jsp";
		}

		if (ObjectUtils.isEmpty(user.getPassword()) || !user.getPassword().equals(user.getCpassword())) {
			bindingResult.rejectValue("cpassword", "notmatch.password", "Passwords dont match!");

			return "/signup.jsp";
		}

		User existingUser = userRepository.findUserByEmail(user.getEmail());
		if (existingUser != null) {
			bindingResult.rejectValue("email", "email.exists", "This email already exists, please use forgot password");

			return "/signup.jsp";
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));

		this.getUserRepository().save(user);

		try {
			String mailContent = buildWelcomeMailContent(user, "/welcomingMail.html");
			String mailTitle = "Welcome to JPrime!";
			mailService.sendEmail(user.getEmail(), mailTitle, mailContent);
		} catch (MessagingException | IOException e) {
			logger.error("Error while sending Welcoming Mail to  " + user, e);
		}

		request.getSession().setAttribute("user", user);
		return "redirect:/home";
	}

	@GetMapping("/login")
	public String login(Model model) {
		return "/login.jsp";
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		request.getSession().removeAttribute("user");
		return "redirect:/";
	}
	
	@GetMapping("/resetPassword")
	public String forgottenPass(@ModelAttribute("sent_to_email") String email, HttpServletRequest request) {
		
		return RESET_PASSWORD_JSP;
	}
	
	
	@PostMapping("/resetPassword")
	public String resetPassword(@RequestParam String email, Model model, final RedirectAttributes redirectAttrs) {
		EmailValidator mailValidator = new EmailValidator();
		if(!mailValidator.isValid(email, null)){
			model.addAttribute("error_msg", "Please, enter a valid mail address");
			return RESET_PASSWORD_JSP;
		}
		
		User user = getUserRepository().findUserByEmail(email);
		if(user != null) {
			String tokenId = resetPassService.createNewToken(user);

			try {
				String mailContent = buildResetMailContent(user, tokenId, "/resetPasswordMail.html");
				String mailTitle = "Reset your JPrime password";
				mailService.sendEmail(email, mailTitle, mailContent);
			} catch (MessagingException | IOException e) {
			    logger.error("Error while sending ResetPassword Mail to  " + user, e);
			}
		}
		redirectAttrs.addFlashAttribute("sent_to_email", email);
		return "redirect:/resetPassword";
	}
	
	@GetMapping("createNewPassword")
	public String createNewPass(@RequestParam(required = true)  String tokenId, Model model){
		
		User owner = resetPassService.checkTokenValidity(tokenId);
		if(owner == null) {
			return "redirect:/home";
		}
		
		model.addAttribute("tokenId", tokenId);
		return CREATE_NEW_PASSWORD_JSP;
	}

	@PostMapping("createNewPassword")
	public String createNewPassPost(@RequestParam(required = true) String tokenId,
			@RequestParam(required = true) String password,
			@RequestParam(required = true) String cpassword, final RedirectAttributes redirectAttrs, Model model) {

		User owner = resetPassService.checkTokenValidity(tokenId);
		if (owner == null) {
			model.addAttribute("error_msg", "Your token is invalid. Request a new one");
			model.addAttribute("tokenId", tokenId);
			return CREATE_NEW_PASSWORD_JSP;
		}
		
		if(ObjectUtils.isEmpty(password) || !password.equals(cpassword)){
			model.addAttribute("error_msg", "Passwords did not match");
			model.addAttribute("tokenId", tokenId);
			return CREATE_NEW_PASSWORD_JSP;
		}
		
		owner.setPassword(passwordEncoder.encode(password));
		getUserRepository().save(owner);
		
		resetPassService.setTokenToUsed(tokenId);
		redirectAttrs.addFlashAttribute("user", owner);
		return "redirect:/successfulPasswordChange";
	}
	
	@GetMapping("successfulPasswordChange")
	public String successfulPasswordChange(@ModelAttribute User user, Model model){
		String msg = "Successfully changed password for user: " + user.getFirstName() + " " + user.getLastName() +
				" (" + user.getEmail() + ") ";
		model.addAttribute("msg", msg);
		model.addAttribute("jprime_year", Globals.CURRENT_BRANCH.getStartDate().getYear());

		return SUCCESS_SCREEN_JSP;
	}
	
	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
    private String buildResetMailContent(User user, String tokenId, String fileName)
            throws IOException {
        String messageText = resourceAsString(fileName);
        messageText = messageText.replace("{user.firstName}", user.getFirstName());
        String url = createNewPasswordUrl+tokenId;
        messageText = messageText.replace("{url}", url);
        return messageText;
    }
    
    private String buildWelcomeMailContent(User user, String fileName)
            throws IOException {
        String messageText = resourceAsString(fileName);
        messageText = messageText.replace("{user.firstName}", user.getFirstName());
        return messageText;
    }
    
    public void setMailService(MailService mailService) {
    	this.mailService = mailService;
    }
}
