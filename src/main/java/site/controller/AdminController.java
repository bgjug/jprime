package site.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class AdminController {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

	private static final String PAGE_ADMIN_INDEX = "/admin/index.jsp";

	@GetMapping("/admin")
	public String index(Model model) {
		return PAGE_ADMIN_INDEX;
	}

}
