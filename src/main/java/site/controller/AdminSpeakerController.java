package site.controller;

import javax.transaction.Transactional;
import javax.validation.Valid;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
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
import site.model.Branch;
import site.model.Speaker;

@Controller()
@RequestMapping(value = "/admin/speaker")
public class AdminSpeakerController {

    private final AdminService adminService;

    private final ThumbnailService thumbnailService;

    public AdminSpeakerController(@Qualifier(AdminService.NAME) AdminService adminService,
        @Qualifier(ThumbnailService.NAME) ThumbnailService thumbnailService) {
        this.adminService = adminService;
        this.thumbnailService = thumbnailService;
    }

    @Transactional
    @GetMapping(value = "/view")
    public String view(Model model, Pageable pageable, @RequestParam(value = "year", required = false)String year) {
        Page<Speaker> speakers;
        if (StringUtils.isNotBlank(year)) {
           Branch branch = Branch.valueOfYear(year);
           speakers = adminService.findSpeakersByBranch(pageable, branch);
        } else {
           speakers = adminService.findAllSpeakers(pageable);
        }

        model.addAttribute("speakers", speakers);
        model.addAttribute("branches", Arrays.asList(Branch.values()));
        model.addAttribute("selected_branch", year);

        return "/admin/speaker/view.jsp";
    }

    @Transactional
    @PostMapping(value = "/add")
    public String add(@Valid final Speaker speaker, BindingResult bindingResult,
                      @RequestParam("file") MultipartFile file, Model model,
                      @RequestParam(name = "resizeImage", required = false) boolean resize, @RequestParam(value = "sourcePage", required = false) String sourcePage) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("branches", Branch.values());
            return "/admin/speaker/edit.jsp";
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
                e.printStackTrace();
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
    public String edit(Model model, @RequestParam(name = "sourcePage", required = false) String sourcePage) {
        model.addAttribute("speaker", new Speaker());
        model.addAttribute("sourcePage", sourcePage);
        model.addAttribute("branches", Branch.values());
        return "/admin/speaker/edit.jsp";
    }

    @Transactional
    @GetMapping(value = "/edit/{itemId}")
    public String edit(@PathVariable("itemId") Long itemId, Model model,@RequestParam(name = "sourcePage", required = false) String sourcePage) {
        Speaker speaker = adminService.findOneSpeaker(itemId);
        model.addAttribute("speaker", speaker);
        model.addAttribute("sourcePage", sourcePage);
        model.addAttribute("branches", Branch.values());
        return "/admin/speaker/edit.jsp";
    }

    @Transactional
    @GetMapping(value = "/remove/{itemId}")
    public String remove(@PathVariable("itemId") Long itemId, Model model) {
        adminService.deleteSpeaker(itemId);
        return "redirect:/admin/speaker/view";
    }
}
