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
import site.model.Branch;
import site.model.Speaker;

@Controller()
@RequestMapping(value = "/admin/speaker")
public class AdminSpeakerController {

	@Autowired
	@Qualifier(AdminService.NAME)
	private AdminService adminService;
	
	@Autowired
	@Qualifier(ThumbnailService.NAME)
	private ThumbnailService thumbnailService;
	
	@Transactional
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(Model model, Pageable pageable){
		Page<Speaker> speakers = adminService.findAllSpeakers(pageable);
		
		model.addAttribute("speakers", speakers);
		
		return "/admin/speaker/view.jsp";
	}
	
	@Transactional
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(@Valid final Speaker speaker, BindingResult bindingResult, @RequestParam("file") MultipartFile file, Model model){
		if(bindingResult.hasErrors()){
			model.addAttribute("branches", Branch.values());
			return "/admin/speaker/edit.jsp";
		}
		if(!file.isEmpty()){
			try {
                byte[] bytes = file.getBytes();
                speaker.setPicture(thumbnailService.thumbImage(bytes, 280, 326));
            } catch (Exception e) {
                e.printStackTrace();
            }
		} else { //empty file is it edit?
			if(speaker.getId()!= null){
				Speaker oldSpeaker = adminService.findOneSpeaker(speaker.getId());
				byte[] oldImage = oldSpeaker.getPicture();
				speaker.setPicture(oldImage);
			}
		}
		this.adminService.saveSpeaker(speaker);
		
		return "redirect:/admin/speaker/view";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String edit(Model model){
		model.addAttribute("speaker", new Speaker());
		model.addAttribute("branches", Branch.values());
		return "/admin/speaker/edit.jsp";
	}
	
	@Transactional
	@RequestMapping(value = "/edit/{itemId}", method = RequestMethod.GET)
	public String edit(@PathVariable("itemId") Long itemId, Model model){
		Speaker speaker = adminService.findOneSpeaker(itemId);
		model.addAttribute("speaker", speaker);
		model.addAttribute("branches", Branch.values());
		return "/admin/speaker/edit.jsp";
	}
	
	@Transactional
	@RequestMapping(value = "/remove/{itemId}", method = RequestMethod.GET)
	public String remove(@PathVariable("itemId") Long itemId, Model model) {
        adminService.deleteSpeaker(itemId);
        return "redirect:/admin/speaker/view";
    }
}
