package site.controller;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import site.facade.UserFacade;
import site.model.SessionLevel;
import site.model.Speaker;
import site.model.Submission;

/**
 * @author Ivan St. Ivanov
 */
public class AbstractCfpController {

    protected Model buildCfpFormModel(Model model, Submission submission) {
        model.addAttribute("submission", submission);
        model.addAttribute("levels", SessionLevel.values());
        return model;
    }

    protected void saveSubmission(Submission submission, MultipartFile file, UserFacade userFacade) {
        fixTwitterHandle(submission.getSpeaker());

        Speaker existingSpeaker = userFacade.findSpeaker(submission.getSpeaker().getEmail());
        if(existingSpeaker != null){
            //replace speaker
            submission.setSpeaker(existingSpeaker);
        } else if (!file.isEmpty()) { //new speaker.. file is required
            try {
                byte[] bytes = file.getBytes();
                submission.getSpeaker().setPicture(bytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        userFacade.submitTalk(submission);
    }

    protected Speaker fixTwitterHandle(Speaker speaker) {
        String twitterHandle = speaker.getTwitter();
        if (twitterHandle != null && twitterHandle.startsWith("@")) {
            speaker.setTwitter(twitterHandle.substring(1));
        }

        return speaker;
    }

}
