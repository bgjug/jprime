package site.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import site.facade.AdminService;
import site.facade.ThumbnailService;
import site.model.Partner;

@Controller()
@RequestMapping(value = "/admin/partner")
public class AdminPartnerController {

    @Autowired
    @Qualifier(AdminService.NAME)
    private AdminService adminFacade;

    @Autowired
    @Qualifier(ThumbnailService.NAME)
    private ThumbnailService thumbnailService;

    @Transactional
        @GetMapping("/view")
    public String view(Model model, Pageable pageable) {
        Page<Partner> partners = adminFacade.findAllPartners(pageable);

        model.addAttribute("partners", partners);

        return "/admin/partner/view.jsp";
    }

    @Transactional
        @PostMapping("/add")
    public String add(@Valid final Partner partner, BindingResult bindingResult,
                      @RequestParam MultipartFile file,
                      @RequestParam(name = "resizeImage", required = false) boolean resize) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            return "/admin/partner/edit.jsp";
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
                e.printStackTrace();
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
        return "/admin/partner/edit.jsp";
    }

    @Transactional
        @GetMapping("/edit/{itemId}")
    public String edit(@PathVariable Long itemId, Model model) {
        Partner partner = adminFacade.findOnePartner(itemId);
        model.addAttribute("partner", partner);
        return "/admin/partner/edit.jsp";
    }

    @Transactional
        @GetMapping("/remove/{itemId}")
    public String remove(@PathVariable Long itemId, Model model) {
        adminFacade.deletePartner(itemId);
        return "redirect:/admin/partner/view";
    }

}
