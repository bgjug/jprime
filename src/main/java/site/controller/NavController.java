package site.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class NavController {
	
	private static final Logger logger = Logger.getLogger(NavController.class);

	private static final String PAGE_INDEX = "index.jsp";

	@RequestMapping("/nav/{tag}")
	public String index(@PathVariable("tag") final String tagName, Model model) {
		//TODO find all articles with this tag.
		// redirect to blog.html
		return PAGE_INDEX;
	}

}
