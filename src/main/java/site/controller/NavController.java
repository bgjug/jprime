package site.controller;

import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import site.facade.BranchService;
import site.facade.UserService;
import site.model.Article;

@Controller
public class NavController {
	
	private static final Logger logger = LogManager.getLogger(NavController.class);

	private final UserService userFacade;

    private final BranchService branchService;

    public NavController(UserService userFacade, BranchService branchService) {
        this.userFacade = userFacade;
        this.branchService = branchService;
    }

    @GetMapping("/nav/{tag}")
	public String getByTag(@PathVariable("tag") final String tagName, @PageableDefault(size = 24)
			Pageable pageable, Model model) {
		model.addAttribute("tags", userFacade.findAllTags());

		Page<Article> articles= userFacade.findArticlesByTag(tagName, pageable);

        model.addAttribute("jprime_year", branchService.getCurrentBranch().getStartDate().getYear());

        if (articles.getTotalElements() == 0) {
            logger.error("Invalid tag name ({}})", tagName);
            return "404";
        }

		if(articles.getTotalElements() > 1) {
			model.addAttribute("articles", articles);
			return "blog";
		} else { //just 1 for example Agenda will be such an article 1 article in a tag.
            // no need for paging and so on
			model.addAttribute("article", articles.getContent().iterator().next());
			return "single-post";
		}
	}

    @GetMapping("/nav")
    public String index(Pageable pageable, Model model) {
        Page<Article> articles= userFacade.allPublishedArticles(pageable);
        model.addAttribute("articles", articles);
        model.addAttribute("tags", userFacade.findAllTags());
        model.addAttribute("jprime_year", branchService.getCurrentBranch().getStartDate().getYear());
        // redirect to nav
        return "blog";
    }

    //read a single blog
    @GetMapping("/nav/article/{id}")
    public String getById(@PathVariable final long id, Model model) {
        model.addAttribute("jprime_year", branchService.getCurrentBranch().getStartDate().getYear());
        Article article= userFacade.getArticleById(id);

        if (article == null) {
            logger.error("Invalid tag id ({})", id);
            return "404";
        }

        //security
        if (article.isPublished()) {
            model.addAttribute("article", article);
        }
        model.addAttribute("tags", userFacade.findAllTags());
        return "single-post";
    }
    
    @GetMapping("/nav/article")
	public String getById(@RequestParam final String title,
			Model model) {
        Article article= userFacade.getArticleByTitle(title);
        model.addAttribute("jprime_year", branchService.getCurrentBranch().getStartDate().getYear());

        if (article == null) {
            logger.error("Invalid tag title ({})", title);
            return "404";
        }

        //security
        if (article.isPublished()) {
            model.addAttribute("article", article);
        }
        model.addAttribute("tags", userFacade.findAllTags());
        return "single-post";
    }

    //read a single blog
    @GetMapping("/team")
    public String showTeam(Model model) {
        model.addAttribute("tags", userFacade.findAllTags());
        return "team";
    }

    //read a single blog
    @GetMapping("/venue")
    public String showVenue(Model model) {
        model.addAttribute("tags", userFacade.findAllTags());
        LocalDateTime startDate = branchService.getCurrentBranch().getStartDate();
        model.addAttribute("conference_dates", String.format("%d-%s", startDate.getDayOfMonth(),
            DateUtils.dateToStringWithMonthAndYear(startDate.plusDays(1))));

        return "venue";
    }

}
