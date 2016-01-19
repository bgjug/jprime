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

import site.facade.AdminService;
import site.model.Tag;


@Controller()
@RequestMapping(value = "/admin/tag")
public class AdminTagController {

	@Autowired
	@Qualifier(AdminService.NAME)
	private AdminService adminFacade;
	
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(Model model, Pageable pageable){
		Page<Tag> tags = adminFacade.findAllTags(pageable);
		
		
		model.addAttribute("tags", tags);
		
		return "/admin/tag/view.jsp";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(@Valid final Tag tag, BindingResult bindingResult){
		if(bindingResult.hasErrors()){
			return "/admin/tag/edit.jsp";
		}
		this.adminFacade.saveTag(tag);
		
		return "redirect:/admin/tag/view";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String edit(Model model){
		model.addAttribute("tag", new Tag());
		return "/admin/tag/edit.jsp";
	}
	
	@RequestMapping(value = "/edit/{itemId}", method = RequestMethod.GET)
	public String edit(@PathVariable("itemId") Long itemId, Model model){
		Tag tag = adminFacade.findOneTag(itemId);
		model.addAttribute("tag", tag);
		return "/admin/tag/edit.jsp";
	}
	
	@RequestMapping(value = "/remove/{itemId}", method = RequestMethod.GET)
	public String remove(@PathVariable("itemId") Long itemId, Model model){
		adminFacade.deleteTag(itemId);
		return "redirect:/admin/tag/view";
	}
	
}
