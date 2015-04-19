package site.controller.util;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import site.facade.UserFacade;
import site.model.SessionLevel;
import site.model.Submission;

import static site.controller.util.Utils.fixTwitterHandle;

/**
 * @author Ivan St. Ivanov
 */
public class CfpControllersHelper {

    public static Model buildCfpFormModel(Model model, Submission submission) {
        model.addAttribute("submission", submission);
        model.addAttribute("levels", SessionLevel.values());
        return model;
    }

    public static void saveSubmission(Submission submission, MultipartFile file, UserFacade userFacade) {
        fixTwitterHandle(submission.getSpeaker());
        if(!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                submission.getSpeaker().setPicture(bytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        userFacade.submitTalk(submission);
    }

}
