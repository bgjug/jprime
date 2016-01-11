package site.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import site.app.Application;
import site.model.SessionLevel;
import site.model.Submission;
import site.model.SubmissionStatus;
import site.repository.SubmissionRepository;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static site.controller.CfpController.CFP_OPEN_JSP;

/**
 * @author Ivan St. Ivanov
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@Transactional
public class CfpControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private MailServiceMock mailer;

    @Autowired
    @Qualifier(SubmissionRepository.NAME)
    private SubmissionRepository submissionRepository;

    @Before
    public void setup() throws Exception {
        final CfpController bean = wac.getBean(CfpController.class);
        this.mailer = new MailServiceMock();
        bean.setMailFacade(mailer);
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void getShouldReturnEmptySubscription() throws Exception {
        mockMvc.perform(get("/cfp"))
                .andExpect(status().isOk())
                .andExpect(view().name(CFP_OPEN_JSP));
    }

    @Test
    public void shouldSubmitSessionWithSingleSpeaker() throws Exception {
        mockMvc.perform(fileUpload("/cfp")
                .file(new MockMultipartFile("speakerImage", new byte[] {}))
                .file(new MockMultipartFile("coSpeakerImage", new byte[] {}))
                .param("title", "JBoss Forge")
                .param("description", "This is the best tool")
                .param("level", SessionLevel.BEGINNER.toString().toUpperCase())
                .param("speaker.firstName", "Ivan")
                .param("speaker.lastName", "Ivanov")
                .param("speaker.email", "ivan@jprime.io")
                .param("speaker.twitter", "@ivan_stefanov")
                .param("speaker.bio", "Ordinary decent nerd"))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/"));

        final List<Submission> allSubmissions = submissionRepository.findAll();
        assertThat(allSubmissions.size(), is(1));

        Submission submission = allSubmissions.get(0);
        assertThat(submission.getTitle(), is("JBoss Forge"));
        assertThat(submission.getStatus(), is(SubmissionStatus.SUBMITTED));
        assertThat(submission.getSpeaker().getEmail(), is("ivan@jprime.io"));
        assertThat(submission.getCoSpeaker(), is(nullValue()));

        assertThat(mailer.recipientAddresses.size(), is(2));
        assertThat(mailer.recipientAddresses, contains("ivan@jprime.io", "conference@jprime.io"));
    }

    @Test
    public void shouldSubmitSessionWithCoSpeaker() throws Exception {
        mockMvc.perform(fileUpload("/cfp")
                .file(new MockMultipartFile("speakerImage", new byte[] {}))
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
                .param("coSpeaker.bio", "Ordinary decent nerd"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));

        final List<Submission> allSubmissions = submissionRepository.findAll();
        assertThat(allSubmissions.size(), is(1));

        Submission submission = allSubmissions.get(0);
        assertThat(submission.getTitle(), is("Boot Forge Addon"));
        assertThat(submission.getStatus(), is(SubmissionStatus.SUBMITTED));
        assertThat(submission.getSpeaker().getEmail(), is("nayden@jprime.io"));
        assertThat(submission.getCoSpeaker().getEmail(), is("ivan@jprime.io"));

        assertThat(mailer.recipientAddresses.size(), is(3));
        assertThat(mailer.recipientAddresses, contains("nayden@jprime.io", "ivan@jprime.io", "conference@jprime.io"));
    }
}