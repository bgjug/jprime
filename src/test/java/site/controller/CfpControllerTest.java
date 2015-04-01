package site.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import site.app.Application;
import site.model.Submission;
import site.repository.SpeakerRepository;
import site.repository.SubmissionRepository;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    @Transactional
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

    /* Fucking spring test, don't know how do to multipart post :(
    @Test
    public void postShouldGenerateSubmission() throws Exception {
        Map<String, String> contentTypeParams = new HashMap<String, String>();
        contentTypeParams.put("title", "JBoss%20Forge");
        contentTypeParams.put("description", "Productivity%20%40not%20just%41%20for%20Java%20EE");
        contentTypeParams.put("level", "INTERMEDIATE");
        contentTypeParams.put("speaker.firstName", "Ivan");
        contentTypeParams.put("speaker.lastName", "Ivanov");
        contentTypeParams.put("speaker.email", "ivan%64jprime.io");
        contentTypeParams.put("speaker.twitter", "%64ivan_stefanov");
        contentTypeParams.put("speaker.bio", "Forge%20contributor");

        MockMultipartFile multipartFile = new MockMultipartFile("file", new FileInputStream("src/test/resources/ivan.jpg"));

        MediaType mediaType = new MediaType("multipart", "form-data", contentTypeParams);
        mockMvc.perform(post("/cfp")
                .content(multipartFile.getBytes())
                .contentType(mediaType))
                .andExpect(status().isOk());
        assertThat(speakerRepository.findSpeakerByName("Ivan", "Ivanov"), not(isNull()));
    }
    */
}