package site.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import site.config.Globals;
import site.facade.AdminService;
import site.model.Article;
import site.model.User;

import javax.validation.Valid;
import java.util.List;


@Controller()
@RequestMapping(value = "/admin/article")
public class AdminArticleController {

    @Autowired
    @Qualifier(AdminService.NAME)
    private AdminService adminFacade;

    @GetMapping("/view")
    public String view(Model model) {
        List<Article> articles = adminFacade.findAllArticles();
        model.addAttribute("articles", articles);
        return "/admin/article/view.jsp";
    }

    @GetMapping("/view/{id}")
    public String getById(@PathVariable final long id, Model model) {
        Article article = adminFacade.findOneArticle(id);
        model.addAttribute("article", article);
        model.addAttribute("jprime_year", Globals.CURRENT_BRANCH.getStartDate().getYear());

        return "/single-post.jsp";
    }

    @PostMapping("/add")
    public String add(@Valid final Article article, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/admin/article/edit.jsp";
        }
        User admin = this.adminFacade.findUserByEmail("admin@jsprime.io");
        if (admin == null) {
            admin = new User();
            admin.setEmail("admin@jsprime.io");
            admin.setFirstName("Admin");
            admin.setLastName("");
            this.adminFacade.saveUser(admin);
            //refresh
            admin = this.adminFacade.findUserByEmail("admin@jsprime.io");
        }
        article.setAuthor(admin);
        this.adminFacade.saveArticle(article);

        return "redirect:/admin/article/view";
    }

    @GetMapping("/add")
    public String edit(Model model) {
        //user info TODO: hardcoded, fixed this!
        model.addAttribute("article", new Article());
        model.addAttribute("tags", this.adminFacade.findAllTags());

        return "/admin/article/edit.jsp";
    }

    @GetMapping("/edit/{itemId}")
    public String edit(@PathVariable Long itemId, Model model) {
        Article article = adminFacade.findOneArticle(itemId);
        model.addAttribute("article", article);
        model.addAttribute("tags", this.adminFacade.findAllTags());
        return "/admin/article/edit.jsp";
    }

    @GetMapping("/remove/{itemId}")
    public String remove(@PathVariable Long itemId, Model model) {
        adminFacade.deleteArticle(itemId);
        return "redirect:/admin/article/view";
    }

}
