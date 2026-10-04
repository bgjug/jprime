package site.controller;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Supplier;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import site.facade.BranchService;
import site.facade.SpeakerAccountService;
import site.model.Branch;
import site.model.Speaker;
import site.model.Submission;
import site.model.SubmissionStatus;

/**
 * @author Ivan St. Ivanov
 */
@Controller
public class CfpController extends AbstractCfpController {

    private static final Logger logger = LogManager.getLogger(CfpController.class);

    public static final String CFP_OPEN_JSP = "proposal";
    public static final String CFP_CLOSED_JSP = "cfp-closed";
    public static final String CFP_THANK_YOU = "cfp-thank-you";
    public static final String CFP_PROBLEM = "cfp-problem";

    private final BranchService branchService;
    private final SpeakerAccountService accounts;

    public CfpController(BranchService branchService, SpeakerAccountService accounts) {
        this.branchService = branchService;
        this.accounts = accounts;
    }

    @GetMapping("/cfp")
    public String submissionForm(Model model, Authentication auth) {
        Submission submission = new Submission(branchService.getCurrentBranch());
        submission.setSpeaker(accounts.currentSpeaker(auth));
        return goToCFP(submission, model);
    }

    @PostMapping("/cfp")
    public String submitSession(@Valid final Submission submission, BindingResult bindingResult,
        @RequestParam MultipartFile speakerImage, @RequestParam MultipartFile coSpeakerImage, Model model,
        HttpServletRequest request, Authentication auth) {
        boolean invalidCaptcha = false;
        if (submission.getCaptcha() == null || !submission.getCaptcha()
            .equals(request.getSession().getAttribute(CaptchaController.SESSION_PARAM_CAPTCHA_IMAGE))) {
            invalidCaptcha = true;
            bindingResult.rejectValue("captcha", "invalid");
        }

        if (bindingResult.hasErrors() || invalidCaptcha) {
            return goToCFP(submission, model);
        }

        Speaker me = accounts.currentSpeaker(auth);
        if (hasCoSpeaker(submission)) {
            Speaker typed = submission.getCoSpeaker();
            Supplier<String> onError = () -> goToCFP(submission, model);
            if (typed.getEmail().trim().equalsIgnoreCase(me.getEmail())) {
                bindingResult.addError(
                    new FieldError("submission", "coSpeaker.email", "You can't be your own co-speaker"));
                return onError.get();
            }
            String result = validateEmail(bindingResult, typed.getEmail(), "coSpeaker", onError);
            if (result != null) {
                return result;
            }
            Optional<Speaker> existing = accounts.findOrPromote(typed.getEmail().trim());
            if (existing.isPresent()) {
                submission.setCoSpeaker(existing.get());
            } else {
                result = validateSpeaker(typed, bindingResult, "coSpeaker", onError);
                if (result != null) {
                    return result;
                }
                // fresh row: the bound object may carry a posted id or password
                Speaker coSpeaker = new Speaker();
                coSpeaker.setEmail(typed.getEmail().trim());
                copyDataFromSubmission(coSpeaker, typed);
                formatPicture(coSpeaker, coSpeakerImage);
                fixTwitterHandle(coSpeaker);
                submission.setCoSpeaker(coSpeaker);
            }
        } else {
            submission.setCoSpeaker(null);
        }

        copyDataFromSubmission(me, submission.getSpeaker());
        formatPicture(me, speakerImage);
        fixTwitterHandle(me);
        submission.setSpeaker(me);

        submission.setId(null);
        submission.setStatus(SubmissionStatus.SUBMITTED);
        submission.setFeatured(false);
        submission.setBranch(branchService.getCurrentBranch());

        try {
            userFacade.submitTalk(submission);
        } catch (Exception e) {
            logger.error("Can't save the submission", e);
            return "redirect:/cfp-problem";
        }

        try {
            sendNotificationEmails(submission);
        } catch (Exception e) {
            logger.error("Could not send confirmation email", e);
        }

        return "redirect:/cfp-thank-you";
    }

    @GetMapping(value = "/cfp-problem")
    public String cfpProblem(Model model) {

        return CfpController.CFP_PROBLEM;
    }

    @GetMapping(value = "/cfp-thank-you")
    public String thankYou(Model model) {
        Branch currentBranch = branchService.getCurrentBranch();

        model.addAttribute("tags", userFacade.findAllTags());
        model.addAttribute("cfp_close_date",
            DateUtils.dateToStringWithMonth(currentBranch.getCfpCloseDate()));
        return CfpController.CFP_THANK_YOU;
    }

    private String goToCFP(@Valid Submission submission, Model model) {
        Branch currentBranch = branchService.getCurrentBranch();

        model.addAttribute("tags", userFacade.findAllTags());
        model.addAttribute("agenda", currentBranch.isAgendaPublished());
        model.addAttribute("cfp_close_date",
            DateUtils.dateToStringWithMonth(currentBranch.getCfpCloseDate()));
        LocalDateTime startDate = currentBranch.getStartDate();
        model.addAttribute("conference_dates", String.format("%s and %s", DateUtils.dateToString(startDate),
            DateUtils.dateToStringWithMonthAndYear(startDate.plusDays(1))));

        updateCfpModel(model, submission);

        LocalDateTime now = LocalDateTime.now();
        if (currentBranch.getCfpCloseDate().isAfter(now) && currentBranch.getCfpOpenDate().isBefore(now)) {
            return CfpController.CFP_OPEN_JSP;
        }
        return CFP_CLOSED_JSP;
    }
}
