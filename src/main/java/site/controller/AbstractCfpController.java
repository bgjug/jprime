package site.controller;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import site.config.Globals;
import site.facade.MailService;
import site.facade.ThumbnailService;
import site.facade.UserService;
import site.model.Branch;
import site.model.SessionLevel;
import site.model.Speaker;
import site.model.Submission;
import site.model.SessionType;

import static site.controller.ResourceAsString.resourceAsString;

/**
 * @author Ivan St. Ivanov
 */
public class AbstractCfpController {

    public static final String JPRIME_CONF_MAIL_ADDRESS = "conference@jprime.io";

    @Autowired
    @Qualifier(UserService.NAME)
    protected UserService userFacade;

    @Autowired
    @Qualifier(MailService.NAME)
    @Lazy
    protected MailService mailFacade;

    @Autowired
    @Qualifier(ThumbnailService.NAME)
    private ThumbnailService thumbnailService;

    protected Model buildCfpFormModel(Model model, Submission submission) {
        model.addAttribute("submission", submission);
        model.addAttribute("levels", SessionLevel.values());
        model.addAttribute("sessionTypes", Arrays.stream(SessionType.values()).collect(
                        Collectors.toMap(Function.identity(), SessionType::toString)));
        model.addAttribute("branches", Branch.values());
        model.addAttribute("coSpeaker_caption", submission.getCoSpeaker() == null || StringUtils.isEmpty(
                        submission.getCoSpeaker().getFirstName()) ? "Add co speaker" : "Remove co speaker");

        return model;
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
        if (existingSpeaker != null) {
            if (existingSpeaker.getBranch() != Globals.CURRENT_BRANCH) {
                existingSpeaker.setAccepted(false);
                existingSpeaker.setFeatured(false);
            }
            existingSpeaker.setBranch(Globals.CURRENT_BRANCH);
            return existingSpeaker;
        } else {
            //new speaker.. file is required
            fixTwitterHandle(speaker);
            speaker.setBranch(Globals.CURRENT_BRANCH);
            formatPicture(speaker, image);
            return speaker;
        }
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
            e.printStackTrace();
        }
    }

    Speaker fixTwitterHandle(Speaker speaker) {
        String twitterHandle = speaker.getTwitter();
        if (twitterHandle != null && twitterHandle.startsWith("@")) {
            speaker.setTwitter(twitterHandle.substring(1));
        }
        return speaker;
    }

    void setMailFacade(MailService mailFacade) {
        this.mailFacade = mailFacade;
    }

    private String loadMailContentTemplate(String templateFileName) throws IOException {
        return resourceAsString("/" + templateFileName);
    }

    public void sendNotificationEmails(Submission submission)
                    throws IOException, MessagingException {

        mailFacade.sendEmail(submission.getSpeaker().getEmail(), "jPrime talk proposal",
                             loadMailContentTemplate("submissionContent.html"));
        if (submission.getCoSpeaker() != null) {
            mailFacade.sendEmail(submission.getCoSpeaker().getEmail(), "jPrime talk proposal",
                                 loadMailContentTemplate("submissionContent.html"));
        }
        mailFacade.sendEmail(JPRIME_CONF_MAIL_ADDRESS, "New talk proposal",
                             prepareNewSubmissionContent(submission, loadMailContentTemplate("newSubmission.html")
                             ));

    }

    private String prepareNewSubmissionContent(Submission submission, String template) {
        return template.replace("{session.title}", submission.getTitle())
                       .replace("{session.abstract}", submission.getDescription())
                       .replace("{speaker.name}",
                                submission.getSpeaker().getFirstName() + " " + submission.getSpeaker().getLastName())
                       .replace("{speaker.bio}", submission.getSpeaker().getBio());
    }

}
