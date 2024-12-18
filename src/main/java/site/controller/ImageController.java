package site.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import site.facade.AdminService;
import site.model.Speaker;

/**
 * @author Ivan St. Ivanov
 */
@RestController
@RequestMapping(value = "/image", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE })
public class ImageController {

	@Autowired
	private AdminService adminFacade;

	@GetMapping(value = "/sponsor/{itemId}")
	public byte[] getSponsorLogo(@PathVariable Long itemId) {
		return adminFacade.findOneSponsor(itemId).getLogo();
	}
	
	@GetMapping(value = "/partner/{itemId}")
	public byte[] getPartnerLogo(@PathVariable Long itemId) {
		return adminFacade.findOnePartner(itemId).getLogo();
	}

	@GetMapping(value = "/speaker/{itemId}")
	public byte[] getSpeakerPhoto(@PathVariable Long itemId) {
		Speaker speaker = adminFacade.findOneSpeaker(itemId);
		return speaker != null ? speaker.getPicture() : new byte[0];
	}

}
