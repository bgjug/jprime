package site.api;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import site.app.Application;
import site.facade.BranchService;
import site.model.Branch;
import site.model.SessionLevel;
import site.model.SessionType;
import site.model.Speaker;
import site.model.Submission;
import site.model.SubmissionStatus;
import site.repository.SpeakerRepository;
import site.repository.SubmissionRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {Application.class})
@WebAppConfiguration
@AutoConfigureMockMvc
@Transactional
class SpeakerRestControllerTest {

    private static final TypeReference<? extends List<Speaker>> SPEAKER_LIST = new TypeReference<>() {};

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void beforeAll(@Autowired SpeakerRepository speakerRepository,
        @Autowired SubmissionRepository submissionRepository, @Autowired BranchService branchService) {
        // Perform actions after all test beans are created in the Spring context

        submissionRepository.deleteAll();
        speakerRepository.deleteAll();

        Branch branch = branchService.getCurrentBranch();
        createSpeakers(branch, speakerRepository, submissionRepository);
    }

    private static void createSpeaker(SpeakerRepository speakerRepository,
        SubmissionRepository submissionRepository, Branch branch, String firstName, String lastName,
        String email, SubmissionStatus status, boolean featured) {
        Speaker speaker = new Speaker(firstName, lastName, email, "", "");
        Submission submission =
            new Submission("session", "description", SessionLevel.BEGINNER, SessionType.CONFERENCE_SESSION,
                speaker, status, featured).branch(branch);
        submissionRepository.save(submission);
        speaker.getSubmissions().add(submission);
        speakerRepository.save(speaker);
    }

    private static void createSpeakers(Branch branch, SpeakerRepository speakerRepository,
        SubmissionRepository submissionRepository) {
        createSpeaker(speakerRepository, submissionRepository, branch, "FirstSpeaker", "FirstLastName",
            "first@jprime.io", SubmissionStatus.ACCEPTED, false);
        createSpeaker(speakerRepository, submissionRepository, branch, "SecondSpeaker", "SecondLastName",
            "second@jprime.io", SubmissionStatus.ACCEPTED, false);
        createSpeaker(speakerRepository, submissionRepository, branch, "ThirdSpeaker", "ThirdLastName",
            "third@jprime.io", SubmissionStatus.SUBMITTED, true);
        createSpeaker(speakerRepository, submissionRepository, branch, "ForthSpeaker", "ForthLastName",
            "fourth@jprime.io", SubmissionStatus.SUBMITTED, false);
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void searchAllSpeakers() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/speaker"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        List<Speaker> speakerList =
            new ObjectMapper().readValue(result.getResponse().getContentAsString(), SPEAKER_LIST);
        assertEquals(3, speakerList.size());
        speakerList.forEach(sp -> {
            assertNotNull(sp.getName());
            assertNotNull(sp.getEmail());
        });

        assertTrue(speakerList.stream()
            .map(sp -> sp.isFeatured() || sp.isAccepted())
            .reduce(false, Boolean::logicalOr));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void searchForSpeaker() throws Exception {
        SpeakerSearch search = new SpeakerSearch("first", "", null);
        MvcResult result = mockMvc.perform(post("/api/speaker/search").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(search)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        List<Speaker> speakerList =
            new ObjectMapper().readValue(result.getResponse().getContentAsString(), SPEAKER_LIST);
        assertEquals(1, speakerList.size());
        Speaker spe = speakerList.get(0);
        assertTrue(spe.isAccepted());
        assertFalse(spe.isFeatured());
    }
}