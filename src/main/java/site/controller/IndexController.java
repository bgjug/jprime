package site.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	
	private static final Logger logger = Logger.getLogger(IndexController.class);

	private static final String PAGE_INDEX = "index.jsp";

	@RequestMapping("/")
	public String index(Model model) {
		//find all sponsors
//		model.addAttribute("sponsors",findedSponsors);
		//find top speakers
		
		return PAGE_INDEX;
	}

}
