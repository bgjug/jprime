package site.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminController {
	
	private static final Logger logger = LogManager.getLogger(AdminController.class);

	private static final String PAGE_ADMIN_INDEX = "/admin/index.jsp";

	@RequestMapping("/admin")
	public String index(Model model) {
		return PAGE_ADMIN_INDEX;
	}

}
