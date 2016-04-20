package site.controller;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import site.config.Globals;
import site.facade.AdminService;
import site.model.Session;

import javax.transaction.Transactional;

/**
 * @author Ivan St. Ivanov
 */
@Controller
@RequestMapping("/admin/session")
public class AdminSessionController {

    public static final String SESSIONS_VIEW_JSP = "/admin/session/view.jsp";
    public static final String SESSIONS_EDIT_JSP = "/admin/session/edit.jsp";

    @Autowired
    @Qualifier(AdminService.NAME)
    private AdminService adminFacade;

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String viewSessions(Model model) {
        model.addAttribute("sessions", adminFacade.findAllSessions());
        return SESSIONS_VIEW_JSP;
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String getNewSessionForm(Model model) {
        return getModelAndView(model, new Session());
    }

    @RequestMapping(value = "/edit/{itemId}", method = RequestMethod.GET)
    public String getEditVenueHallForm(@PathVariable("itemId") Long itemId, Model model) {
        return getModelAndView(model, adminFacade.findOneSession(itemId));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addSession(@RequestParam String submission,
                             @RequestParam @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm") DateTime startTime,
                             @RequestParam @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm") DateTime endTime,
                             @RequestParam String title,
                             @RequestParam String hall,
                             @RequestParam String id) {
        Session session = new Session();
        if (!"".equals(id)) {
            session = adminFacade.findOneSession(Long.parseLong(id));
        }
        if (title != null && !title.equals("")) {
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
    @RequestMapping(value = "/remove/{itemId}", method = RequestMethod.GET)
    public String remove(@PathVariable("itemId") Long itemId, Model model) {
        adminFacade.deleteSession(itemId);
        return "redirect:/admin/session/view";
    }

    private String getModelAndView(Model model, Session session) {
        model.addAttribute("session", session);
        model.addAttribute("submissions", adminFacade.findAllAcceptedSubmissionsForBranch(Globals.CURRENT_BRANCH));
        model.addAttribute("halls", adminFacade.findAllVenueHalls());
        return SESSIONS_EDIT_JSP;
    }
}
