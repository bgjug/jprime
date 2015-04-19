package site.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import site.app.Application;
import site.facade.MailFacade;
import site.facade.UserFacade;
import site.model.Submission;
import site.repository.SpeakerRepository;
import site.repository.SubmissionRepository;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.isNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static site.controller.CfpController.PROPOSAL_JSP;

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

    @Autowired
    @Qualifier(SubmissionRepository.NAME)
    private SubmissionRepository submissionRepository;

    @Autowired
    @Qualifier(SpeakerRepository.NAME)
    private SpeakerRepository speakerRepository;

    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void getShouldReturnEmptySubscription() throws Exception {
        mockMvc.perform(get("/cfp"))
                .andExpect(status().isOk())
                .andExpect(view().name(PROPOSAL_JSP))
                .andExpect(model().attribute("submission", is(new Submission())));
    }

    @Test
    public void postShouldGenerateSubmission() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file",
                new FileInputStream("src/test/resources/ivan.jpg"));

        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/cfp")
                .file(multipartFile)
                .param("title", "JBoss Forge")
                .param("description", "Productivity [not just] for Java EE")
                .param("level", "INTERMEDIATE")
                .param("speaker.firstName", "Ivan")
                .param("speaker.lastName", "Ivanov")
                .param("speaker.email", "ivan@jprime.io")
                .param("speaker.twitter", "ivan_stefanov")
                .param("speaker.bio", "Forge contributor"))
                .andExpect(status().isFound())
                .andExpect(result -> assertThat(result.getResponse().getRedirectedUrl(), is("/")));

        assertThat(submissionRepository.findAll().get(0).getTitle(), is("JBoss Forge"));
        assertThat(speakerRepository.findSpeakerByName("Ivan", "Ivanov"), not(isNull()));
    }

}