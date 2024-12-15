package site.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import site.facade.AdminService;
import site.facade.ThumbnailService;
import site.model.Sponsor;

@Controller()
@RequestMapping(value = "/admin/sponsor")
public class AdminSponsorController {

    private static final Logger log = LogManager.getLogger(AdminSponsorController.class);

    private final AdminService adminFacade;

    private final ThumbnailService thumbnailService;

    public AdminSponsorController(@Qualifier(AdminService.NAME) AdminService adminFacade,
        @Qualifier(ThumbnailService.NAME) ThumbnailService thumbnailService) {
        this.adminFacade = adminFacade;
        this.thumbnailService = thumbnailService;
    }

    @Transactional
        @GetMapping("/view")
    public String view(Model model, Pageable pageable) {
        Page<Sponsor> sponsors = adminFacade.findAllSponsors(pageable);

        model.addAttribute("sponsors", sponsors.getContent());
        model.addAttribute("totalPages", sponsors.getTotalPages());
        model.addAttribute("number", sponsors.getNumber());

        return "admin/sponsor/view";
    }

    @Transactional
        @PostMapping("/add")
    public String add(@Valid final Sponsor sponsor, BindingResult bindingResult,
                      @RequestParam MultipartFile file,
                      @RequestParam(name = "resizeImage", required = false) boolean resize) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            return "admin/sponsor/edit";
        }
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                if (resize) {
                    sponsor.setLogo(thumbnailService.thumbImage(bytes, 180, 64,
                                                                ThumbnailService.ResizeType.FIT_TO_HEIGHT));
                } else {
                    sponsor.setLogo(bytes);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        } else { //empty file is it edit?
            if (sponsor.getId() != null) {
                Sponsor oldSponsor = adminFacade.findOneSponsor(sponsor.getId());
                byte[] oldImage = oldSponsor.getLogo();
                sponsor.setLogo(oldImage);
            }
        }
        this.adminFacade.saveSponsor(sponsor);

        return "redirect:/admin/sponsor/view";
    }

    @GetMapping("/add")
    public String edit(Model model) {
        model.addAttribute("sponsor", new Sponsor());
        return "admin/sponsor/edit";
    }

    @Transactional
        @GetMapping("/edit/{itemId}")
    public String edit(@PathVariable Long itemId, Model model) {
        Sponsor sponsor = adminFacade.findOneSponsor(itemId);
        model.addAttribute("sponsor", sponsor);
        return "admin/sponsor/edit";
    }

    @Transactional
        @GetMapping("/remove/{itemId}")
    public String remove(@PathVariable Long itemId, Model model) {
        adminFacade.deleteSponsor(itemId);
        return "redirect:/admin/sponsor/view";
    }

}
