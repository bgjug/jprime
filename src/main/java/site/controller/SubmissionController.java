package site.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import site.config.Globals;
import site.facade.AdminService;
import site.facade.CSVService;
import site.model.Branch;
import site.model.Submission;
import site.model.SubmissionStatus;

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

    @GetMapping("/view/all")
    public String listAllSubmissions(Model model, Pageable pageable) {
        Page<Submission> submissions = adminFacade.findAllSubmissions(pageable);
        model.addAttribute("submissions", submissions);
        model.addAttribute("path", "all");
        return ADMIN_SUBMISSION_VIEW_JSP;
    }

    @GetMapping("/view/{year}")
    public String listSubmissions(Model model, Pageable pageable, @PathVariable String year) {
    	Branch branch = Branch.valueOfYear(year);
        return listSubmissionsForBranch(model, pageable, branch);
    }

    @GetMapping("/view")
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

    @GetMapping("/add")
    public String showSubmissionForm(Model model) {
        buildCfpFormModel(model, new Submission());
        return ADMIN_SUBMISSION_EDIT_JSP;
    }

    @GetMapping("/accept/{submissionId}")
    public String accept(Model model, Pageable pageable, @PathVariable Long submissionId) {
        Submission submission = adminFacade.findOneSubmission(submissionId);
        adminFacade.acceptSubmission(submission);
        try {
            sendEmails(submission, "/acceptSubmission.html");
        } catch (Exception e) {
            model.addAttribute("msg", "Could not send accept email");
            logger.error("Could not send accept email", e);
        }
        return listSubmissions(model, pageable);
    }

    @GetMapping("/notify/{submissionId}")
    public String notify(Model model, Pageable pageable, @PathVariable Long submissionId) {
        Submission submission = adminFacade.findOneSubmission(submissionId);
        try {
            sendNotificationEmails(submission);
        } catch (Exception e) {
            model.addAttribute("msg", "Could not send notification emails");
            logger.error("Could not send notification emails", e);
        }
        return listSubmissions(model, pageable);
    }

    @GetMapping("/reject/{submissionId}")
    public String reject(Model model, Pageable pageable, @PathVariable Long submissionId) {
        Submission submission = adminFacade.findOneSubmission(submissionId);
        adminFacade.rejectSubmission(submission);
        try {
            sendEmails(submission, "/rejectSubmission.html");
        } catch (Exception e) {
            model.addAttribute("msg", "Could not send rejection email");
            logger.error("Could not send rejection email", e);
        }
        return listSubmissions(model, pageable);
    }

    @GetMapping("/delete/{submissionId}")
    public String delete(Model model, Pageable pageable, @PathVariable Long submissionId) {
        Submission submission = adminFacade.findOneSubmission(submissionId);
        if (submission.getStatus() != SubmissionStatus.SUBMITTED) {
            model.addAttribute("msg", "Only SUBMITTED submissions can be deleted!");
        } else {
            adminFacade.deleteSubmission(submission);
        }

        return listSubmissions(model, pageable);
    }

    @GetMapping(value = "/exportCSV", produces = PRODUCES_TYPE)
    public@ResponseBody void exportSubmissionsToCSV(HttpServletResponse response) {
    	List<Submission> findAllSubmittedSubmissionsForCurrentBranch = adminFacade.findAllSubmittedSubmissionsForCurrentBranch();
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
	        	logger.warn("Submission file: {} cannot be deleted", submissionsCSVFile::getAbsolutePath);
	        }
		} catch (IOException e) {
            logger.error(() -> "Could not download file: " + submissionsCSVFile.getAbsolutePath(), e);
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

    @GetMapping("/edit/{submissionId}")
    public String editSubmissionForm(@PathVariable Long submissionId, Model model, @RequestParam(required = false) String sourcePage) {
        buildCfpFormModel(model, adminFacade.findOneSubmission(submissionId));
        model.addAttribute("sourcePage", sourcePage);
        return ADMIN_SUBMISSION_EDIT_JSP;
    }

    @PostMapping("/edit")
    public String editSubmission(@Valid final Submission submission,
            BindingResult bindingResult,
            @RequestParam MultipartFile speakerImage,
            @RequestParam MultipartFile coSpeakerImage,
        @RequestParam(required = false) String sourcePage,
            Model model) {
        if (bindingResult.hasErrors()) {
        	buildCfpFormModel(model, submission);
        	return ADMIN_SUBMISSION_EDIT_JSP;
        }
        saveSubmission(submission, speakerImage, coSpeakerImage);

        return REDIRECT + (ObjectUtils.isEmpty(sourcePage) ? "/admin/submission/view" : sourcePage);
    }
}
