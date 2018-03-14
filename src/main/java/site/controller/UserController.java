package site.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import site.facade.MailService;
import site.facade.ResetPasswordService;
import site.model.User;
import site.repository.UserRepository;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
public class UserController {

	public static final String RESET_PASSWORD_JSP = "/resetPassword.jsp";
	public static final String CREATE_NEW_PASSWORD_JSP = "/createNewPassword.jsp";
	public static final String SUCCESS_SCREEN_JSP = "/successScreen.jsp";

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	//@Autowired - Spring cannot find the Bean and it is not used anywhere, so we do not need it
	//private AuthenticationManager authenticationManager;

	
	@Autowired
	protected MailService mailService;
	
	@Autowired
	private ResetPasswordService resetPassService;
	
	private static final Logger logger = LogManager.getLogger(UserController.class);

	@Value("${site.url.reset.password:https://jprime.io/createNewPassword?tokenId=}")
	private  String createNewPasswordUrl;
	
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signup(Model model) {
		model.addAttribute("user", new User());
		return "/signup.jsp";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signip(@Valid final User user, BindingResult bindingResult, HttpServletRequest request) {
		if (bindingResult.hasErrors()) {
			return "/signup.jsp";
		}

		if (StringUtils.isEmpty(user.getPassword()) || !user.getPassword().equals(user.getCpassword())) {
			bindingResult.rejectValue("cpassword", "notmatch.password", "Passwords dont match!");

			return "/signup.jsp";
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));

		this.getUserRepository().save(user);

		try {
			String mailContent = buildWelcomeMailContent(user, "/welcomingMail.html");
			String mailTitle = "Welcome to JPrime!";
			mailService.sendEmail(user.getEmail(), mailTitle, mailContent);
		} catch (MessagingException | IOException | URISyntaxException e) {
			logger.error("Error while sending Welcoming Mail to  " + user, e);
		}

		request.getSession().setAttribute("user", user);
		return "redirect:/home";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model) {
		return "/login.jsp";
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request) {
		request.getSession().removeAttribute("user");
		return "redirect:/";
	}
	
//	@RequestMapping(value = "/login", method = RequestMethod.POST)
//	public String login(@RequestParam(required = true) String username, @RequestParam(required = true) String password,
//			Model model, HttpServletRequest request) {
//		System.out.println("POSTTT");
//		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(username)) {
//			return "/login.jsp";
//		}
//
//		User user = this.getUserRepository().findUserByEmail(username);
//		if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
//			return "/login.jsp";
//		}
//		request.getSession().setAttribute("user", user);
//	
//		 try {
//             UsernamePasswordAuthenticationToken session = new UsernamePasswordAuthenticationToken(username, password);
//             session.setDetails(new WebAuthenticationDetails(request));
//             Authentication authentication = authenticationManager.authenticate(session);
//             SecurityContextHolder.getContext().setAuthentication(authentication);
//         } catch (AuthenticationException var8) {
//        	 return "/login.jsp";
//         }
//
//		return "redirect:/tickets";
//	}

	
	@RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
	public String forgottenPass(@ModelAttribute("sent_to_email") String email, HttpServletRequest request) {
		
		return RESET_PASSWORD_JSP;
	}
	
	
	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
	public String resetPassword(@RequestParam(value = "email") String email, Model model, final RedirectAttributes redirectAttrs) {
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
			} catch (MessagingException | IOException | URISyntaxException e) {
			    logger.error("Error while sending ResetPassword Mail to  " + user, e);
			}
		}
		redirectAttrs.addFlashAttribute("sent_to_email", email);
		return "redirect:/resetPassword";
	}
	
	@RequestMapping(value = "createNewPassword", method  = RequestMethod.GET)
	public String createNewPass(@RequestParam(value = "tokenId", required = true)  String tokenId, Model model){
		
		User owner = resetPassService.checkTokenValidity(tokenId);
		if(owner == null) {
			return "redirect:/home";
		}
		
		model.addAttribute("tokenId", tokenId);
		return CREATE_NEW_PASSWORD_JSP;
	}

	@RequestMapping(value = "createNewPassword", method = RequestMethod.POST)
	public String createNewPassPost(@RequestParam(value = "tokenId", required = true) String tokenId,
			@RequestParam(value = "password", required = true) String password,
			@RequestParam(value = "cpassword", required = true) String cpassword, final RedirectAttributes redirectAttrs, Model model) {

		User owner = resetPassService.checkTokenValidity(tokenId);
		if (owner == null) {
			model.addAttribute("error_msg", "Your token is invalid. Request a new one");
			model.addAttribute("tokenId", tokenId);
			return CREATE_NEW_PASSWORD_JSP;
		}
		
		if(StringUtils.isEmpty(password) || !password.equals(cpassword)){
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
	
	@RequestMapping(value = "successfulPasswordChange", method = RequestMethod.GET)
	public String successfulPasswordChange(@ModelAttribute("user") User user, Model model){
		String msg = "Successfully changed password for user: " + user.getFirstName() + " " + user.getLastName() +
				" (" + user.getEmail() + ") ";
		model.addAttribute("msg", msg);
		
		return SUCCESS_SCREEN_JSP;
	}
	
	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
    private String buildResetMailContent(User user, String tokenId, String fileName)
            throws IOException, URISyntaxException {
        String messageText = new String(Files.readAllBytes(Paths.get(getClass().getResource(
                fileName).toURI())));
        messageText = messageText.replace("{user.firstName}", user.getFirstName());
        String url = createNewPasswordUrl+tokenId;
        messageText = messageText.replace("{url}", url);
        return messageText;
    }
    
    private String buildWelcomeMailContent(User user, String fileName)
            throws IOException, URISyntaxException {
        String messageText = new String(Files.readAllBytes(Paths.get(getClass().getResource(
                fileName).toURI())));
        messageText = messageText.replace("{user.firstName}", user.getFirstName());
        return messageText;
    }
    
    public void setMailService(MailService mailService) {
    	this.mailService = mailService;
    }
}
