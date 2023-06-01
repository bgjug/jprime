package site.controller;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import site.config.Globals;
import site.model.Session;
import site.model.Submission;
import site.repository.SessionRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Teodor Tunev
 */
@Controller
public class PWAController {

    @Autowired
    @Qualifier(SessionRepository.NAME)
    private SessionRepository sessionRepository;

    private static class SessionDTO {
        public Long id;

        public String hallName;

        public String title;

        public String lectorName;

        public String coLectorName;

        public String talkDescription;

        public DateTime startTime;

        public DateTime endTime;

        SessionDTO(Session session, String hallName) {
            this.hallName = hallName;
            this.id = session.getId();
            this.startTime = session.getStartTime();
            this.endTime = session.getEndTime();
            this.title = session.getTitle();

            Submission submission = session.getSubmission();
            if (submission != null) {
                if (submission.getSpeaker() != null) {
                    this.lectorName = submission.getSpeaker().getFirstName() + " " + submission.getSpeaker().getLastName();
                }

                if (submission.getCoSpeaker() != null) {
                    this.coLectorName = submission.getCoSpeaker().getFirstName() + " " + submission.getCoSpeaker().getLastName();
                }

                this.title = submission.getTitle();
                this.talkDescription = submission.getDescription();
            }
        }
    }

    @ResponseBody
        @GetMapping("/pwa/findSessionsByHall")
    public List<SessionDTO> getSessionByHall(String hallName) {
        List<Session> sessions = sessionRepository.findSessionsForBranchAndHallOrHallIsNull(hallName, Globals.CURRENT_BRANCH.name());
        return sessions.stream().map(session -> new SessionDTO(session, hallName)).collect(Collectors.toList());
    }

    @GetMapping({"/pwa", "/pwa/**"})
    public String getPwaPage() {
        return "/pwa.jsp";
    }

}


