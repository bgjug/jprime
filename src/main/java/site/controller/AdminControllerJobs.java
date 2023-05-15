package site.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import site.facade.AdminService;

@Controller()
@RequestMapping(value = "/admin/jobs")
public class AdminControllerJobs {

    private static final String JOBS_VIEW_JSP = "/admin/jobs/view.jsp";

    @Autowired
    @Qualifier(AdminService.NAME)
    private AdminService adminFacade;

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String viewRegistrants(Model model) {
        model.addAttribute("jobs", adminFacade.findBackgroundJobs());
        return JOBS_VIEW_JSP;
    }
}
