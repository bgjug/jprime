package site.controller;

import java.time.LocalDateTime;
import java.util.List;

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
import site.model.Session;
import site.model.SessionLevel;
import site.model.SessionType;
import site.model.Speaker;
import site.model.Submission;
import site.model.SubmissionStatus;
import site.model.VenueHall;
import site.repository.SessionRepository;
import site.repository.SpeakerRepository;
import site.repository.SubmissionRepository;
import site.repository.VenueHallRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for AgendaController.
 * Tests agenda and talk display functionality.
 */
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Transactional
class AgendaControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private SpeakerRepository speakerRepository;

    @Autowired
    private VenueHallRepository venueHallRepository;

    @Autowired
    private BranchService branchService;

    private MockMvc mockMvc;
    private Session testSession;
    private Branch currentBranch;

    @BeforeAll
    public static void beforeAll(@Autowired BranchService branchService) {
        DefaultBranchUtil.createDefaultBranch(branchService);
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        sessionRepository.deleteAll();
        submissionRepository.deleteAll();
        speakerRepository.deleteAll();
        venueHallRepository.deleteAll();

        currentBranch = branchService.getCurrentBranch();
        currentBranch.setAgendaPublished(true);
        branchService.createBranch(currentBranch, List.of());

        // Create hall
        VenueHall hallA = new VenueHall("Hall A", "Main Hall");
        hallA = venueHallRepository.save(hallA);

        // Create speaker and submission
        Speaker speaker = new Speaker("John", "Doe", "john@example.com", "Java Expert", "@johndoe");
        speaker = speakerRepository.save(speaker);

        Submission submission = new Submission(
            "Test Talk", "Amazing content", SessionLevel.ADVANCED,
            SessionType.CONFERENCE_SESSION, speaker, SubmissionStatus.CONFIRMED, false
        );
        submission.branch(currentBranch);
        submission = submissionRepository.save(submission);

        // Create session
        testSession = new Session(submission,
            LocalDateTime.now().plusDays(1).withHour(10).withMinute(0),
            LocalDateTime.now().plusDays(1).withHour(11).withMinute(0), hallA);
        testSession = sessionRepository.save(testSession);
    }

    @Test
    void getAgenda_shouldReturnAgendaPage() throws Exception {
        mockMvc.perform(get("/agenda"))
            .andExpect(status().isOk())
            .andExpect(view().name("talks"))
            .andExpect(model().attributeExists("tags"))
            .andExpect(model().attributeExists("alpha"))
            .andExpect(model().attributeExists("beta"))
            .andExpect(model().attributeExists("workshops"))
            .andExpect(model().attributeExists("agenda"))
            .andExpect(model().attribute("agenda", true));
    }

    @Test
    void getAgendaById_shouldReturnTalkPage() throws Exception {
        mockMvc.perform(get("/agenda/" + testSession.getId()))
            .andExpect(status().isOk())
            .andExpect(view().name("talk"))
            .andExpect(model().attributeExists("tags"))
            .andExpect(model().attributeExists("jprime_year"))
            .andExpect(model().attribute("talk", testSession));
    }

    @Test
    void getAgendaById_withInvalidId_shouldReturn404() throws Exception {
        mockMvc.perform(get("/agenda/999999"))
            .andExpect(status().isOk())
            .andExpect(view().name("404"));
    }

    @Test
    void getAgenda_shouldIncludeFirstAndSecondDayDates() throws Exception {
        mockMvc.perform(get("/agenda"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("firstDayDate"))
            .andExpect(model().attributeExists("secondDayDate"));
    }
}
