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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import site.facade.AdminService;
import site.facade.ThumbnailService;
import site.model.Speaker;
import site.model.Sponsor;


@Controller()
@RequestMapping(value = "/admin/sponsor")
public class SponsorController {

	@Autowired
	@Qualifier(AdminService.NAME)
	private AdminService adminFacade;
	
	@Autowired
	@Qualifier(ThumbnailService.NAME)
	private ThumbnailService thumbnailService;
	
	@Transactional
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(Model model, Pageable pageable){
		Page<Sponsor> sponsors = adminFacade.findAllSponsors(pageable);
		
		model.addAttribute("sponsors", sponsors);
		
		return "/admin/sponsor/view.jsp";
	}
	
	@Transactional
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(@Valid final Sponsor sponsor, BindingResult bindingResult, @RequestParam("file") MultipartFile file){
		if(bindingResult.hasErrors()){
			System.out.println(bindingResult.getAllErrors());
			return "/admin/sponsor/edit.jsp";
		}
		if(!file.isEmpty()){
			try {
                byte[] bytes = file.getBytes();
                sponsor.setLogo(thumbnailService.thumbImage(bytes, 180, 64));
            } catch (Exception e) {
                e.printStackTrace();
            }
		} else { //empty file is it edit?
			if(sponsor.getId()!= null){
				Sponsor oldSponsor = adminFacade.findOneSponsor(sponsor.getId());
				byte[] oldImage = oldSponsor.getLogo();
				sponsor.setLogo(oldImage);
			}
		}
		this.adminFacade.saveSponsor(sponsor);
		
		return "redirect:/admin/sponsor/view";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String edit(Model model){
		model.addAttribute("sponsor", new Sponsor());
		return "/admin/sponsor/edit.jsp";
	}
	
	@Transactional
	@RequestMapping(value = "/edit/{itemId}", method = RequestMethod.GET)
	public String edit(@PathVariable("itemId") Long itemId, Model model){
		Sponsor sponsor = adminFacade.findOneSponsor(itemId);
		model.addAttribute("sponsor", sponsor);
		return "/admin/sponsor/edit.jsp";
	}
	
	@Transactional
	@RequestMapping(value = "/remove/{itemId}", method = RequestMethod.GET)
	public String remove(@PathVariable("itemId") Long itemId, Model model){
		adminFacade.deleteSponsor(itemId);
		return "redirect:/admin/sponsor/view";
	}

}
