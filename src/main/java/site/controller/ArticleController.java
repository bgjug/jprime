package site.controller;

import javax.validation.Valid;

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

import site.facade.AdminFacade;
import site.model.Article;


@Controller()
@RequestMapping(value = "/admin/article")
public class ArticleController {

	@Autowired
	@Qualifier(AdminFacade.NAME)
	private AdminFacade adminFacade;
	
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(Model model, Pageable pageable){
		Page<Article> articles = adminFacade.findAllArticles(pageable);
		
		
		model.addAttribute("articles", articles);
		
		return "/admin/article/view.jsp";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(@Valid final Article article, BindingResult bindingResult){
		if(bindingResult.hasErrors()){
			return "/admin/article/edit.jsp";
		}
		this.adminFacade.saveArticle(article);
		
		return "redirect:/admin/article/view";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String edit(Model model){
		model.addAttribute("article", new Article());
		model.addAttribute("tags", this.adminFacade.findAllTags());
		return "/admin/article/edit.jsp";
	}
	
	@RequestMapping(value = "/edit/{itemId}", method = RequestMethod.GET)
	public String edit(@PathVariable("itemId") Long itemId, Model model){
		Article article = adminFacade.findOneArticle(itemId);
		model.addAttribute("article", article);
		model.addAttribute("tags", this.adminFacade.findAllTags());
		return "/admin/article/edit.jsp";
	}
	
	@RequestMapping(value = "/remove/{itemId}", method = RequestMethod.GET)
	public String remove(@PathVariable("itemId") Long itemId, Model model){
		adminFacade.deleteArticle(itemId);
		return "redirect:/admin/article/view";
	}
	
}
