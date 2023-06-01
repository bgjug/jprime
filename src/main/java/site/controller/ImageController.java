package site.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import site.facade.AdminService;
import site.model.Speaker;

/**
 * @author Ivan St. Ivanov
 */
@Controller
@RequestMapping(value = "/image")
public class ImageController {

	@Autowired
	@Qualifier(AdminService.NAME)
	private AdminService adminFacade;

	@GetMapping(value = "/sponsor/{itemId}", produces = {
			MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE })
	@ResponseBody
	public byte[] getSponsorLogo(@PathVariable Long itemId) {
		return adminFacade.findOneSponsor(itemId).getLogo();
	}
	
	@GetMapping(value = "/partner/{itemId}", produces = {
			MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE })
	@ResponseBody
	public byte[] getPartnerLogo(@PathVariable Long itemId) {
		return adminFacade.findOnePartner(itemId).getLogo();
	}

	@GetMapping(value = "/speaker/{itemId}", produces = {
			MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE })
	@ResponseBody
	public byte[] getSpeakerPhoto(@PathVariable Long itemId) {
		Speaker speaker = adminFacade.findOneSpeaker(itemId);
		return speaker != null ? speaker.getPicture() : new byte[0];
	}

}
