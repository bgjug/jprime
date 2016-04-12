package site.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import site.facade.AdminService;
import site.model.Article;
import site.model.Role;
import site.model.User;

import javax.validation.Valid;
import java.util.List;


@Controller()
@RequestMapping(value = "/admin/article")
public class AdminArticleController {

    @Autowired
    @Qualifier(AdminService.NAME)
    private AdminService adminFacade;

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String view(Model model) {
        List<Article> articles = adminFacade.findAllArticles();
        model.addAttribute("articles", articles);
        return "/admin/article/view.jsp";
    }

    @RequestMapping("/view/{id}")
    public String getById(@PathVariable("id") final long id, Model model) {
        Article article = adminFacade.findOneArticle(id);
        model.addAttribute("article", article);
        return "/single-post.jsp";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(@Valid final Article article, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/admin/article/edit.jsp";
        }
        User adminUser = this.adminFacade.findUserByEmail(User.ADMIN_EMAIL);
        Role adminRole = this.adminFacade.findRoleByNameOrCreate(Role.ADMIN_NAME); //@Trifon
        if (adminUser == null) {
            adminUser = this.adminFacade.createRegularUser(User.ADMIN_EMAIL);
            adminUser.setFirstName("Admin");
            adminUser.setLastName("");
            adminUser.addRole( adminRole ); //@Trifon
            adminUser = this.adminFacade.saveUser(adminUser);
            //refresh
            adminUser = this.adminFacade.findUserByEmail(User.ADMIN_EMAIL); // TODO - Maybe this refresh is not needed as admin is already updated by saveUser()?
        }
        article.setAuthor(adminUser);
        this.adminFacade.saveArticle(article);

        return "redirect:/admin/article/view";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String edit(Model model) {
        //user info TODO: hardcoded, fixed this!
        model.addAttribute("article", new Article());
        model.addAttribute("tags", this.adminFacade.findAllTags());

        return "/admin/article/edit.jsp";
    }

    @RequestMapping(value = "/edit/{itemId}", method = RequestMethod.GET)
    public String edit(@PathVariable("itemId") Long itemId, Model model) {
        Article article = adminFacade.findOneArticle(itemId);
        model.addAttribute("article", article);
        model.addAttribute("tags", this.adminFacade.findAllTags());
        return "/admin/article/edit.jsp";
    }

    @RequestMapping(value = "/remove/{itemId}", method = RequestMethod.GET)
    public String remove(@PathVariable("itemId") Long itemId, Model model) {
        adminFacade.deleteArticle(itemId);
        return "redirect:/admin/article/view";
    }
}