package site.controller;

import java.time.LocalDateTime;
import java.util.List;

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
import org.springframework.transaction.annotation.Transactional;

import site.app.Application;
import site.facade.BranchService;
import site.facade.DefaultBranchUtil;
import site.facade.MailService;
import site.model.Branch;
import site.model.SessionLevel;
import site.model.Submission;
import site.model.SubmissionStatus;
import site.repository.SubmissionRepository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.instanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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
        String cfpPage = CfpController.CFP_CLOSED_JSP;
        Branch currentBranch = branchService.getCurrentBranch();
        Assertions.assertThat(currentBranch).isNotNull();

        if (currentBranch.getCfpCloseDate().isAfter(LocalDateTime.now()) && currentBranch.getCfpOpenDate()
            .isBefore(LocalDateTime.now())) {
            cfpPage = CfpController.CFP_OPEN_JSP;
        }

        mockMvc.perform(get("/cfp")).andExpect(status().isOk()).andExpect(view().name(cfpPage));
    }

    @Test
    void shouldSubmitSessionWithSingleSpeaker() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/captcha-image")).andExpect(status().isOk()).andReturn();
        HttpSession session = mvcResult.getRequest().getSession();
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
                .session((MockHttpSession) session))
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
        MvcResult mvcResult = mockMvc.perform(get("/captcha-image")).andExpect(status().isOk()).andReturn();
        HttpSession session = mvcResult.getRequest().getSession();
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
                .session((MockHttpSession) session))
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
}