package site.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import site.facade.AdminService;
import site.model.Submission;

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
@RequestMapping("/admin/submission")
public class SubmissionController extends AbstractCfpController {

    private static final Logger logger = Logger.getLogger(SubmissionController.class);

    static final String ADMIN_SUBMISSION_VIEW_JSP = "/admin/submission/view.jsp";
    static final String ADMIN_SUBMISSION_EDIT_JSP = "/admin/submission/edit.jsp";
    public static final String REDIRECT = "redirect:";

    @Autowired
    @Qualifier(AdminService.NAME)
    private AdminService adminFacade;
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String listSubmissions(Model model, Pageable pageable) {
        Page<Submission> submissions = adminFacade.findAllSubmissions(pageable);
        model.addAttribute("submissions", submissions);
        return ADMIN_SUBMISSION_VIEW_JSP;
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String showSubmissionForm(Model model) {
        buildCfpFormModel(model, new Submission());
        return ADMIN_SUBMISSION_EDIT_JSP;
    }

    @RequestMapping(value = "/accept/{submissionId}", method = RequestMethod.GET)
    public String accept(@PathVariable("submissionId") Long submissionId) {
        Submission submission = adminFacade.findOneSubmission(submissionId);
        adminFacade.acceptSubmission(submission);
        try {
            sendEmails(submission, "/acceptSubmission.html");
        } catch (Exception e) {
            logger.error("Could not send accept email", e);
        }
        return REDIRECT + "/admin/submission/view";
    }

    @RequestMapping(value = "/reject/{submissionId}", method = RequestMethod.GET)
    public String reject(@PathVariable("submissionId") Long submissionId) {
        Submission submission = adminFacade.findOneSubmission(submissionId);
        adminFacade.rejectSubmission(submission);
        try {
            sendEmails(submission, "/rejectSubmission.html");
        } catch (Exception e) {
            logger.error("Could not send rejection email", e);
        }
        return REDIRECT + "/admin/submission/view";
    }

    private void sendEmails(Submission submission, String fileName)
            throws IOException, URISyntaxException, MessagingException {
        final String mailSubject = "Your jPrime talk proposal status";
        String messageText = buildMessage(submission, fileName);
        mailFacade.sendEmail(submission.getSpeaker().getEmail(), mailSubject, messageText);
        if (submission.getCoSpeaker() != null) {
            final String messageForCoSpeaker = messageText.replace(
                    submission.getSpeaker().getFirstName(),
                    submission.getCoSpeaker().getFirstName());
            mailFacade.sendEmail(submission.getCoSpeaker().getEmail(), mailSubject, messageForCoSpeaker);
        }
    }

    private String buildMessage(Submission submission, String fileName)
            throws IOException, URISyntaxException {
        String messageText = new String(Files.readAllBytes(Paths.get(getClass().getResource(
                fileName).toURI())));
        messageText = messageText.replace("{speaker.firstName}", submission.getSpeaker().getFirstName());
        messageText = messageText.replace("{submission.title}", submission.getTitle());
        return messageText;
    }

    @RequestMapping(value = "/edit/{submissionId}", method = RequestMethod.GET)
    public String editSubmissionForm(@PathVariable("submissionId") Long submissionId, Model model) {
        buildCfpFormModel(model, adminFacade.findOneSubmission(submissionId));
        return ADMIN_SUBMISSION_EDIT_JSP;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String editSubmission(@Valid final Submission submission,
            BindingResult bindingResult,
            @RequestParam("speakerImage") MultipartFile speakerImage,
            @RequestParam("coSpeakerImage") MultipartFile coSpeakerImage,
            Model model) {
        if (bindingResult.hasErrors()) {
        	buildCfpFormModel(model, submission);
        	return ADMIN_SUBMISSION_EDIT_JSP;
        }
        saveSubmission(submission, speakerImage, coSpeakerImage);

        return REDIRECT + "/admin/submission/view";
    }
}
