package site.controller;

import org.joda.time.DateTime;
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
import site.model.*;
import site.repository.SessionRepository;
import site.repository.SubmissionRepository;
import site.repository.VenueHallRepository;

import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static site.controller.AdminSessionController.SESSIONS_EDIT_JSP;
import static site.controller.AdminSessionController.SESSIONS_VIEW_JSP;

/**
 * @author Ivan St. Ivanov
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@Transactional
public class SessionControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    @Qualifier(SessionRepository.NAME)
    private SessionRepository sessionRepository;

    @Autowired
    @Qualifier(SubmissionRepository.NAME)
    private SubmissionRepository submissionRepository;

    @Autowired
    @Qualifier(VenueHallRepository.NAME)
    private VenueHallRepository venueHallRepository;

    private MockMvc mockMvc;

    private Submission forgeSubmission;
    private VenueHall betaHall;
    private Session bootSession;

    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        this.forgeSubmission = submissionRepository.save(new Submission("Forge with me", "Forge is the best", SessionLevel.BEGINNER, new Speaker("Ivan St.", "Ivanov", "ivan.st.ivanov@example.com", "The Forge Guy", "@forge", false)));
        forgeSubmission.setStatus(SubmissionStatus.ACCEPTED);
        Submission bootSubmission = submissionRepository.save(new Submission("Spring Boot", "Bootiful or what?", SessionLevel.BEGINNER, new Speaker("Nayden", "Gochev", "nayden.gochev@example.com", "The Spring Guy", "@sprink", true)));
        bootSubmission.setStatus(SubmissionStatus.ACCEPTED);
        this.betaHall = venueHallRepository.save(new VenueHall("Beta", "600 seats"));
        venueHallRepository.save(new VenueHall("Alpha", "400 seats"));
        this.bootSession = sessionRepository.save(new Session(bootSubmission, DateTime.now(), DateTime.now(), betaHall));
    }

    @Test
    public void getViewSessionsShouldReturnAllSessions() throws Exception {
        mockMvc.perform(get("/admin/session/view"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("sessions", hasSize(1)))
                .andExpect(view().name(SESSIONS_VIEW_JSP));
    }

    @Test
    public void getNewSessionShouldReturnFormWithEmptySession() throws Exception {
        mockMvc.perform(get("/admin/session/add"))
                .andExpect(model().attribute("halls", hasSize(2)))
                .andExpect(model().attribute("submissions", hasSize(2)))
                .andExpect(model().attribute("session", is(new Session())))
                .andExpect(status().isOk())
                .andExpect(view().name(SESSIONS_EDIT_JSP));
    }

    @Test
    public void getEditVenueHallShouldReturnFormWithHall() throws Exception {
        mockMvc.perform(get("/admin/session/edit/" + bootSession.getId()))
                .andExpect(model().attribute("session", is(bootSession)))
                .andExpect(status().isOk())
                .andExpect(view().name(SESSIONS_EDIT_JSP));
    }

    @Test
    public void shouldAddNewSession() throws Exception {
        mockMvc.perform(post("/admin/session/add")
                .param("submission", forgeSubmission.getId() + "")
                .param("startTime", "26.05.16 10:15")
                .param("endTime", "26.05.16 11:15")
                .param("hall", betaHall.getId() + "")
                .param("id", ""))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/admin/session/view"));
        List<Session> sessions = sessionRepository.findAll();
        assertThat(sessions.size(), is(2));

        Session session = sessions.get(1);
        assertThat(session.getSubmission().getTitle(), is(forgeSubmission.getTitle()));
        assertThat(session.getStartTime(), is(new DateTime().withMonthOfYear(5).withDayOfMonth(26).withHourOfDay(10).withMinuteOfHour(15).withSecondOfMinute(0).withMillisOfSecond(0)));
        assertThat(session.getEndTime(), is(new DateTime().withMonthOfYear(5).withDayOfMonth(26).withHourOfDay(11).withMinuteOfHour(15).withSecondOfMinute(0).withMillisOfSecond(0)));
        assertThat(session.getHall().getName(), is(betaHall.getName()));
    }

    @Test
    public void shouldEditSession() throws Exception {
        Submission bootSubmission = bootSession.getSubmission();
        mockMvc.perform(post("/admin/session/add")
                .param("submission", bootSubmission.getId() + "")
                .param("startTime", "27.05.16 10:15")
                .param("endTime", "27.05.16 11:15")
                .param("hall", betaHall.getId() + "")
                .param("id", bootSession.getId() + ""))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/admin/session/view"));
        List<Session> sessions = sessionRepository.findAll();
        assertThat(sessions.size(), is(1));

        Session session = sessions.get(0);
        assertThat(session.getSubmission().getTitle(), is(bootSubmission.getTitle()));
        assertThat(session.getStartTime(), is(new DateTime().withMonthOfYear(5).withDayOfMonth(27).withHourOfDay(10).withMinuteOfHour(15).withSecondOfMinute(0).withMillisOfSecond(0)));
        assertThat(session.getEndTime(), is(new DateTime().withMonthOfYear(5).withDayOfMonth(27).withHourOfDay(11).withMinuteOfHour(15).withSecondOfMinute(0).withMillisOfSecond(0)));
        assertThat(session.getHall().getName(), is(betaHall.getName()));
    }

    @Test
    public void getDeleteShouldRemoveSession() throws Exception {
        mockMvc.perform(get("/admin/session/remove/" + bootSession.getId()));
        List<Session> sessions = sessionRepository.findAll();
        assertThat(sessions.size(), is(0));
    }
}
