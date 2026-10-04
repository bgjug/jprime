package site.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import site.facade.BranchService;
import site.facade.SpeakerAccountService;
import site.facade.SubmissionPolicy;
import site.facade.ThumbnailService;
import site.model.Branch;
import site.model.SessionLevel;
import site.model.SessionType;
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
        if (picture != null && !picture.isEmpty()) {
            try {
                speaker.setPicture(thumbnailService.thumbImage(picture.getBytes(), 280, 326,
                    ThumbnailService.ResizeType.FIT_TO_RATIO));
            } catch (RuntimeException e) {
                errors.reject("picture.invalid", "Please upload an image (JPG or PNG)");
                model.addAttribute("email", speaker.getEmail());
                return "my-profile";
            }
        }
        profile.applyTo(speaker);
        speakerRepository.save(speaker);
        return "redirect:/my";
    }

    @GetMapping("/submissions/{id}")
    public String submission(Authentication auth, @PathVariable Long id, Model model) {
        Speaker speaker = accounts.currentSpeaker(auth);
        Submission s = viewable(speaker, id);
        return form(model, s, SubmissionForm.of(s), speaker);
    }

    @PostMapping("/submissions/{id}")
    public String saveSubmission(Authentication auth, @PathVariable Long id,
                                 @Valid @ModelAttribute("form") SubmissionForm form, BindingResult errors,
                                 Model model) {
        Speaker speaker = accounts.currentSpeaker(auth);
        Submission s = viewable(speaker, id);
        if (!policy.canEdit(speaker, s, branchService.getCurrentBranch(), LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (errors.hasErrors()) {
            return form(model, s, form, speaker);
        }
        form.applyTo(s);
        submissionRepository.save(s);
        return "redirect:/my";
    }

    private Submission viewable(Speaker speaker, Long id) {
        return submissionRepository.findById(id).filter(s -> policy.canView(speaker, s))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private String form(Model model, Submission s, SubmissionForm form, Speaker speaker) {
        model.addAttribute("form", form);
        model.addAttribute("submission", s);
        model.addAttribute("editable",
            policy.canEdit(speaker, s, branchService.getCurrentBranch(), LocalDateTime.now()));
        model.addAttribute("levels", SessionLevel.values());
        model.addAttribute("sessionTypes", Arrays.stream(SessionType.values())
            .collect(Collectors.toMap(Function.identity(), SessionType::toString)));
        return "my-submission";
    }
}
