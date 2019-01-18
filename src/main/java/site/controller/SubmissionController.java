package site.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import site.config.Globals;
import site.facade.AdminService;
import site.facade.CSVService;
import site.model.Branch;
import site.model.Submission;

import static site.controller.ResourceAsString.resourceAsString;

/**
 * @author Ivan St. Ivanov
 */
@Controller
@RequestMapping("/admin/submission")
public class SubmissionController extends AbstractCfpController {

    private static final Logger logger = LogManager.getLogger(SubmissionController.class);

    static final String ADMIN_SUBMISSION_VIEW_JSP = "/admin/submission/view.jsp";
    static final String ADMIN_SUBMISSION_EDIT_JSP = "/admin/submission/edit.jsp";
    public static final String REDIRECT = "redirect:";
    public static final String PRODUCES_TYPE = "application/octet-stream";

    @Autowired
    @Qualifier(AdminService.NAME)
    private AdminService adminFacade;

    @Autowired
    @Qualifier(CSVService.NAME)
    private CSVService csvFacade;

    @RequestMapping(value = "/view/all", method = RequestMethod.GET)
    public String listAllSubmissions(Model model, Pageable pageable) {
        Page<Submission> submissions = adminFacade.findAllSubmissions(pageable);
        model.addAttribute("submissions", submissions);
        model.addAttribute("path", "all");
        return ADMIN_SUBMISSION_VIEW_JSP;
    }

    @RequestMapping(value = "/view/{year}", method = RequestMethod.GET)
    public String listSubmissions(Model model, Pageable pageable, @PathVariable String year) {
    	Branch branch = Branch.valueOfYear(year);
        return listSubmissionsForBranch(model, pageable, branch);
    }

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String listSubmissions(Model model, Pageable pageable) {
        return listSubmissionsForBranch(model, pageable, Globals.CURRENT_BRANCH);
    }

    private String listSubmissionsForBranch(Model model, Pageable pageable, Branch branch) {
        Page<Submission> submissions = adminFacade.findAllSubmissionsForBranch(branch, pageable);
        model.addAttribute("submissions", submissions);
        model.addAttribute("path", "");
        model.addAttribute("isCurrentYearOnly", Boolean.TRUE);
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

    @RequestMapping(value = "/notify/{submissionId}", method = RequestMethod.GET)
    public String notify(@PathVariable("submissionId") Long submissionId) {
        Submission submission = adminFacade.findOneSubmission(submissionId);
        try {
            sendNotificationEmails(submission);
        } catch (Exception e) {
            logger.error("Could not send notification emails", e);
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

    @RequestMapping(value = "/exportCSV", method = RequestMethod.GET, produces = PRODUCES_TYPE)
    public@ResponseBody void exportSubmissionsToCSV(HttpServletResponse response) {
    	List<Submission> findAllSubmittedSubmissionsForCurrentBranch = adminFacade.findAllSubmitedSubmissionsForCurrentBranch();
    	File submissionsCSVFile;
		try {
			submissionsCSVFile = csvFacade.exportSubmissions(findAllSubmittedSubmissionsForCurrentBranch);
		} catch (IOException e) {
			logger.error("Could not create submissions file", e);
			return;
		}

		try(InputStream inputStream = new FileInputStream(submissionsCSVFile) ){
	        response.setContentType(PRODUCES_TYPE);
	        response.setHeader("Content-Disposition", "attachment; filename=" + submissionsCSVFile.getName());
	        response.setHeader("Content-Length", String.valueOf(submissionsCSVFile.length()));
	        FileCopyUtils.copy(inputStream, response.getOutputStream());
	        if(!submissionsCSVFile.delete()) {
	        	logger.warn("Submission file: " + submissionsCSVFile.getAbsolutePath() + " cannot be deleted");
	        }
		} catch (IOException e) {
			logger.error("Could not download file: " + submissionsCSVFile.getAbsolutePath(), e);
		}
    }

    private void sendEmails(Submission submission, String fileName)
            throws IOException, MessagingException {
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
            throws IOException {
        String messageText = resourceAsString(fileName);
        messageText = messageText.replace("{speaker.firstName}", submission.getSpeaker().getFirstName());
        messageText = messageText.replace("{submission.title}", submission.getTitle());
        messageText = messageText.replace("{submission.year}", Globals.CURRENT_BRANCH.toString());
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
