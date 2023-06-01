package site.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import site.config.Globals;
import site.facade.UserService;
import site.model.Article;

@Controller
public class NavController {
	
	private static final Logger logger = LogManager.getLogger(NavController.class);

	private final UserService userFacade;

    public NavController(@Qualifier(UserService.NAME) UserService userFacade) {
        this.userFacade = userFacade;
    }

    @GetMapping("/nav/{tag}")
	public String getByTag(@PathVariable("tag") final String tagName, @PageableDefault(size = 24)
			Pageable pageable, Model model) {
		model.addAttribute("tags", userFacade.findAllTags());

		Page<Article> articles= userFacade.findArticlesByTag(tagName, pageable);

        model.addAttribute("jprime_year", Globals.CURRENT_BRANCH.getStartDate().getYear());

        if (articles.getTotalElements() == 0) {
            logger.error("Invalid tag name ({}})", tagName);
            return "/404.jsp";
        }

		if(articles.getTotalElements() > 1) {
			model.addAttribute("articles", articles);
			return "/blog.jsp";
		} else { //just 1 for example Agenda will be such an article 1 article in a tag.
            // no need for paging and so on
			model.addAttribute("article", articles.getContent().iterator().next());
			return "/single-post.jsp";
		}
	}

    @GetMapping("/nav")
    public String index(Pageable pageable, Model model) {
        Page<Article> articles= userFacade.allPublishedArticles(pageable);
        model.addAttribute("articles", articles);
        model.addAttribute("tags", userFacade.findAllTags());
        model.addAttribute("jprime_year", Globals.CURRENT_BRANCH.getStartDate().getYear());
        // redirect to nav
        return "/blog.jsp";
    }

    //read a single blog
    @GetMapping("/nav/article/{id}")
    public String getById(@PathVariable final long id, Model model) {
        model.addAttribute("jprime_year", Globals.CURRENT_BRANCH.getStartDate().getYear());
        Article article= userFacade.getArticleById(id);

        if (article == null) {
            logger.error(String.format("Invalid tag id (%1$d)", id));
            return "/404.jsp";
        }

        //security
        if (article.isPublished()) {
            model.addAttribute("article", article);
        }
        model.addAttribute("tags", userFacade.findAllTags());
        return "/single-post.jsp";
    }
    
    @GetMapping("/nav/article")
	public String getById(@RequestParam final String title,
			Model model) {
        Article article= userFacade.getArticleByTitle(title);
        model.addAttribute("jprime_year", Globals.CURRENT_BRANCH.getStartDate().getYear());

        if (article == null) {
            logger.error(String.format("Invalid tag title (%1$s)", title));
            return "/404.jsp";
        }

        //security
        if (article.isPublished()) {
            model.addAttribute("article", article);
        }
        model.addAttribute("tags", userFacade.findAllTags());
        return "/single-post.jsp";
    }

    //read a single blog
    @GetMapping("/team")
    public String showTeam(Model model) {
        model.addAttribute("tags", userFacade.findAllTags());
        return "/team.jsp";
    }

    //read a single blog
    @GetMapping("/venue")
    public String showVenue(Model model) {
        model.addAttribute("tags", userFacade.findAllTags());
        DateTime startDate = Globals.CURRENT_BRANCH.getStartDate();
        model.addAttribute("conference_dates", String.format("%d-%s", startDate.getDayOfMonth(),
            DateUtils.dateToStringWithMonthAndYear(startDate.plusDays(1))));

        return "/venue.jsp";
    }

}
