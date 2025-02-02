package site.controller;

import java.time.LocalDateTime;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import site.facade.BranchService;
import site.model.Branch;
import site.model.Submission;

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

    public CfpController(BranchService branchService) {
        this.branchService = branchService;
    }

    @GetMapping("/cfp")
    public String submissionForm(Model model) {
        return goToCFP(new Submission(branchService.getCurrentBranch()), model);
    }

    @PostMapping("/cfp")
    public String submitSession(@Valid final Submission submission, BindingResult bindingResult,
        @RequestParam MultipartFile speakerImage, @RequestParam MultipartFile coSpeakerImage, Model model,
        HttpServletRequest request) {
        boolean invalidCaptcha = false;
        if (submission.getCaptcha() == null || !submission.getCaptcha()
            .equals(request.getSession().getAttribute(CaptchaController.SESSION_PARAM_CAPTCHA_IMAGE))) {
            invalidCaptcha = true;
            bindingResult.rejectValue("captcha", "invalid");
        }

        if (bindingResult.hasErrors() || invalidCaptcha) {
            return goToCFP(submission, model);
        }

        String result =
            validateAndUpdateSpeaker(bindingResult, submission.getSpeaker(), "speaker",
                submission::setSpeaker,
                () -> goToCFP(submission, model));
        if (result != null) {
            return result;
        }
        if (hasCoSpeaker(submission)) {
            result =
                validateAndUpdateSpeaker(bindingResult, submission.getCoSpeaker(), "coSpeaker", submission::setCoSpeaker,
                    () -> goToCFP(submission, model));
            if (result != null) {
                return result;
            }
        }

        submission.setBranch(branchService.getCurrentBranch());

        try {
            saveSubmission(submission, speakerImage, coSpeakerImage);
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
