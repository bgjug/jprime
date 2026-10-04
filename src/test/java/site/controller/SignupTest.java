package site.controller;

import org.junit.jupiter.api.BeforeAll;
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
import site.facade.BranchService;
import site.facade.DefaultBranchUtil;
import site.facade.MailService;
import site.model.User;
import site.repository.UserRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Transactional
class SignupTest {

	private static final String MSG = "We sent a link to ada@x.io. Open it to set your password.";

	@Autowired
	private WebApplicationContext wac;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private MailService mailService;

	private MockMvc mockMvc;
	private MailServiceMock mailer;

	@BeforeAll
	static void beforeAll(@Autowired BranchService branchService) {
		DefaultBranchUtil.createDefaultBranch(branchService);
	}

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
		mailer = (MailServiceMock) mailService;
		mailer.clear();
	}

	private void signup(String email) throws Exception {
		mockMvc.perform(post("/signup").param("firstName", "Ada").param("lastName", "Lovelace").param("email", email))
				.andExpect(status().isOk())
				.andExpect(view().name("successScreen"))
				.andExpect(model().attribute("msg", MSG))
				.andExpect(model().attributeExists("jprime_year"));
	}

	private void assertOneSetPasswordMail() {
		assertThat(mailer.getRecipientAddresses(), contains("ada@x.io"));
		assertThat(mailer.getSubjects(), contains("Set your JPrime password"));
		assertThat(mailer.getMessageTexts().get(0), containsString("createNewPassword?tokenId="));
	}

	@Test
	void newEmailCreatesUserAndSendsSetPasswordMail() throws Exception {
		signup("ada@x.io");
		assertThat(userRepository.findUserByEmail("ada@x.io").getPassword(), nullValue());
		assertOneSetPasswordMail();
	}

	@Test
	void existingEmailGetsSameResponseAndMail() throws Exception {
		User u = new User();
		u.setEmail("ada@x.io");
		u.setFirstName("Ada");
		u.setLastName("Lovelace");
		u.setPassword("hash");
		userRepository.save(u);

		signup("ada@x.io");

		assertThat(userRepository.findAll().stream().filter(x -> "ada@x.io".equals(x.getEmail())).count(), equalTo(1L));
		assertOneSetPasswordMail();
	}

	@Test
	void blankEmailRejected() throws Exception {
		mockMvc.perform(post("/signup").param("firstName", "Ada").param("lastName", "L").param("email", ""))
				.andExpect(view().name("signup"))
				.andExpect(model().attributeHasFieldErrors("user", "email"));
	}
}
