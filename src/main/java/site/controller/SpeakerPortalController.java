package site.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import site.facade.BranchService;
import site.facade.SpeakerAccountService;
import site.facade.SubmissionPolicy;
import site.facade.ThumbnailService;
import site.model.Branch;
import site.model.Speaker;
import site.model.Submission;
import site.repository.SpeakerRepository;
import site.repository.SubmissionRepository;

@Controller
@RequestMapping("/my")
public class SpeakerPortalController {

    private final SpeakerAccountService accounts;
    private final SubmissionPolicy policy;
    private final BranchService branchService;
    private final SubmissionRepository submissionRepository;
    private final SpeakerRepository speakerRepository;
    private final ThumbnailService thumbnailService;

    public SpeakerPortalController(SpeakerAccountService accounts, SubmissionPolicy policy,
                                   BranchService branchService, SubmissionRepository submissionRepository,
                                   SpeakerRepository speakerRepository, ThumbnailService thumbnailService) {
        this.accounts = accounts;
        this.policy = policy;
        this.branchService = branchService;
        this.submissionRepository = submissionRepository;
        this.speakerRepository = speakerRepository;
        this.thumbnailService = thumbnailService;
    }

    @GetMapping
    public String dashboard(Authentication auth, Model model) {
        Speaker speaker = accounts.currentSpeaker(auth);
        Branch current = branchService.getCurrentBranch();
        List<Submission> submissions = submissionRepository.findBySpeakerOrCoSpeakerAndBranch(speaker, current);
        LocalDateTime now = LocalDateTime.now();
        Map<Long, Boolean> editable = new HashMap<>();
        submissions.forEach(s -> editable.put(s.getId(), policy.canEdit(speaker, s, current, now)));
        model.addAttribute("speaker", speaker);
        model.addAttribute("submissions", submissions);
        model.addAttribute("editable", editable);
        return "my";
    }

    @GetMapping("/profile")
    public String profile(Authentication auth, Model model) {
        Speaker speaker = accounts.currentSpeaker(auth);
        model.addAttribute("profile", ProfileForm.of(speaker));
        model.addAttribute("email", speaker.getEmail());
        return "my-profile";
    }

    @PostMapping("/profile")
    public String saveProfile(Authentication auth, @Valid @ModelAttribute("profile") ProfileForm profile,
                              BindingResult errors, @RequestParam(required = false) MultipartFile picture,
                              Model model) throws IOException {
        Speaker speaker = accounts.currentSpeaker(auth);
        if (errors.hasErrors()) {
            model.addAttribute("email", speaker.getEmail());
            return "my-profile";
        }
        profile.applyTo(speaker);
        if (picture != null && !picture.isEmpty()) {
            speaker.setPicture(
                thumbnailService.thumbImage(picture.getBytes(), 280, 326, ThumbnailService.ResizeType.FIT_TO_RATIO));
        }
        speakerRepository.save(speaker);
        return "redirect:/my";
    }
}
