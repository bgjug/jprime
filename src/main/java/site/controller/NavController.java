package site.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import site.facade.UserService;
import site.model.Article;

@Controller
public class NavController {
	
	private static final Logger logger = LogManager.getLogger(NavController.class);

	@Autowired
	@Qualifier(UserService.NAME)
	private UserService userFacade;
	
	@RequestMapping("/nav/{tag}")
	public String getByTag(@PathVariable("tag") final String tagName, @PageableDefault(size = 24)
			Pageable pageable, Model model) {
		model.addAttribute("tags", userFacade.findAllTags());

		Page<Article> articles= userFacade.findArticlesByTag(tagName, pageable);
		if(articles.getTotalElements() > 1) {
			model.addAttribute("articles", articles);
			return "/blog.jsp";
		} else { //just 1 for example Agenda will be such an article 1 article in a tag.. no need for paging and so on
			model.addAttribute("article", articles.getContent().iterator().next());
			return "/single-post.jsp";
		}
	}

    @RequestMapping("/nav")
    public String index(Pageable pageable, Model model) {
        Page<Article> articles= userFacade.allPublishedArticles(pageable);
        model.addAttribute("articles", articles);
        model.addAttribute("tags", userFacade.findAllTags());
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
        model.addAttribute("tags", userFacade.findAllTags());
        return "/single-post.jsp";
    }
    
    @RequestMapping("/nav/article")
	public String getById(@RequestParam(required = true) final String title,
			Model model) {
        Article article= userFacade.getArticleByTitle(title);
        //security
        if (article.isPublished()) {
            model.addAttribute("article", article);
        }
        model.addAttribute("tags", userFacade.findAllTags());
        return "/single-post.jsp";
    }

    //read a single blog
    @RequestMapping("/team")
    public String showTeam(Model model) {
        model.addAttribute("tags", userFacade.findAllTags());
        return "/team.jsp";
    }

    //read a single blog
    @RequestMapping("/venue")
    public String showVenue(Model model) {
        model.addAttribute("tags", userFacade.findAllTags());
        return "/venue.jsp";
    }

}
