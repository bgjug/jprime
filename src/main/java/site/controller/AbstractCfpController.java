package site.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import site.facade.ThumbnailService;
import site.facade.UserService;
import site.model.Branch;
import site.model.SessionLevel;
import site.model.Speaker;
import site.model.Submission;

/**
 * @author Ivan St. Ivanov
 */
public class AbstractCfpController {

	@Autowired
	@Qualifier(ThumbnailService.NAME)
	private ThumbnailService thumbnailService;
	
    protected Model buildCfpFormModel(Model model, Submission submission) {
        model.addAttribute("submission", submission);
        model.addAttribute("levels", SessionLevel.values());
        model.addAttribute("branches", Branch.values());
        return model;
    }

    protected void saveSubmission(Submission submission, MultipartFile file, UserService userFacade) {
        fixTwitterHandle(submission.getSpeaker());

        Speaker existingSpeaker = userFacade.findSpeaker(submission.getSpeaker().getEmail());
        if(existingSpeaker != null){
            //replace speaker
            submission.setSpeaker(existingSpeaker);
        } else if (!file.isEmpty()) { //new speaker.. file is required
            try {
                byte[] bytes = file.getBytes();
                submission.getSpeaker().setPicture(thumbnailService.thumbImage(bytes, 280, 326));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        submission.getSpeaker().setBranch(Branch.YEAR_2016);
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
