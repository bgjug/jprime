package site.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import site.facade.BranchService;
import site.model.Session;
import site.model.Submission;
import site.repository.SessionRepository;

/**
 * @author Teodor Tunev
 */
@Controller
@RequestMapping("/pwa")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PWAController {

    private final SessionRepository sessionRepository;

    private final BranchService branchService;

    public PWAController(SessionRepository sessionRepository, BranchService branchService) {
        this.sessionRepository = sessionRepository;
        this.branchService = branchService;
    }

    public static class SessionDTO {
        public final Long id;

        public final String hallName;

        public final String title;

        public final String lectorName;

        public final String coLectorName;

        public final String talkDescription;

        public final LocalDateTime startTime;

        public final LocalDateTime endTime;

        SessionDTO(Session session, String hallName) {
            this.hallName = hallName;
            this.id = session.getId();
            this.startTime = session.getStartTime();
            this.endTime = session.getEndTime();

            Submission submission = session.getSubmission();
            if (submission != null) {
                if (submission.getSpeaker() != null) {
                    lectorName = submission.getSpeaker().getFirstName() + " " + submission.getSpeaker().getLastName();
                } else {
                    lectorName = null;
                }

                if (submission.getCoSpeaker() != null) {
                    coLectorName = submission.getCoSpeaker().getFirstName() + " " + submission.getCoSpeaker().getLastName();
                } else {
                    coLectorName = null;
                }

                title = submission.getTitle();
                talkDescription = submission.getDescription();
            } else {
                coLectorName = null;
                lectorName = null;
                title = session.getTitle();
                talkDescription = null;
            }
        }
    }

    @GetMapping(value = "/findSessionsByHall", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<SessionDTO> getSessionByHall(String hallName) {
        List<Session> sessions = sessionRepository.findSessionsForBranchAndHallOrHallIsNull(hallName, branchService.getCurrentBranch().getLabel());
        return sessions.stream().map(session -> new SessionDTO(session, hallName)).toList();
    }

    @GetMapping({"/", "/**"})
    public String getPwaPage() {
        return "pwa";
    }

}


