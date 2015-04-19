package site.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import javax.validation.Valid;

import net.coobird.thumbnailator.Thumbnails;

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

import site.facade.AdminFacade;
import site.model.Speaker;
import site.repository.SpeakerRepository;

@Controller()
@RequestMapping(value = "/admin/speaker")
public class SpeakerController {

	@Autowired
	@Qualifier(AdminFacade.NAME)
	private AdminFacade adminFacade;

	@Transactional
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(Model model, Pageable pageable) {
		Page<Speaker> speakers = adminFacade.findAllSpeakers(pageable);

		model.addAttribute("speakers", speakers);

		return "/admin/speaker/view.jsp";
	}

	@Transactional
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(@Valid final Speaker speaker,
			BindingResult bindingResult,
			@RequestParam("file") MultipartFile file) {
		if (bindingResult.hasErrors()) {
			return "/admin/speaker/edit.jsp";
		}
		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();
				InputStream in = new ByteArrayInputStream(bytes);
				BufferedImage initialImage = ImageIO.read(in);
				BufferedImage readyImage = null;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				readyImage = Thumbnails.of(initialImage).size(280, 326)
						.outputFormat("jpg").outputQuality(0.8)
						.asBufferedImage();
				ImageIO.write(readyImage, "jpg", baos);
				baos.flush();
				byte[] readyImageInBytes = baos.toByteArray();
				baos.close();
				speaker.setPicture(readyImageInBytes);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		this.adminFacade.saveSpeaker(speaker);

		return "redirect:/admin/speaker/view";
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String edit(Model model) {
		model.addAttribute("speaker", new Speaker());
		return "/admin/speaker/edit.jsp";
	}

	@Transactional
	@RequestMapping(value = "/edit/{itemId}", method = RequestMethod.GET)
	public String edit(@PathVariable("itemId") Long itemId, Model model) {
		Speaker speaker = adminFacade.findOneSpeaker(itemId);
		model.addAttribute("speaker", speaker);
		return "/admin/speaker/edit.jsp";
	}

	@Transactional
	@RequestMapping(value = "/remove/{itemId}", method = RequestMethod.GET)
	public String remove(@PathVariable("itemId") Long itemId, Model model) {
		adminFacade.deleteSpeaker(itemId);
		return "redirect:/admin/speaker/view";
	}
}
