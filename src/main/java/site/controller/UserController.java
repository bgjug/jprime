package site.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import site.model.User;
import site.repository.UserRepository;

@Controller
public class UserController {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;

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
	
	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
}
