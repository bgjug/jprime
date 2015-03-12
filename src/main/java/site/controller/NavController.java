package site.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import site.facade.UserFacade;

@Controller
public class NavController {
	
	private static final Logger logger = Logger.getLogger(NavController.class);

	private static final String PAGE_INDEX = "index.jsp";

	@Autowired
	@Qualifier(UserFacade.NAME)
	private UserFacade userFacade;
	
	@RequestMapping("/nav/{tag}")
	public String index(@PathVariable("tag") final String tagName, Pageable pageable, Model model) {
		//TODO find all articles with this tag.
		userFacade.findArticlesByTag(tagName, pageable);
		// redirect to blog.html
		return PAGE_INDEX;
	}

}
