package site.controller;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.List;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.context.WebApplicationContext;

import site.app.Application;
import site.facade.BranchService;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static site.controller.AdminSessionController.SESSIONS_EDIT_JSP;
import static site.controller.AdminSessionController.SESSIONS_VIEW_JSP;

/**
 * @author Ivan St. Ivanov
 */
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Transactional
class SessionControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private SessionRepository sessionRepository;

    private MockMvc mockMvc;

    private static Submission forgeSubmission;

    private static VenueHall betaHall;

    private static Session bootSession;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private BranchService branchService;

    @Autowired
    private SpeakerRepository speakerRepository;

    @Autowired
    private VenueHallRepository venueHallRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        venueHallRepository.deleteAll();
        sessionRepository.deleteAll();
        submissionRepository.deleteAll();
        speakerRepository.deleteAll();

        Speaker ivan =
            new Speaker("Ivan St.", "Ivanov", "ivan.st.ivanov@example.com", "The Forge Guy", "@forge");
        forgeSubmission = submissionRepository.save(
            new Submission("Forge with me", "Forge is the best", SessionLevel.BEGINNER,
                SessionType.CONFERENCE_SESSION, ivan, SubmissionStatus.ACCEPTED, true).branch(
                branchService.getCurrentBranch()));
        ivan.getSubmissions().add(forgeSubmission);
        speakerRepository.save(ivan);

        Speaker nayden =
            new Speaker("Nayden", "Gochev", "nayden.gochev@example.com", "The Spring Guy", "@sprink");
        Submission bootSubmission = submissionRepository.save(
            new Submission("Spring Boot", "Bootiful or what?", SessionLevel.BEGINNER,
                SessionType.CONFERENCE_SESSION, nayden, SubmissionStatus.ACCEPTED, true).branch(
                branchService.getCurrentBranch()));
        nayden.getSubmissions().add(bootSubmission);
        speakerRepository.save(nayden);

        betaHall = venueHallRepository.save(new VenueHall("Beta", "600 seats"));
        Session session =
            new Session(submissionRepository.getReferenceById(bootSubmission.getId()), LocalDateTime.now(),
                LocalDateTime.now(), venueHallRepository.getReferenceById(betaHall.getId()));
        session = sessionRepository.save(session);
        entityManager.refresh(session);
        sessionRepository.findById(session.getId()).ifPresent(s -> bootSession = s);
        venueHallRepository.save(new VenueHall("Alpha", "400 seats"));
    }

    @Test
    void getViewSessionsShouldReturnAllSessions() throws Exception {
        mockMvc.perform(get("/admin/session/view"))
            .andExpect(status().isOk())
            .andExpect(model().attribute("sessions", hasSize(1)))
            .andExpect(view().name(SESSIONS_VIEW_JSP));
    }

    @Test
    void getNewSessionShouldReturnFormWithEmptySession() throws Exception {
        mockMvc.perform(get("/admin/session/add"))
            .andExpect(model().attribute("halls", hasSize(2)))
            .andExpect(model().attribute("submissions", hasSize(1)))
            .andExpect(model().attribute("session", is(new Session())))
            .andExpect(status().isOk())
            .andExpect(view().name(SESSIONS_EDIT_JSP));
    }

    @Test
    void getEditVenueHallShouldReturnFormWithHall() throws Exception {
        mockMvc.perform(get("/admin/session/edit/" + bootSession.getId()))
            .andExpect(model().attribute("session", is(bootSession)))
            .andExpect(status().isOk())
            .andExpect(view().name(SESSIONS_EDIT_JSP));
    }

    @Test
    void shouldAddNewSession() throws Exception {
        mockMvc.perform(post("/admin/session/add").param("submission", forgeSubmission.getId() + "")
                .param("startTime", "26.05.2017 10:15")
                .param("endTime", "26.05.2017 11:15")
                .param("title", "")
                .param("hall", betaHall.getId() + "")
                .param("id", ""))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/session/view"));
        List<Session> sessions = sessionRepository.findAll();
        assertThat(sessions.size(), is(2));

        Session session = sessions.get(1);
        assertThat(session.getSubmission().getTitle(), is(forgeSubmission.getTitle()));
        assertThat(session.getStartTime(), is(LocalDateTime.of(2017, Month.MAY, 26, 10, 15, 0, 0)));
        assertThat(session.getEndTime(), is(LocalDateTime.of(2017, Month.MAY, 26, 11, 15, 0, 0)));
        assertThat(session.getHall().getName(), is(betaHall.getName()));
        assertEquals(session.getTitle(), session.getSubmission().getTitle());
    }

    @Test
    void shouldAddCoffeeBreak() throws Exception {
        mockMvc.perform(post("/admin/session/add")
                .param("submission", "")
                .param("startTime", "26.05.2017 10:15")
                .param("endTime", "26.05.2017 11:15")
                .param("title", "Coffee break")
                .param("hall", "")
                .param("id", ""))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/session/view"));
        List<Session> sessions = sessionRepository.findAll();
        assertThat(sessions.size(), is(2));

        Session session = sessions.get(1);
        assertNull(session.getSubmission());
        assertThat(session.getTitle(), is("Coffee break"));
        assertThat(session.getStartTime(), is(LocalDateTime.of(2017, Month.MAY, 26, 10, 15, 0, 0)));
        assertThat(session.getEndTime(), is(LocalDateTime.of(2017, Month.MAY, 26, 11, 15, 0, 0)));
        assertNull(session.getHall());
    }

    @Test
    void shouldEditSession() throws Exception {
        Submission bootSubmission = bootSession.getSubmission();
        mockMvc.perform(post("/admin/session/add")
                .param("submission", bootSubmission.getId() + "")
                .param("startTime", "27.05.2017 10:15")
                .param("endTime", "27.05.2017 11:15")
                .param("title", "")
                .param("hall", betaHall.getId() + "")
                .param("id", bootSession.getId() + ""))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/session/view"));
            List<Session> sessions = sessionRepository.findAll();
            assertThat(sessions.size(), is(1));

            Session session = sessions.get(0);
            assertThat(session.getSubmission().getTitle(), is(bootSubmission.getTitle()));
            assertThat(session.getStartTime(), is(LocalDateTime.of(2017, Month.MAY, 27, 10, 15, 0, 0)));
            assertThat(session.getEndTime(), is(LocalDateTime.of(2017, Month.MAY, 27, 11, 15, 0, 0)));
            assertThat(session.getHall().getName(), is(betaHall.getName()));
    }

    @Test
    void shouldChangeSessionType() throws Exception {
        mockMvc.perform(post("/admin/session/add").param("submission", "")
                .param("startTime", "27.05.2017 10:15")
                .param("endTime", "27.05.2017 11:15")
                .param("title", "Opening")
                .param("hall", "")
                .param("id", bootSession.getId() + ""))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/session/view"));
        List<Session> sessions = sessionRepository.findAll();
        assertThat(sessions.size(), is(1));

        Session session = sessions.get(0);
        assertNull(session.getSubmission());
        assertThat(session.getStartTime(), is(LocalDateTime.of(2017, Month.MAY, 27, 10, 15, 0, 0)));
        assertThat(session.getEndTime(), is(LocalDateTime.of(2017, Month.MAY, 27, 11, 15, 0, 0)));
        assertNull(session.getHall());
        assertThat(session.getTitle(), is("Opening"));
    }

    @Test
    void getDeleteShouldRemoveSession() throws Exception {
        mockMvc.perform(get("/admin/session/remove/" + bootSession.getId()));
        List<Session> sessions = sessionRepository.findAll();
        assertThat(sessions.size(), is(0));
    }
}
