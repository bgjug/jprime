package site.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import site.app.Application;
import site.config.Globals;
import site.facade.CSVService;
import site.model.Branch;
import site.model.SessionLevel;
import site.model.SessionType;
import site.model.Speaker;
import site.model.Submission;
import site.model.SubmissionStatus;
import site.repository.SubmissionRepository;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static site.controller.SubmissionController.ADMIN_SUBMISSION_EDIT_JSP;
import static site.controller.SubmissionController.ADMIN_SUBMISSION_VIEW_JSP;

/**
 * @author Ivan St. Ivanov
 */
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Transactional
class SubmissionControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private MailServiceMock mailer;

    @Autowired
    @Qualifier(SubmissionRepository.NAME)
    private SubmissionRepository submissionRepository;
    
    @Autowired
    @Qualifier(CSVService.NAME)
    private CSVService csvFacade;

    private Submission valhalla;
    private Submission forge;
    private Submission bootAddon;

    @BeforeEach
    void setUp() throws Exception {
        final SubmissionController bean = wac.getBean(SubmissionController.class);
        this.mailer = new MailServiceMock();
        bean.setMailFacade(mailer);

        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        Speaker brianGoetz = new Speaker("Brian", "Goetz", "brian@oracle.com", "The Java Language Architect", "@briangoetz", true, true);
        Speaker ivanIvanov = new Speaker("Ivan St.", "Ivanov", "ivan@jprime.io", "JBoss Forge", "@ivan_stefanov", false, true);
        Speaker naydenGochev = new Speaker("Nayden", "Gochev", "nayden@jprio.io", "The Spring Guy", "@gochev", false, true);
        Speaker ivanIvanov2 = new Speaker("Ivan St.", "Ivanov", "ivan@forge.com", "JBoss Forge", "@ivan_stefanov", false, true);

        valhalla = submissionRepository.save(new Submission("Project Valhalla", "Primitives in Generics",
                SessionLevel.ADVANCED, SessionType.CONFERENCE_SESSION, brianGoetz));
        valhalla.setBranch(Globals.CURRENT_BRANCH);
        forge = submissionRepository.save(new Submission("JBoss Forge", "Productivity for Java EE",
                SessionLevel.INTERMEDIATE, SessionType.CONFERENCE_SESSION, ivanIvanov));
        forge.setBranch(Globals.CURRENT_BRANCH);
        bootAddon = submissionRepository.save(new Submission("Spring Boot Forge Addon", "We are not hipsters",
                SessionLevel.BEGINNER, SessionType.CONFERENCE_SESSION, naydenGochev, ivanIvanov2));
        bootAddon.setBranch(Branch.YEAR_2016);
    }

    @Test
    void viewAllSubmissionsShouldReturnAllSubmissions() throws Exception {
        mockMvc.perform(get("/admin/submission/view/all"))
                .andExpect(status().isOk())
                .andExpect(view().name(ADMIN_SUBMISSION_VIEW_JSP))
                .andExpect(model().attribute("submissions", contains(valhalla, forge, bootAddon)));
    }

    @Test
    void viewSubmissionsShouldReturnSubmissionsForCurrentYear() throws Exception {
        mockMvc.perform(get("/admin/submission/view"))
                .andExpect(status().isOk())
                .andExpect(view().name(ADMIN_SUBMISSION_VIEW_JSP))
                .andExpect(model().attribute("submissions", contains(valhalla, forge)));
    }


    @Test
    void viewSubmissionsShouldReturnAllSubmissionsForPrevYear() throws Exception {
        mockMvc.perform(get("/admin/submission/view/2016"))
                .andExpect(status().isOk())
                .andExpect(view().name(ADMIN_SUBMISSION_VIEW_JSP))
                .andExpect(model().attribute("submissions", contains(bootAddon)));
    }

    @Test
    void showSubmissionFormShouldReturnTheFormAndEmptySubmission() throws Exception {
        mockMvc.perform(get("/admin/submission/add"))
                .andExpect(status().isOk())
                .andExpect(view().name(ADMIN_SUBMISSION_EDIT_JSP))
                .andExpect(model().attribute("submission", is(new Submission())));
    }

    @Test
    void acceptSubmissionShouldChangeTheSubmissionStatus() throws Exception {
        mockMvc.perform(get("/admin/submission/accept/" + valhalla.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name(ADMIN_SUBMISSION_VIEW_JSP));

        assertThat(valhalla.getStatus(), is(SubmissionStatus.ACCEPTED));
        assertThat(forge.getStatus(), is(SubmissionStatus.SUBMITTED));

        assertThat(mailer.recipientAddresses.size(), is(1));
        assertThat(mailer.recipientAddresses, contains(valhalla.getSpeaker().getEmail()));
    }

    @Test
    void rejectSubmissionShouldChangeTheSubmissionStatus() throws Exception {
        mockMvc.perform(get("/admin/submission/reject/" + forge.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name(ADMIN_SUBMISSION_VIEW_JSP));

        assertThat(valhalla.getStatus(), is(SubmissionStatus.SUBMITTED));
        assertThat(forge.getStatus(), is(SubmissionStatus.REJECTED));

        assertThat(mailer.recipientAddresses.size(), is(1));
        assertThat(mailer.recipientAddresses, contains(forge.getSpeaker().getEmail()));
    }

    @Test
    void submissionStatusChangeShouldSendEmailToCoSpeakerToo() throws Exception {
        mockMvc.perform(get("/admin/submission/accept/" + bootAddon.getId()))
                .andExpect(status().isOk())
               .andExpect(view().name(ADMIN_SUBMISSION_VIEW_JSP));

        assertThat(mailer.recipientAddresses.size(), is(2));
        assertThat(mailer.recipientAddresses, contains(bootAddon.getSpeaker().getEmail(),
                bootAddon.getCoSpeaker().getEmail()));
    }

    @Test
    void editSubmissionFormShouldContainInModelSubmissionToBeEdited() throws Exception {
        mockMvc.perform(get("/admin/submission/edit/" + forge.getId()))
                .andExpect(view().name(ADMIN_SUBMISSION_EDIT_JSP))
                .andExpect(status().isOk())
                .andExpect(model().attribute("submission", is(forge)));
    }

    @Test
    void exportSubmissionsAsCSVShouldReturnSubmissionsCSVFile() throws Exception{
    	File exportSubmissions = csvFacade.exportSubmissions(submissionRepository.
    			findByBranchAndStatus(Globals.CURRENT_BRANCH, SubmissionStatus.SUBMITTED));
    	String length = Long.toString(exportSubmissions.length());
    	
    	mockMvc.perform(get("/admin/submission/exportCSV/"))
    	.andExpect(status().isOk())
    	.andExpect(header().string("Content-Length", length));
    	
    	exportSubmissions.delete();
    }
}
