package site.controller;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.mail.MessagingException;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.ui.Model;
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
            submission.getCoSpeaker().getLastName());
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

}
