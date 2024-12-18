package site.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import site.app.Application;
import site.config.Globals;
import site.model.Session;
import site.model.Speaker;
import site.model.Submission;
import site.repository.SessionRepository;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest(classes = Application.class) // Load the full application context using your main application class
@AutoConfigureMockMvc
public class PwaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SessionRepository sessionRepository;

    @Test
    public void testGetSessionByHall() throws Exception {
        // Mock data
        String hallName = "Main Hall";
        Session mockSession = new Session();
        mockSession.setId(1L);
        mockSession.setStartTime(LocalDateTime.of(2023, 10, 20, 10, 0));
        mockSession.setEndTime(LocalDateTime.of(2023, 10, 20, 12, 0));

        Submission submission = new Submission();
        Speaker speaker = new Speaker();
        speaker.setFirstName("John");
        speaker.setLastName("Doe");
        submission.setSpeaker(speaker);
        submission.setTitle("Mock Session");
        submission.setDescription("A description of the session");
        mockSession.setSubmission(submission);

        when(sessionRepository.findSessionsForBranchAndHallOrHallIsNull(hallName, Globals.CURRENT_BRANCH.name()))
            .thenReturn(List.of(mockSession));

        // Perform GET request
        mockMvc.perform(get("/pwa/findSessionsByHall").param("hallName", hallName))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[0].hallName").value(hallName))
            .andExpect(jsonPath("$[0].lectorName").value("John Doe"))
            .andExpect(jsonPath("$[0].title").value("Mock Session"))
            .andExpect(jsonPath("$[0].talkDescription").value("A description of the session"));
    }

    @Test
    public void testGetPwaPage() throws Exception {
        // Perform GET request for the PWA page
        mockMvc.perform(get("/pwa/"))
            .andExpect(status().isOk())
            .andExpect(view().name("pwa"));
    }
}