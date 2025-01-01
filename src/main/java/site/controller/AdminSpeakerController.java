package site.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import site.facade.BranchService;
import site.facade.ThumbnailService;
import site.model.Branch;
import site.model.Speaker;

@Controller
@RequestMapping(value = "/admin/speaker")
public class AdminSpeakerController {

    private static final Logger log = LogManager.getLogger(AdminSpeakerController.class);

    private final AdminService adminService;

    private final ThumbnailService thumbnailService;

    private final BranchService branchService;

    public AdminSpeakerController(AdminService adminService,
        ThumbnailService thumbnailService, BranchService branchService) {
        this.adminService = adminService;
        this.thumbnailService = thumbnailService;
        this.branchService = branchService;
    }

    @Transactional
    @GetMapping(value = "/view")
    public String view(Model model, Pageable pageable, @RequestParam(required = false)String year) {
        Page<Speaker> speakers;
        if (StringUtils.isNotBlank(year)) {
           Branch branch = branchService.findBranchByYear(Integer.parseInt(year));
           speakers = adminService.findSpeakersByBranch(pageable, branch);
        } else {
           speakers = adminService.findAllSpeakers(pageable);
        }

        model.addAttribute("speakers", speakers.getContent());
        model.addAttribute("number", speakers.getNumber());
        model.addAttribute("totalPages", speakers.getTotalPages());
        model.addAttribute("branches", branchService.allBranches());
        model.addAttribute("selected_branch", year);

        return "admin/speaker/view";
    }

    @Transactional
    @PostMapping(value = "/add")
    public String add(@Valid final Speaker speaker, BindingResult bindingResult,
                      @RequestParam MultipartFile file, Model model,
                      @RequestParam(name = "resizeImage", required = false, defaultValue = "false") boolean resize, @RequestParam(required = false) String sourcePage) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("branches", branchService.allBranches());
            return "admin/speaker/edit";
        }
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                if (resize) {
                    speaker.setPicture(thumbnailService.thumbImage(bytes, 280, 326,
                                                                   ThumbnailService.ResizeType.FIT_TO_RATIO));
                } else {
                    speaker.setPicture(bytes);
                }
            } catch (Exception e) {
                log.error("Error while processing speaker image!!!", e);
            }
        } else { //empty file is it edit?
            if (speaker.getId() != null) {
                Speaker oldSpeaker = adminService.findOneSpeaker(speaker.getId());
                byte[] oldImage = oldSpeaker.getPicture();
                speaker.setPicture(oldImage);
            }
        }
        this.adminService.saveSpeaker(speaker);

        return StringUtils.isEmpty(sourcePage) ? "redirect:/admin/speaker/view" : "redirect:" + sourcePage;
    }

    @GetMapping(value = "/add")
    public String edit(Model model, @RequestParam(required = false) String sourcePage) {
        model.addAttribute("speaker", new Speaker());
        model.addAttribute("sourcePage", sourcePage);
        model.addAttribute("branches", branchService.allBranches());
        return "/admin/speaker/edit";
    }

    @Transactional
    @GetMapping(value = "/edit/{itemId}")
    public String edit(@PathVariable Long itemId, Model model, @RequestParam(required = false) String sourcePage) {
        Speaker speaker = adminService.findOneSpeaker(itemId);
        model.addAttribute("speaker", speaker);
        model.addAttribute("sourcePage", sourcePage);
        model.addAttribute("branches", branchService.allBranches());
        return "/admin/speaker/edit";
    }

    @Transactional
    @GetMapping(value = "/remove/{itemId}")
    public String remove(@PathVariable Long itemId) {
        adminService.deleteSpeaker(itemId);
        return "redirect:/admin/speaker/view";
    }
}
