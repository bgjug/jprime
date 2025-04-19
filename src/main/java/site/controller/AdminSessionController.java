package site.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import site.facade.AdminService;
import site.facade.BranchService;
import site.model.Session;
import site.model.Submission;

import static org.springframework.util.ObjectUtils.isEmpty;

/**
 * @author Ivan St. Ivanov
 */
@Controller
@RequestMapping("/admin/session")
public class AdminSessionController {

    public static final String SESSIONS_VIEW_JSP = "admin/session/view";

    public static final String SESSIONS_EDIT_JSP = "admin/session/edit";

    @Autowired
    private AdminService adminFacade;

    @Autowired
    private BranchService branchService;

    @GetMapping("/view")
    public String viewSessions(Model model) {
        model.addAttribute("sessions", adminFacade.findAllSessions());
        return SESSIONS_VIEW_JSP;
    }

    @GetMapping("/add")
    public String getNewSessionForm(Model model) {
        return getModelAndView(model, new Session());
    }

    @GetMapping("/edit/{itemId}")
    public String getEditVenueHallForm(@PathVariable Long itemId, Model model) {
        return getModelAndView(model, adminFacade.detachSession(adminFacade.findOneSession(itemId)));
    }

    @PostMapping("/add")
    public String addSession(@RequestParam String submission,
        @RequestParam @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm") LocalDateTime startTime,
        @RequestParam @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm") LocalDateTime endTime,
        @RequestParam String title,
        @RequestParam String hall,
        @RequestParam String id) {
        Session session = new Session();
        if (!"".equals(id)) {
            session = adminFacade.findOneSession(Long.parseLong(id));
        }

        if (!isEmpty(title)) {
            session.setTitle(title);
            session.setSubmission(null);
            if (title.equals("TBA")) {
                if (!isEmpty(hall)) {
                    session.setHall(adminFacade.findOneVenueHall(Long.parseLong(hall)));
                }
            } else {
                session.setHall(null);
            }
        } else {
            session.setSubmission(adminFacade.findOneSubmission(Long.parseLong(submission)));
            session.setHall(adminFacade.findOneVenueHall(Long.parseLong(hall)));
            session.setTitle(null);
        }
        session.setStartTime(startTime);
        session.setEndTime(endTime);
        adminFacade.saveSession(session);
        return "redirect:/admin/session/view";
    }

    @Transactional
        @GetMapping("/remove/{itemId}")
    public String remove(@PathVariable Long itemId) {
        adminFacade.deleteSession(itemId);
        return "redirect:/admin/session/view";
    }

    private String getModelAndView(Model model, Session session) {
        model.addAttribute("session", session);

        // Reduce the list of submissions to only with those that are not scheduled yet
        List<Submission> confirmedSubmissions =
            adminFacade.findAllConfirmedSubmissionsForBranch(branchService.getCurrentBranch());
        List<Submission> scheduledSubmissions =
            adminFacade.findAllSessions().stream().map(Session::getSubmission).toList();
        confirmedSubmissions = confirmedSubmissions.stream()
                                                 .filter(
                                                     submission -> !scheduledSubmissions.contains(submission))
                                                 .collect(Collectors.toList());
        if (session.getSubmission() != null) {
            // For edit - we need to have current submission to
            confirmedSubmissions.add(session.getSubmission());
        } else {
            if (!StringUtils.isEmpty(session.getTitle())) {
                Submission submission =
                    new Submission(session.getTitle(), null, null, null, null, null, false);
                switch (session.getTitle()) {
                    case "Break" : submission.setId(-1L); break;
                    case "TBA" : submission.setId(-2L); break;
                    default: submission = null;
                }
                session.setSubmission(submission);
            }
        }

        model.addAttribute("submissions", confirmedSubmissions);
        model.addAttribute("halls", adminFacade.findAllVenueHalls());

        return SESSIONS_EDIT_JSP;
    }
}
