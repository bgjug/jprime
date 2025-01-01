package site.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import site.app.Application;
import site.facade.MailService;
import site.model.User;
import site.repository.UserRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static site.controller.UserController.CREATE_NEW_PASSWORD_JSP;
import static site.controller.UserController.RESET_PASSWORD_JSP;

/**
 * @author Zhorzh Raychev
 */
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Transactional
class ResetPasswordTest {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MailService mailService;

    @BeforeEach
    void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

		User user = new User();
		user.setEmail("testEmail@gmail.com");
		user.setFirstName("Zhorzh");
		user.setLastName("Raychev");
		user.setPhone("0888181912");
		user.setPassword("abcd1234");
		userRepository.save(user);
	}

    @Test
    void showResetPasswordForm() throws Exception{
		mockMvc.perform(get("/resetPassword"))
        .andExpect(status().isOk())
        .andExpect(view().name(RESET_PASSWORD_JSP));
	}

    @Test
    void resetPasswordFullTest() throws Exception {
		assertThat(mailService, instanceOf(MailServiceMock.class));
		MailServiceMock mailService = (MailServiceMock) this.mailService;
		mailService.clear();

		 mockMvc.perform(post("/resetPassword")
	                .param("email", "testEmail@gmail.com"))
	                .andExpect(status().isFound())
	                .andExpect(view().name("redirect:/resetPassword"));

		 assertThat(mailService.getRecipientAddresses().size(), is(1));
		 String lastMessageText = mailService.getMessageTexts().get(0);
		 assertThat(lastMessageText, notNullValue());
		 String resetPassURL = getResetPassURL(lastMessageText);
		 assertThat(resetPassURL, notNullValue());
			
		String tokenId = getTokenIdFromURL(resetPassURL);
		assertThat(resetPassURL, notNullValue());
		mockMvc.perform(get("/createNewPassword")
					.param("tokenId", tokenId))
					.andExpect(status().isOk())
				    .andExpect(view().name(CREATE_NEW_PASSWORD_JSP))
				    .andExpect(model().attribute("tokenId", equalTo(tokenId)));
		
		mockMvc.perform(post("/createNewPassword")
		                .param("tokenId", tokenId)
		                .param("password", "PASSWORDS")
		                .param("cpassword", "DONT MATCH"))
					   .andExpect(status().isOk())
		               .andExpect(view().name(CREATE_NEW_PASSWORD_JSP))
		               .andExpect(model().attribute("error_msg", notNullValue()))
		    	       .andExpect(model().attribute("tokenId", equalTo(tokenId)));
		 
		 User user = userRepository.findUserByEmail("testEmail@gmail.com");
		 String oldPasswordHash = user.getPassword();
		 
		 mockMvc.perform(post("/createNewPassword")
	                .param("tokenId", tokenId)
	                .param("password", "aaaa1234")
	                .param("cpassword", "aaaa1234"))
	                .andExpect(status().is3xxRedirection())
	                .andExpect(view().name("redirect:/successfulPasswordChange"));
		 
		  user = userRepository.findUserByEmail("testEmail@gmail.com");
		  String newPasswordHash = user.getPassword();
		  
		  assertThat(oldPasswordHash, not(equalTo(newPasswordHash)));
		  
		mockMvc.perform(get("/createNewPassword")
			.param("tokenId", tokenId))
			.andExpect(status().is3xxRedirection())
		    .andExpect(view().name("redirect:/home"));
		  
	}
	
	private String getTokenIdFromURL(String resetPassULR2) {
		
		int indexOf = resetPassULR2.indexOf("tokenId=");
		return resetPassULR2.substring(indexOf+8);
	}

	private String getResetPassURL(String lastMessageText) {
		int startOfURL = lastMessageText.indexOf("class=\"reseturl\" href=\"");
		int endOfURL = lastMessageText.indexOf("\"", startOfURL + 24);
		return lastMessageText.substring(startOfURL + 23, endOfURL);
	}
}
