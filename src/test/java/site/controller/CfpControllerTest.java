package site.controller;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import site.app.Application;
import site.facade.BranchService;
import site.facade.DefaultBranchUtil;
import site.facade.MailService;
import site.model.Branch;
import site.model.SessionLevel;
import site.model.Submission;
import site.model.SubmissionStatus;
import site.model.Speaker;
import site.repository.SpeakerRepository;
import site.repository.SubmissionRepository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.instanceOf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * @author Ivan St. Ivanov
 */
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Transactional
@AutoConfigureMockMvc
class CfpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MailService mailer;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private BranchService branchService;

    @Autowired
    private SpeakerRepository speakerRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private MailServiceMock mailerMock;

    @BeforeAll
    public static void beforeAll(@Autowired BranchService branchService) {
        DefaultBranchUtil.createDefaultBranch(branchService);
    }

    @BeforeEach
    void cleanupSubmissionRepository () {
        submissionRepository.deleteAll();

        assertThat(mailer, instanceOf(MailServiceMock.class));
        mailerMock = (MailServiceMock) this.mailer;
        mailerMock.clear();
    }

    @Test
    void getShouldReturnEmptySubscription() throws Exception {
        saveSpeaker("ivan@jprime.io", "Ivan", "Original");
        String cfpPage = CfpController.CFP_CLOSED_JSP;
        Branch currentBranch = branchService.getCurrentBranch();
        Assertions.assertThat(currentBranch).isNotNull();

        if (currentBranch.getCfpCloseDate().isAfter(LocalDateTime.now()) && currentBranch.getCfpOpenDate()
            .isBefore(LocalDateTime.now())) {
            cfpPage = CfpController.CFP_OPEN_JSP;
        }

        mockMvc.perform(get("/cfp").with(user("ivan@jprime.io"))).andExpect(status().isOk()).andExpect(view().name(cfpPage));
    }

    @Test
    void shouldSubmitSessionWithSingleSpeaker() throws Exception {
        saveSpeaker("ivan@jprime.io", "Ivan", "Original");
        MvcResult mvcResult = mockMvc.perform(get("/captcha-image")).andExpect(status().isOk()).andReturn();
        HttpSession session = mvcResult.getRequest().getSession();
        Assertions.assertThat(session).isNotNull();
        String captcha = (String) session.getAttribute("session_captcha");

        mockMvc.perform(multipart("/cfp").file(new MockMultipartFile("speakerImage", new byte[] {}))
                .file(new MockMultipartFile("coSpeakerImage", new byte[] {}))
                .param("title", "JBoss Forge")
                .param("description", "This is the best tool")
                .param("level", SessionLevel.BEGINNER.toString().toUpperCase())
                .param("speaker.firstName", "Ivan")
                .param("speaker.lastName", "Ivanov")
                .param("speaker.email", "ivan@jprime.io")
                .param("speaker.twitter", "@ivan_stefanov")
                .param("speaker.bio", "Ordinary decent nerd")
                .param("captcha", captcha)
                .session((MockHttpSession) session)
                .with(user("ivan@jprime.io")).with(csrf()))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/cfp-thank-you"));

        final List<Submission> allSubmissions = submissionRepository.findAll();
        assertThat(allSubmissions.size(), is(1));

        Submission submission = allSubmissions.get(0);
        assertThat(submission.getTitle(), is("JBoss Forge"));
        assertThat(submission.getStatus(), is(SubmissionStatus.SUBMITTED));
        assertThat(submission.getSpeaker().getEmail(), is("ivan@jprime.io"));
        assertThat(submission.getCoSpeaker(), is(nullValue()));

        assertThat(mailerMock.getRecipientAddresses().size(), is(2));
        assertThat(mailerMock.getRecipientAddresses(), contains("ivan@jprime.io", "conference@jprime.io"));
    }

    @Test
    void shouldSubmitSessionWithCoSpeaker() throws Exception {
        saveSpeaker("nayden@jprime.io", "Nayden", "Original");
        saveSpeaker("ivan@jprime.io", "Ivan", "Original");
        MvcResult mvcResult = mockMvc.perform(get("/captcha-image")).andExpect(status().isOk()).andReturn();
        HttpSession session = mvcResult.getRequest().getSession();
        Assertions.assertThat(session).isNotNull();
        String captcha = (String) session.getAttribute("session_captcha");

        mockMvc.perform(multipart("/cfp").file(new MockMultipartFile("speakerImage", new byte[] {}))
                .file(new MockMultipartFile("coSpeakerImage", new byte[] {}))
                .param("title", "Boot Forge Addon")
                .param("description", "Forge supports Spring")
                .param("level", SessionLevel.BEGINNER.toString().toUpperCase())
                .param("speaker.firstName", "Nayden")
                .param("speaker.lastName", "Gochev")
                .param("speaker.email", "nayden@jprime.io")
                .param("speaker.twitter", "@gochev")
                .param("speaker.bio", "Spring nerd")
                .param("coSpeaker.firstName", "Ivan")
                .param("coSpeaker.lastName", "Ivanov")
                .param("coSpeaker.email", "ivan@jprime.io")
                .param("coSpeaker.twitter", "@ivan_stefanov")
                .param("coSpeaker.bio", "Ordinary decent nerd")
                .param("captcha", captcha)
                .session((MockHttpSession) session)
                .with(user("nayden@jprime.io")).with(csrf()))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/cfp-thank-you"));

        final List<Submission> allSubmissions = submissionRepository.findAll();
        assertThat(allSubmissions.size(), is(1));

        Submission submission = allSubmissions.get(0);
        assertThat(submission.getTitle(), is("Boot Forge Addon"));
        assertThat(submission.getStatus(), is(SubmissionStatus.SUBMITTED));
        assertThat(submission.getSpeaker().getEmail(), is("nayden@jprime.io"));
        assertThat(submission.getCoSpeaker().getEmail(), is("ivan@jprime.io"));

        assertThat(mailerMock.getRecipientAddresses().size(), is(3));
        assertThat(mailerMock.getRecipientAddresses(),
            contains("nayden@jprime.io", "ivan@jprime.io", "conference@jprime.io"));
    }

    private Speaker saveSpeaker(String email, String firstName, String bio) {
        Speaker speaker = new Speaker();
        speaker.setEmail(email);
        speaker.setFirstName(firstName);
        speaker.setLastName("Test");
        speaker.setBio(bio);
        return speakerRepository.save(speaker);
    }

    private MockHttpServletRequestBuilder cfp(String loginEmail, String... params) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/captcha-image")).andReturn();
        HttpSession session = mvcResult.getRequest().getSession();
        MockHttpServletRequestBuilder request = multipart("/cfp")
            .file(new MockMultipartFile("speakerImage", new byte[] {}))
            .file(new MockMultipartFile("coSpeakerImage", new byte[] {}))
            .param("title", "T").param("description", "D")
            .param("level", SessionLevel.BEGINNER.toString().toUpperCase())
            .param("captcha", (String) session.getAttribute("session_captcha"));
        if (!List.of(params).contains("speaker.email")) {
            request.param("speaker.email", loginEmail);
        }
        for (int i = 0; i < params.length; i += 2) {
            request.param(params[i], params[i + 1]);
        }
        return request.session((MockHttpSession) session).with(user(loginEmail)).with(csrf());
    }

    @Test
    void primarySpeakerIsLoggedInUserNotTypedEmail() throws Exception {
        saveSpeaker("ivan@jprime.io", "Ivan", "Original");
        mockMvc.perform(cfp("ivan@jprime.io", "speaker.email", "evil@x.io", "speaker.firstName", "Evil"))
            .andExpect(view().name("redirect:/cfp-thank-you"));

        assertThat(submissionRepository.findAll().get(0).getSpeaker().getEmail(), is("ivan@jprime.io"));
        assertThat(speakerRepository.findByEmail("evil@x.io"), is(nullValue()));
    }

    @Test
    void primaryProfileUpdatedFromForm() throws Exception {
        saveSpeaker("ivan@jprime.io", "Ivan", "Original");
        mockMvc.perform(cfp("ivan@jprime.io", "speaker.bio", "New bio", "speaker.twitter", "@ivan"))
            .andExpect(view().name("redirect:/cfp-thank-you"));

        Speaker ivan = speakerRepository.findByEmail("ivan@jprime.io");
        assertThat(ivan.getBio(), is("New bio"));
        assertThat(ivan.getTwitter(), is("ivan"));
    }

    @Test
    void existingCoSpeakerNotOverwritten() throws Exception {
        saveSpeaker("ivan@jprime.io", "Ivan", "Original");
        Speaker nayden = saveSpeaker("nayden@jprime.io", "Nayden", "Original");
        mockMvc.perform(cfp("ivan@jprime.io", "coSpeaker.email", "nayden@jprime.io", "coSpeaker.bio", "Hacked"))
            .andExpect(view().name("redirect:/cfp-thank-you"));

        Submission submission = submissionRepository.findAll().get(0);
        assertThat(submission.getCoSpeaker().getId(), is(nayden.getId()));
        assertThat(speakerRepository.findByEmail("nayden@jprime.io").getBio(), is("Original"));
    }

    @Test
    void newCoSpeakerCreated() throws Exception {
        saveSpeaker("ivan@jprime.io", "Ivan", "Original");
        mockMvc.perform(cfp("ivan@jprime.io", "coSpeaker.email", "new@x.io", "coSpeaker.firstName", "New",
                "coSpeaker.lastName", "One", "coSpeaker.bio", "Fresh"))
            .andExpect(view().name("redirect:/cfp-thank-you"));

        assertThat(speakerRepository.findByEmail("new@x.io").getBio(), is("Fresh"));
    }

    @Test
    void newCoSpeakerWithoutBioRejected() throws Exception {
        saveSpeaker("ivan@jprime.io", "Ivan", "Original");
        mockMvc.perform(cfp("ivan@jprime.io", "coSpeaker.email", "new@x.io", "coSpeaker.firstName", "New",
                "coSpeaker.lastName", "One"))
            .andExpect(model().attributeHasFieldErrors("submission", "coSpeaker.bio"));
    }

    @Test
    void coSpeakerSameAsSelfRejected() throws Exception {
        saveSpeaker("ivan@jprime.io", "Ivan", "Original");
        mockMvc.perform(cfp("ivan@jprime.io", "coSpeaker.email", "IVAN@jprime.io"))
            .andExpect(model().attributeHasFieldErrors("submission", "coSpeaker.email"));
    }

    @Test
    void postedIdStatusFeaturedIgnored() throws Exception {
        Speaker other = saveSpeaker("other@jprime.io", "Other", "Original");
        Submission existing = new Submission(branchService.getCurrentBranch());
        existing.setTitle("Theirs");
        existing.setDescription("Theirs");
        existing.setLevel(SessionLevel.BEGINNER);
        existing.setSpeaker(other);
        existing = submissionRepository.save(existing);
        saveSpeaker("ivan@jprime.io", "Ivan", "Original");

        mockMvc.perform(cfp("ivan@jprime.io", "id", existing.getId().toString(), "status", "ACCEPTED",
                "featured", "true"))
            .andExpect(view().name("redirect:/cfp-thank-you"));

        assertThat(submissionRepository.findById(existing.getId()).get().getTitle(), is("Theirs"));
        Submission created = submissionRepository.findAll().stream()
            .filter(s -> s.getTitle().equals("T")).findFirst().get();
        assertThat(created.getStatus(), is(SubmissionStatus.SUBMITTED));
        assertThat(created.getFeatured(), is(false));
    }

    @Test
    void postedCoSpeakerIdCannotHijackOtherSpeaker() throws Exception {
        saveSpeaker("ivan@jprime.io", "Ivan", "Original");
        Speaker other = saveSpeaker("other@jprime.io", "Other", "Original");
        mockMvc.perform(cfp("ivan@jprime.io", "coSpeaker.id", other.getId().toString(),
                "coSpeaker.email", "new@x.io", "coSpeaker.firstName", "New", "coSpeaker.lastName", "One",
                "coSpeaker.bio", "Fresh"))
            .andExpect(view().name("redirect:/cfp-thank-you"));

        Speaker unchanged = speakerRepository.findByEmail("other@jprime.io");
        assertThat(unchanged.getBio(), is("Original"));
        assertThat(unchanged.getFirstName(), is("Other"));
        Speaker created = speakerRepository.findByEmail("new@x.io");
        assertThat(created.getId().equals(other.getId()), is(false));
    }

    @Test
    void rejectedSubmissionDoesNotChangeProfile() throws Exception {
        saveSpeaker("ivan@jprime.io", "Ivan", "Original");
        mockMvc.perform(cfp("ivan@jprime.io", "speaker.bio", "Changed", "coSpeaker.email", "new@x.io"))
            .andExpect(model().attributeHasFieldErrors("submission", "coSpeaker.firstName"));

        entityManager.flush();
        entityManager.clear();
        assertThat(speakerRepository.findByEmail("ivan@jprime.io").getBio(), is("Original"));
    }

    @Test
    void getPrefillsLoggedInSpeaker() throws Exception {
        saveSpeaker("ivan@jprime.io", "Ivan", "Original");
        mockMvc.perform(get("/cfp").with(user("ivan@jprime.io")))
            .andExpect(model().attribute("submission", org.hamcrest.Matchers.hasProperty("speaker",
                org.hamcrest.Matchers.allOf(org.hamcrest.Matchers.hasProperty("email", is("ivan@jprime.io")),
                    org.hamcrest.Matchers.hasProperty("bio", is("Original"))))));
    }
}
