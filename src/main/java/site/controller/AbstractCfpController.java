package site.controller;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import jakarta.mail.MessagingException;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import site.facade.MailService;
import site.facade.ThumbnailService;
import site.facade.UserService;
import site.model.SessionLevel;
import site.model.SessionType;
import site.model.Speaker;
import site.model.Submission;

/**
 * @author Ivan St. Ivanov
 */
public class AbstractCfpController {

    private static final Logger log = LogManager.getLogger(AbstractCfpController.class);

    public static final String JPRIME_CONF_MAIL_ADDRESS = "conference@jprime.io";

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    protected UserService userFacade;

    @Autowired
    @Lazy
    private MailService mailFacade;

    @Autowired
    private ThumbnailService thumbnailService;

    /**
     * Copies data from the submissionSpeaker object to the speaker object if the respective fields in
     * submissionSpeaker are not empty or blank.
     *
     * @param speaker           the Speaker object to which data will be copied
     * @param submissionSpeaker the Speaker object from which data will be copied
     */
    private static void copyDataFromSubmission(Speaker speaker, Speaker submissionSpeaker) {
        if (ArrayUtils.isNotEmpty(submissionSpeaker.getPicture())) {
            speaker.setPicture(submissionSpeaker.getPicture());
        }

        if (StringUtils.isNotBlank(submissionSpeaker.getBio())) {
            speaker.setBio(submissionSpeaker.getBio());
        }

        if (StringUtils.isNotBlank(submissionSpeaker.getTwitter())) {
            speaker.setTwitter(submissionSpeaker.getTwitter());
        }

        if (StringUtils.isNotBlank(submissionSpeaker.getFirstName())) {
            speaker.setFirstName(submissionSpeaker.getFirstName());
        }

        if (StringUtils.isNotBlank(submissionSpeaker.getLastName())) {
            speaker.setLastName(submissionSpeaker.getLastName());
        }

        if (StringUtils.isNotBlank(submissionSpeaker.getBsky())) {
            speaker.setBsky(submissionSpeaker.getBsky());
        }
    }

    protected void updateCfpModel(Model model, Submission submission) {
        model.addAttribute("submission", submission);
        model.addAttribute("levels", SessionLevel.values());
        model.addAttribute("sessionTypes", Arrays.stream(SessionType.values())
            .collect(Collectors.toMap(Function.identity(), SessionType::toString)));
        model.addAttribute("coSpeaker_caption", submission.getCoSpeaker() == null || StringUtils.isEmpty(
            submission.getCoSpeaker().getFirstName()) ? "Add co speaker" : "Remove co speaker");
    }

    protected void saveSubmission(Submission submission, MultipartFile speakerImage,
        MultipartFile coSpeakerImage) {
        submission.setSpeaker(handleSubmittedSpeaker(submission.getSpeaker(), speakerImage));
        if (hasCoSpeaker(submission)) {
            submission.setCoSpeaker(handleSubmittedSpeaker(submission.getCoSpeaker(), coSpeakerImage));
        } else {
            submission.setCoSpeaker(null);
        }
        userFacade.submitTalk(submission);
    }

    protected boolean hasCoSpeaker(Submission submission) {
        return submission.getCoSpeaker() != null && !StringUtils.isEmpty(
            submission.getCoSpeaker().getEmail());
    }

    private Speaker handleSubmittedSpeaker(Speaker speaker, MultipartFile image) {
        Speaker existingSpeaker = userFacade.findSpeaker(speaker);
        if (existingSpeaker == null) {
            //new speaker. file is required
            fixTwitterHandle(speaker);
            formatPicture(speaker, image);
        }
        return speaker;
    }

    private void formatPicture(Speaker speaker, MultipartFile image) {
        if (image.isEmpty()) {
            return;
        }

        try {
            byte[] bytes = image.getBytes();
            speaker.setPicture(
                thumbnailService.thumbImage(bytes, 280, 326, ThumbnailService.ResizeType.FIT_TO_RATIO));
        } catch (Exception e) {
            log.error("Error while processing speaker picture!!!", e);
        }
    }

    void fixTwitterHandle(Speaker speaker) {
        String twitterHandle = speaker.getTwitter();
        if (twitterHandle != null && twitterHandle.startsWith("@")) {
            speaker.setTwitter(twitterHandle.substring(1));
        }
    }

    public void sendNotificationEmails(Submission submission) throws MessagingException {

        mailFacade.sendEmail(submission.getSpeaker().getEmail(), "jPrime talk proposal",
            prepareNewSubmissionContent(submission, "submissionContent.html"));
        if (submission.getCoSpeaker() != null) {
            mailFacade.sendEmail(submission.getCoSpeaker().getEmail(), "jPrime talk proposal",
                prepareNewSubmissionContent(submission, "submissionContent.html"));
        }
        mailFacade.sendEmail(JPRIME_CONF_MAIL_ADDRESS, "New talk proposal",
            prepareNewSubmissionContent(submission, "newSubmission.html"));

    }

    private String prepareNewSubmissionContent(Submission submission, String templateName) {
        // Create a context for Thymeleaf
        Context context = new Context();
        context.setVariable("submission", submission);

        // Process the template and generate content
        return templateEngine.process(templateName, context);
    }

    protected String validateAndUpdateSpeaker(BindingResult bindingResult,
        Speaker speaker, String role, Consumer<Speaker> speakerUpdate, Supplier<String> onError) {
        String result = validateEmail(bindingResult, speaker.getEmail(), role, onError);
        if (result != null) {
            return result;
        }

        Speaker existingSpeaker = userFacade.findSpeaker(speaker);
        if (existingSpeaker != null) {
            copyDataFromSubmission(existingSpeaker, speaker);
            speakerUpdate.accept(existingSpeaker);
        } else {
            result = validateSpeaker(speaker, bindingResult, role, onError);
            return result;
        }

        return null;
    }

    private String validateSpeaker(Speaker speaker, BindingResult bindingResult, String role,
        Supplier<String> errorSupplier) {
        String msg;
        String field;
        if (StringUtils.isBlank(speaker.getFirstName())) {
            msg = "First name is required!";
            field = "firstName";
        } else {
            if (StringUtils.isBlank(speaker.getLastName())) {
                msg = "Last name is required!";
                field = "lastName";
            } else {
                msg = StringUtils.isEmpty(speaker.getBio()) ? "Bio is required!" : null;
                field = "bio";
            }
        }

        if (msg == null) {
            return null;
        }
        bindingResult.addError(new FieldError("submission", role + "." + field, msg));

        return errorSupplier.get();
    }

    private String validateEmail(BindingResult bindingResult, String email, String role,
        Supplier<String> errorSupplier) {
        EmailValidator emailValidator = new EmailValidator();

        if (!StringUtils.isEmpty(email) && emailValidator.isValid(email, null)) {
            // Email is valid
            return null;
        }

        bindingResult.addError(new FieldError("submission", role + ".email", "Invalid Email!"));
        return errorSupplier.get();
    }
}
