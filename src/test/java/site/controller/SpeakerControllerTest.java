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
import site.model.Branch;
import site.model.SessionLevel;
import site.model.SessionType;
import site.model.Speaker;
import site.model.Submission;
import site.model.SubmissionStatus;
import site.repository.SpeakerRepository;
import site.repository.SubmissionRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for SpeakerController.
 * Tests public speaker display functionality.
 */
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Transactional
class SpeakerControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private SpeakerRepository speakerRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private BranchService branchService;

    private MockMvc mockMvc;
    private Speaker confirmedSpeaker;
    private Speaker featuredSpeaker;
    private Speaker submittedSpeaker;
    private Branch currentBranch;

    @BeforeAll
    public static void beforeAll(@Autowired BranchService branchService) {
        DefaultBranchUtil.createDefaultBranch(branchService);
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        submissionRepository.deleteAll();
        speakerRepository.deleteAll();

        currentBranch = branchService.getCurrentBranch();

        // Confirmed speaker
        confirmedSpeaker = new Speaker("John", "Doe", "john@example.com", "Java Expert", "@johndoe");
        confirmedSpeaker = speakerRepository.save(confirmedSpeaker);

        Submission confirmedSubmission = new Submission(
            "Confirmed Talk", "Content", SessionLevel.ADVANCED,
            SessionType.CONFERENCE_SESSION, confirmedSpeaker, SubmissionStatus.CONFIRMED, false
        );
        confirmedSubmission.branch(currentBranch);
        confirmedSubmission = submissionRepository.save(confirmedSubmission);
        confirmedSpeaker.getSubmissions().add(confirmedSubmission);
        speakerRepository.save(confirmedSpeaker);

        // Featured speaker
        featuredSpeaker = new Speaker("Jane", "Smith", "jane@example.com", "Spring Expert", "@janesmith");
        featuredSpeaker = speakerRepository.save(featuredSpeaker);

        Submission featuredSubmission = new Submission(
            "Featured Talk", "Amazing content", SessionLevel.INTERMEDIATE,
            SessionType.CONFERENCE_SESSION, featuredSpeaker, SubmissionStatus.SUBMITTED, true
        );
        featuredSubmission.branch(currentBranch);
        featuredSubmission = submissionRepository.save(featuredSubmission);
        featuredSpeaker.getSubmissions().add(featuredSubmission);
        speakerRepository.save(featuredSpeaker);

        // Speaker with only submitted (not confirmed) submission
        submittedSpeaker = new Speaker("Bob", "Johnson", "bob@example.com", "Kotlin Dev", "@bobjohnson");
        submittedSpeaker = speakerRepository.save(submittedSpeaker);

        Submission submittedSubmission = new Submission(
            "Submitted Talk", "Content", SessionLevel.BEGINNER,
            SessionType.CONFERENCE_SESSION, submittedSpeaker, SubmissionStatus.SUBMITTED, false
        );
        submittedSubmission.branch(currentBranch);
        submittedSubmission = submissionRepository.save(submittedSubmission);
        submittedSpeaker.getSubmissions().add(submittedSubmission);
        speakerRepository.save(submittedSpeaker);
    }

    @Test
    void getSpeakers_shouldReturnConfirmedSpeakersPage() throws Exception {
        mockMvc.perform(get("/speakers"))
            .andExpect(status().isOk())
            .andExpect(view().name("speakers"))
            .andExpect(model().attributeExists("speakers"))
            .andExpect(model().attributeExists("tags"));
    }

    @Test
    void getSpeakerById_withConfirmedSpeaker_shouldReturnSpeakerPage() throws Exception {
        mockMvc.perform(get("/speaker/" + confirmedSpeaker.getId()))
            .andExpect(status().isOk())
            .andExpect(view().name("speaker"))
            .andExpect(model().attribute("speaker", confirmedSpeaker))
            .andExpect(model().attributeExists("jprime_year"))
            .andExpect(model().attributeExists("tags"));
    }

    @Test
    void getSpeakerById_withFeaturedSpeaker_shouldReturnSpeakerPage() throws Exception {
        mockMvc.perform(get("/speaker/" + featuredSpeaker.getId()))
            .andExpect(status().isOk())
            .andExpect(view().name("speaker"))
            .andExpect(model().attribute("speaker", featuredSpeaker))
            .andExpect(model().attributeExists("jprime_year"));
    }

    @Test
    void getSpeakerById_withInvalidId_shouldReturn404() throws Exception {
        mockMvc.perform(get("/speaker/999999"))
            .andExpect(status().isOk())
            .andExpect(view().name("404"));
    }
}
