package site.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import site.facade.MailFacade;
import site.facade.UserFacade;
import site.model.SessionLevel;
import site.model.Submission;
import site.model.SubmissionStatus;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Ivan St. Ivanov
 */
@Controller
public class CfpController {

    private static final Logger logger = Logger.getLogger(CfpController.class);

    static final String PROPOSAL_JSP = "/proposal.jsp";

    @Autowired
    @Qualifier(UserFacade.NAME)
    private UserFacade userFacade;

    @Autowired
    @Qualifier(MailFacade.NAME)
    private MailFacade mailFacade;

    @RequestMapping(value = "/cfp", method = RequestMethod.GET)
    public String submissionForm(Model model) {
        model.addAttribute("submission", new Submission());
        model.addAttribute("levels", SessionLevel.values());
        return PROPOSAL_JSP;
    }

    @RequestMapping(value = "/cfp", method = RequestMethod.POST)
    public String submitSession(@Valid final Submission submission, BindingResult bindingResult, @RequestParam("file") MultipartFile file) {
        if (bindingResult.hasErrors()) {
            return "/cfp";
        }
        submission.setStatus(SubmissionStatus.SUBMITTED);
        if(!file.isEmpty()){
            try {
                byte[] bytes = file.getBytes();
                submission.getSpeaker().setPicture(bytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        userFacade.submitTalk(submission);
        try {
            mailFacade.sendEmail(submission.getSpeaker().getEmail(), "jPrime talk proposal",
                    new String(Files.readAllBytes(Paths.get(getClass().getResource("/submissionContent.html").toURI()))));
        } catch (MessagingException | IOException | URISyntaxException e) {
            logger.error("Could not send confirmation email", e);
        }

        return "redirect:/";
    }

}
