package site.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for AdminSpeakerController.
 * Tests speaker CRUD operations in admin panel.
 */
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Transactional
class AdminSpeakerControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private SpeakerRepository speakerRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private BranchService branchService;

    private MockMvc mockMvc;
    private Speaker testSpeaker;
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

        testSpeaker = new Speaker("John", "Doe", "john@example.com", "Java Expert", "@johndoe");
        testSpeaker = speakerRepository.save(testSpeaker);

        Submission submission = new Submission(
            "Test Talk", "Description", SessionLevel.INTERMEDIATE,
            SessionType.CONFERENCE_SESSION, testSpeaker, SubmissionStatus.CONFIRMED, false
        );
        submission.branch(currentBranch);
        submission = submissionRepository.save(submission);
        testSpeaker.getSubmissions().add(submission);
        speakerRepository.save(testSpeaker);
    }

    @Test
    void getView_shouldReturnSpeakerViewWithAllSpeakers() throws Exception {
        mockMvc.perform(get("/admin/speaker/view"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/speaker/view"))
            .andExpect(model().attributeExists("speakers"))
            .andExpect(model().attributeExists("branches"))
            .andExpect(model().attributeExists("current_branch"));
    }

    @Test
    void getView_withBranchParameter_shouldFilterByBranch() throws Exception {
        mockMvc.perform(get("/admin/speaker/view").param("branch", currentBranch.getLabel()))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/speaker/view"))
            .andExpect(model().attributeExists("speakers"))
            .andExpect(model().attribute("selected_branch", currentBranch.getLabel()));
    }

    @Test
    void getAdd_shouldReturnFormWithEmptySpeaker() throws Exception {
        mockMvc.perform(get("/admin/speaker/add"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/speaker/edit"))
            .andExpect(model().attribute("speaker", is(new Speaker())))
            .andExpect(model().attributeExists("branches"));
    }

    @Test
    void getEdit_shouldReturnFormWithExistingSpeaker() throws Exception {
        mockMvc.perform(get("/admin/speaker/edit/" + testSpeaker.getId()))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/speaker/edit"))
            .andExpect(model().attribute("speaker", testSpeaker))
            .andExpect(model().attributeExists("branches"));
    }

    @Test
    void postAdd_shouldCreateNewSpeaker() throws Exception {
        MockMultipartFile emptyFile = new MockMultipartFile("file", new byte[0]);

        mockMvc.perform(multipart("/admin/speaker/add")
                .file(emptyFile)
                .param("firstName", "Jane")
                .param("lastName", "Smith")
                .param("email", "jane@example.com")
                .param("headline", "Spring Expert")
                .param("twitter", "@janesmith")
                .param("bio", "An experienced Spring developer"))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/speaker/view"));

        assertThat(speakerRepository.findByEmail("jane@example.com")).isNotNull();
    }

    @Test
    void postAdd_withImage_shouldSaveSpeakerWithImage() throws Exception {
        byte[] imageBytes = new byte[]{1, 2, 3, 4, 5};
        MockMultipartFile imageFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", imageBytes);

        mockMvc.perform(multipart("/admin/speaker/add")
                .file(imageFile)
                .param("firstName", "Bob")
                .param("lastName", "Johnson")
                .param("email", "bob@example.com")
                .param("headline", "Kotlin Expert")
                .param("twitter", "@bobjohnson")
                .param("resizeImage", "false"))
            .andExpect(status().isFound());

        Speaker saved = speakerRepository.findByEmail("bob@example.com");
        assertThat(saved).isNotNull();
        assertThat(saved.getPicture()).isNotNull();
    }

    @Test
    void getRemove_shouldDeleteSpeaker() throws Exception {
        long speakerId = testSpeaker.getId();

        mockMvc.perform(get("/admin/speaker/remove/" + speakerId))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/speaker/view"));

        assertThat(speakerRepository.findById(speakerId)).isEmpty();
    }
}
