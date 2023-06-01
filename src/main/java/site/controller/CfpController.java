package site.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import site.config.Globals;
import site.model.Submission;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author Ivan St. Ivanov
 */
@Controller
public class CfpController extends AbstractCfpController {

    private static final Logger logger = LogManager.getLogger(CfpController.class);

    public static final String CFP_OPEN_JSP = "/proposal.jsp";
    public static final String CFP_CLOSED_JSP = "/cfp-closed.jsp";
    public static final String CFP_THANK_YOU = "/cfp-thank-you.jsp";

    @Value("${agenda.published:false}")
    private boolean agendaPublished;

    @GetMapping("/cfp")
    public String submissionForm(Model model) {
        return goToCFP(new Submission(), model);
    }

    @PostMapping("/cfp")
    public String submitSession(@Valid final Submission submission,
            BindingResult bindingResult,
        @RequestParam MultipartFile speakerImage,
            @RequestParam MultipartFile coSpeakerImage,
            Model model, HttpServletRequest request) {
        boolean invalidCaptcha = false;
        if (submission.getCaptcha() == null || !submission.getCaptcha()
            .equals(request.getSession().getAttribute(CaptchaController.SESSION_PARAM_CAPTCHA_IMAGE))) {
            invalidCaptcha = true;
            bindingResult.rejectValue("captcha", "invalid");
        }
        if (bindingResult.hasErrors() || invalidCaptcha) {
            return goToCFP(submission, model);
        }

        String result = validateEmail(bindingResult, submission, model, submission.getSpeaker().getEmail(), "speaker");
        if (result != null) {
            return result;
        }

        if (hasCoSpeaker(submission)) {
            result = validateEmail(bindingResult, submission, model, submission.getCoSpeaker().getEmail(),
                "coSpeaker");
            if (result != null) {
                return result;
            }
        }

        try {
            saveSubmission(submission, speakerImage, coSpeakerImage);
        } catch (Exception e) {
            logger.error("Can't save the submission", e);
        }
        try {
            sendNotificationEmails(submission);
        } catch (Exception e) {
            logger.error("Could not send confirmation email", e);
        }

        return "redirect:/cfp-thank-you";
    }

    @GetMapping(value = "/cfp-thank-you")
    public String thankYou(Model model) {
        model.addAttribute("tags", userFacade.findAllTags());
        model.addAttribute("cfp_close_date", DateUtils.dateToStringWithMonth(Globals.CURRENT_BRANCH.getCfpCloseDate()));
        return CfpController.CFP_THANK_YOU;
    }

    private String validateEmail(BindingResult bindingResult, Submission submission, Model model, String email, String role) {
        EmailValidator emailValidator = new EmailValidator();

        if (!StringUtils.isEmpty(email) && emailValidator.isValid(email, null)) {
            // Email is valid
            return null;
        }

        bindingResult.addError(new FieldError("submission", role + ".email", "Invalid Email!"));
        return goToCFP(submission, model);
    }

    private String goToCFP(@Valid Submission submission, Model model) {
        model.addAttribute("tags", userFacade.findAllTags());
        model.addAttribute("agenda", agendaPublished);
        model.addAttribute("cfp_close_date", DateUtils.dateToStringWithMonth(Globals.CURRENT_BRANCH.getCfpCloseDate()));
        DateTime startDate = Globals.CURRENT_BRANCH.getStartDate();
        model.addAttribute("conference_dates", String.format("%s and %s", DateUtils.dateToString(startDate),
            DateUtils.dateToStringWithMonthAndYear(startDate.plusDays(1))));

        buildCfpFormModel(model, submission);

        if (Globals.CURRENT_BRANCH.getCfpCloseDate().isAfterNow()) {
            return CfpController.CFP_OPEN_JSP;
        }
        return CFP_CLOSED_JSP;
    }
}
