package site.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import site.config.Globals;
import site.facade.AdminService;
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
    @Qualifier(AdminService.NAME)
    private AdminService adminFacade;

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
        return getModelAndView(model, adminFacade.findOneSession(itemId));
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
            session.setHall(null);
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
    public String remove(@PathVariable Long itemId, Model model) {
        adminFacade.deleteSession(itemId);
        return "redirect:/admin/session/view";
    }

    private String getModelAndView(Model model, Session session) {
        model.addAttribute("session", session);

        // Reduce list of submissions to only with those that are not scheduled yet
        List<Submission> acceptedSubmissions =
            adminFacade.findAllAcceptedSubmissionsForBranch(Globals.CURRENT_BRANCH);
        List<Submission> scheduledSubmissions =
            adminFacade.findAllSessions().stream().map(Session::getSubmission).toList();
        acceptedSubmissions = acceptedSubmissions.stream()
                                                 .filter(
                                                     submission -> !scheduledSubmissions.contains(submission))
                                                 .collect(Collectors.toList());
        if (session.getSubmission() != null) {
            // For edit - we need to have current submission to
            acceptedSubmissions.add(session.getSubmission());
        }

        model.addAttribute("submissions", acceptedSubmissions);
        model.addAttribute("halls", adminFacade.findAllVenueHalls());

        return SESSIONS_EDIT_JSP;
    }
}
