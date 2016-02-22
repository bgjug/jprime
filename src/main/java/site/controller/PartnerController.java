package site.controller;

import javax.transaction.Transactional;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import site.facade.AdminService;
import site.facade.ThumbnailService;
import site.model.Partner;


@Controller()
@RequestMapping(value = "/admin/partner")
public class PartnerController {

	@Autowired
	@Qualifier(AdminService.NAME)
	private AdminService adminFacade;
	
	@Autowired
	@Qualifier(ThumbnailService.NAME)
	private ThumbnailService thumbnailService;
	
	@Transactional
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(Model model, Pageable pageable){
		Page<Partner> partners = adminFacade.findAllPartners(pageable);
		
		model.addAttribute("partners", partners);
		
		return "/admin/partner/view.jsp";
	}
	
	@Transactional
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(@Valid final Partner partner, BindingResult bindingResult, @RequestParam("file") MultipartFile file){
		if(bindingResult.hasErrors()){
			System.out.println(bindingResult.getAllErrors());
			return "/admin/partner/edit.jsp";
		}
		if(!file.isEmpty()){
			try {
                byte[] bytes = file.getBytes();
                partner.setLogo(thumbnailService.thumbImage(bytes, 180, 64));
            } catch (Exception e) {
                e.printStackTrace();
            }
		} else { //empty file is it edit?
			if(partner.getId()!= null){
				Partner oldPartner = adminFacade.findOnePartner(partner.getId());
				byte[] oldImage = oldPartner.getLogo();
				partner.setLogo(oldImage);
			}
		}
		this.adminFacade.savePartner(partner);
		
		return "redirect:/admin/partner/view";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String edit(Model model){
		model.addAttribute("partner", new Partner());
		return "/admin/partner/edit.jsp";
	}
	
	@Transactional
	@RequestMapping(value = "/edit/{itemId}", method = RequestMethod.GET)
	public String edit(@PathVariable("itemId") Long itemId, Model model){
		Partner partner = adminFacade.findOnePartner(itemId);
		model.addAttribute("partner", partner);
		return "/admin/partner/edit.jsp";
	}
	
	@Transactional
	@RequestMapping(value = "/remove/{itemId}", method = RequestMethod.GET)
	public String remove(@PathVariable("itemId") Long itemId, Model model){
		adminFacade.deletePartner(itemId);
		return "redirect:/admin/partner/view";
	}

}
