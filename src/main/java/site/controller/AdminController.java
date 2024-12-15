package site.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    private static final String PAGE_ADMIN_INDEX = "admin/index";

    @GetMapping({"/admin", "/admin/"})
	public String index(Model model) {
        return PAGE_ADMIN_INDEX;
    }

}
