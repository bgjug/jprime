package site.controller;

import java.io.File;

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
import site.facade.CSVService;
import site.facade.MailService;
import site.model.Branch;
import site.model.SessionLevel;
import site.model.SessionType;
import site.model.Speaker;
import site.model.Submission;
import site.model.SubmissionStatus;
import site.repository.SessionRepository;
import site.repository.SpeakerRepository;
import site.repository.SubmissionRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.instanceOf;
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

    @Autowired
    private MailService mailer;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private CSVService csvFacade;

    @Autowired
    private BranchService branchService;

    private static Submission valhalla;

    private static Submission forge;

    private static Submission bootAddon;

    @Autowired
    private SpeakerRepository speakerRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        Speaker brianGoetz =
            new Speaker("Brian", "Goetz", "brian@oracle.com", "The Java Language Architect", "@briangoetz");
        Speaker ivanIvanov =
            new Speaker("Ivan St.", "Ivanov", "ivan@jprime.io", "JBoss Forge", "@ivan_stefanov");
        Speaker naydenGochev =
            new Speaker("Nayden", "Gochev", "nayden@jprio.io", "The Spring Guy", "@gochev");
        Speaker ivanIvanov2 =
            new Speaker("Ivan St.", "Ivanov", "ivan@forge.com", "JBoss Forge", "@ivan_stefanov");

        sessionRepository.deleteAll();
        submissionRepository.deleteAll();
        speakerRepository.deleteAll();

        Branch currentBranch = branchService.getCurrentBranch();
        valhalla = submissionRepository.save(
            new Submission("Project Valhalla", "Primitives in Generics", SessionLevel.ADVANCED,
                SessionType.CONFERENCE_SESSION, brianGoetz, SubmissionStatus.SUBMITTED, true).branch(
                currentBranch));
        brianGoetz.getSubmissions().add(valhalla);
        speakerRepository.save(brianGoetz);

        forge = submissionRepository.save(
            new Submission("JBoss Forge", "Productivity for Java EE", SessionLevel.INTERMEDIATE,
                SessionType.CONFERENCE_SESSION, ivanIvanov, SubmissionStatus.SUBMITTED, true).branch(currentBranch));
        ivanIvanov.getSubmissions().add(forge);
        speakerRepository.save(ivanIvanov);

        bootAddon = submissionRepository.save(
            new Submission("Spring Boot Forge Addon", "We are not hipsters", SessionLevel.BEGINNER,
                SessionType.CONFERENCE_SESSION, naydenGochev, ivanIvanov2, SubmissionStatus.SUBMITTED,
                true).branch(branchService.findBranchByYear(2016)));
        naydenGochev.getSubmissions().add(bootAddon);
        speakerRepository.save(naydenGochev);
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
        assertThat(mailer, instanceOf(MailServiceMock.class));
        MailServiceMock mailer = (MailServiceMock) this.mailer;
        mailer.clear();

        mockMvc.perform(get("/admin/submission/accept/" + valhalla.getId()))
            .andExpect(status().isOk())
            .andExpect(view().name(ADMIN_SUBMISSION_VIEW_JSP));

        assertThat(valhalla.getStatus(), is(SubmissionStatus.ACCEPTED));
        assertThat(forge.getStatus(), is(SubmissionStatus.SUBMITTED));

        assertThat(mailer.getRecipientAddresses().size(), is(1));
        assertThat(mailer.getRecipientAddresses(), contains(valhalla.getSpeaker().getEmail()));
    }

    @Test
    void rejectSubmissionShouldChangeTheSubmissionStatus() throws Exception {
        assertThat(mailer, instanceOf(MailServiceMock.class));
        MailServiceMock mailer = (MailServiceMock) this.mailer;
        mailer.clear();

        mockMvc.perform(get("/admin/submission/reject/" + forge.getId()))
            .andExpect(status().isOk())
            .andExpect(view().name(ADMIN_SUBMISSION_VIEW_JSP));

        assertThat(valhalla.getStatus(), is(SubmissionStatus.SUBMITTED));
        assertThat(forge.getStatus(), is(SubmissionStatus.REJECTED));

        assertThat(mailer.getRecipientAddresses().size(), is(1));
        assertThat(mailer.getRecipientAddresses(), contains(forge.getSpeaker().getEmail()));
    }

    @Test
    void submissionStatusChangeShouldSendEmailToCoSpeakerToo() throws Exception {
        assertThat(mailer, instanceOf(MailServiceMock.class));
        MailServiceMock mailer = (MailServiceMock) this.mailer;
        mailer.clear();

        mockMvc.perform(get("/admin/submission/accept/" + bootAddon.getId()))
            .andExpect(status().isOk())
            .andExpect(view().name(ADMIN_SUBMISSION_VIEW_JSP));

        assertThat(mailer.getRecipientAddresses().size(), is(2));
        assertThat(mailer.getRecipientAddresses(),
            contains(bootAddon.getSpeaker().getEmail(), bootAddon.getCoSpeaker().getEmail()));
    }

    @Test
    void editSubmissionFormShouldContainInModelSubmissionToBeEdited() throws Exception {
        mockMvc.perform(get("/admin/submission/edit/" + forge.getId()))
            .andExpect(view().name(ADMIN_SUBMISSION_EDIT_JSP))
            .andExpect(status().isOk())
            .andExpect(model().attribute("submission", is(forge)));
    }

    @Test
    void exportSubmissionsAsCSVShouldReturnSubmissionsCSVFile() throws Exception {
        File exportSubmissions = csvFacade.exportSubmissions(
            submissionRepository.findByBranchAndStatus(branchService.getCurrentBranch(),
                SubmissionStatus.SUBMITTED));
        String length = Long.toString(exportSubmissions.length());

        mockMvc.perform(get("/admin/submission/exportCSV"))
            .andExpect(status().isOk())
            .andExpect(header().string("Content-Length", length));

        exportSubmissions.delete();
    }
}
