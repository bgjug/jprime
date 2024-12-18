package site.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import site.model.Partner;

@Controller
@RequestMapping(value = "/admin/partner")
public class AdminPartnerController {

    private static final Logger log = LoggerFactory.getLogger(AdminPartnerController.class);

    private final AdminService adminFacade;

    private final ThumbnailService thumbnailService;

    public AdminPartnerController(AdminService adminFacade,
        ThumbnailService thumbnailService) {
        this.adminFacade = adminFacade;
        this.thumbnailService = thumbnailService;
    }

    @Transactional
        @GetMapping("/view")
    public String view(Model model, Pageable pageable) {
        Page<Partner> partners = adminFacade.findAllPartners(pageable);

        model.addAttribute("partners", partners.getContent());
        model.addAttribute("totalPages", partners.getTotalPages());
        model.addAttribute("number", partners.getNumber());

        return "admin/partner/view";
    }

    @Transactional
        @PostMapping("/add")
    public String add(@Valid final Partner partner, BindingResult bindingResult,
                      @RequestParam MultipartFile file,
                      @RequestParam(name = "resizeImage", required = false) boolean resize) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            return "admin/partner/edit";
        }
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                if (resize) {
                    partner.setLogo(thumbnailService.thumbImage(bytes, 180, 64,
                                                                ThumbnailService.ResizeType.FIT_TO_HEIGHT));
                }else{
                    partner.setLogo(bytes);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        } else { //empty file is it edit?
            if (partner.getId() != null) {
                Partner oldPartner = adminFacade.findOnePartner(partner.getId());
                byte[] oldImage = oldPartner.getLogo();
                partner.setLogo(oldImage);
            }
        }
        this.adminFacade.savePartner(partner);

        return "redirect:/admin/partner/view";
    }

    @GetMapping("/add")
    public String edit(Model model) {
        model.addAttribute("partner", new Partner());
        return "admin/partner/edit";
    }

    @Transactional
        @GetMapping("/edit/{itemId}")
    public String edit(@PathVariable Long itemId, Model model) {
        Partner partner = adminFacade.findOnePartner(itemId);
        model.addAttribute("partner", partner);
        return "admin/partner/edit";
    }

    @Transactional
        @GetMapping("/remove/{itemId}")
    public String remove(@PathVariable Long itemId) {
        adminFacade.deletePartner(itemId);
        return "redirect:/admin/partner/view";
    }

}
