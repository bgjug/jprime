package site.controller;

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

/**
 * @author Ivan St. Ivanov
 */
public class AbstractCfpController {

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
        model.addAttribute("sessionTypes", Arrays.stream(SessionType.values()).collect(Collectors.toMap(Function.identity(), SessionType::toString)));
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
        Speaker existingSpeaker = userFacade.findSpeaker(speaker.getEmail());
        if (existingSpeaker != null) {
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
        if (!image.isEmpty()) {
            try {
                byte[] bytes = image.getBytes();
                speaker.setPicture(thumbnailService.thumbImage(bytes, 280, 326));
            } catch (Exception e) {
                e.printStackTrace();
            }
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

}
