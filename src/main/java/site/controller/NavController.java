package site.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import site.facade.UserFacade;
import site.model.Article;

import java.awt.*;

@Controller
public class NavController {
	
	private static final Logger logger = Logger.getLogger(NavController.class);

	@Autowired
	@Qualifier(UserFacade.NAME)
	private UserFacade userFacade;
	
	@RequestMapping("/nav/{tag}")
	public String getByTag(@PathVariable("tag") final String tagName,
			Pageable pageable, Model model) {
		Page<Article> articles= userFacade.findArticlesByTag(tagName, pageable);
		model.addAttribute("articles", articles);
		// redirect to nav
		return "/blog.jsp";
	}

    @RequestMapping("/nav")
    public String index(Pageable pageable, Model model) {
        Page<Article> articles= userFacade.allPublishedArticles(pageable);
        model.addAttribute("articles", articles);
        // redirect to nav
        return "/blog.jsp";
    }

    //read a single blog
    @RequestMapping("/nav/article/{id}")
    public String getById(@PathVariable("id") final long id, Model model) {
        Article article= userFacade.getArticleById(id);
        //security
        if (article.isPublished()) {
            model.addAttribute("article", article);
        }
        return "/single-post.jsp";
    }

    //read a single blog
    @RequestMapping("/team")
    public String showTeam(Model model) {
        return "/team.jsp";
    }


}
