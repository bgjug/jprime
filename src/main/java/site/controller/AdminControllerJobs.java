package site.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import site.facade.AdminService;

@Controller()
@RequestMapping(value = "/admin/jobs")
public class AdminControllerJobs {

    private static final String JOBS_VIEW_JSP = "admin/jobs/view";

    @Autowired
    private AdminService adminFacade;

    @GetMapping("/view")
    public String viewRegistrants(Model model) {
        model.addAttribute("jobs", adminFacade.findBackgroundJobs());
        return JOBS_VIEW_JSP;
    }
}
