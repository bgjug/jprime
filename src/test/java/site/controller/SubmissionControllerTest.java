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
import site.model.SessionLevel;
import site.model.Speaker;
import site.model.Submission;
import site.model.SubmissionStatus;
import site.repository.SubmissionRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static site.controller.SubmissionController.ADMIN_SUBMISSION_EDIT_JSP;
import static site.controller.SubmissionController.ADMIN_SUBMISSION_VIEW_JSP;

/**
 * @author Ivan St. Ivanov
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@Transactional
public class SubmissionControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private MailServiceMock mailer;

    @Autowired
    @Qualifier(SubmissionRepository.NAME)
    private SubmissionRepository submissionRepository;

    private Submission valhalla;
    private Submission forge;
    private Submission bootAddon;

    @Before
    public void setUp() throws Exception {
        final SubmissionController bean = wac.getBean(SubmissionController.class);
        this.mailer = new MailServiceMock();
        bean.setMailFacade(mailer);

        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        Speaker brianGoetz = new Speaker("Brian", "Goetz", "brian@oracle.com", "The Java Language Architect", "@briangoetz", true);
        Speaker ivanIvanov = new Speaker("Ivan St.", "Ivanov", "ivan@jprime.io", "JBoss Forge", "@ivan_stefanov", false);
        Speaker naydenGochev = new Speaker("Nayden", "Gochev", "nayden@jprio.io", "The Spring Guy", "@gochev", false);
        Speaker ivanIvanov2 = new Speaker("Ivan St.", "Ivanov", "ivan@forge.com", "JBoss Forge", "@ivan_stefanov", false);

        valhalla = submissionRepository.save(new Submission("Project Valhalla", "Primitives in Generics",
                SessionLevel.ADVANCED, brianGoetz));
        forge = submissionRepository.save(new Submission("JBoss Forge", "Productivity for Java EE",
                SessionLevel.INTERMEDIATE, ivanIvanov));
        bootAddon = submissionRepository.save(new Submission("Spring Boot Forge Addon", "We are not hipsters",
                SessionLevel.BEGINNER, naydenGochev, ivanIvanov2));
    }

    @Test
    public void viewSubmissionsShouldReturnAllSubmissions() throws Exception {
        mockMvc.perform(get("/admin/submission/view"))
                .andExpect(status().isOk())
                .andExpect(view().name(ADMIN_SUBMISSION_VIEW_JSP))
                .andExpect(model().attribute("submissions", contains(valhalla, forge, bootAddon)));
    }

    @Test
    public void showSubmissionFormShouldReturnTheFormAndEmptySubmission() throws Exception {
        mockMvc.perform(get("/admin/submission/add"))
                .andExpect(status().isOk())
                .andExpect(view().name(ADMIN_SUBMISSION_EDIT_JSP))
                .andExpect(model().attribute("submission", is(new Submission())));
    }

    @Test
    public void acceptSubmissionShouldChangeTheSubmissionStatus() throws Exception {
        mockMvc.perform(get("/admin/submission/accept/" + valhalla.getId()))
                .andExpect(status().isFound())
                .andExpect(result -> assertThat(result.getResponse().getRedirectedUrl(),
                        is("/admin/submission/view")));

        assertThat(valhalla.getStatus(), is(SubmissionStatus.ACCEPTED));
        assertThat(forge.getStatus(), is(SubmissionStatus.SUBMITTED));

        assertThat(mailer.recipientAddresses.size(), is(1));
        assertThat(mailer.recipientAddresses, contains(valhalla.getSpeaker().getEmail()));
    }

    @Test
    public void rejectSubmissionShouldChangeTheSubmissionStatus() throws Exception {
        mockMvc.perform(get("/admin/submission/reject/" + forge.getId()))
                .andExpect(status().isFound())
                .andExpect(result -> assertThat(result.getResponse().getRedirectedUrl(),
                        is("/admin/submission/view")));

        assertThat(valhalla.getStatus(), is(SubmissionStatus.SUBMITTED));
        assertThat(forge.getStatus(), is(SubmissionStatus.REJECTED));

        assertThat(mailer.recipientAddresses.size(), is(1));
        assertThat(mailer.recipientAddresses, contains(forge.getSpeaker().getEmail()));
    }

    @Test
    public void submissionStatusChangeShouldSendEmailToCoSpeakerToo() throws Exception {
        mockMvc.perform(get("/admin/submission/accept/" + bootAddon.getId()))
                .andExpect(status().isFound())
                .andExpect(result -> assertThat(result.getResponse().getRedirectedUrl(),
                        is("/admin/submission/view")));

        assertThat(mailer.recipientAddresses.size(), is(2));
        assertThat(mailer.recipientAddresses, contains(bootAddon.getSpeaker().getEmail(),
                bootAddon.getCoSpeaker().getEmail()));
    }

    @Test
    public void editSubmissionFormShouldContainInModelSubmissionToBeEdited() throws Exception {
        mockMvc.perform(get("/admin/submission/edit/" + forge.getId()))
                .andExpect(view().name(ADMIN_SUBMISSION_EDIT_JSP))
                .andExpect(status().isOk())
                .andExpect(model().attribute("submission", is(forge)));
    }
}
