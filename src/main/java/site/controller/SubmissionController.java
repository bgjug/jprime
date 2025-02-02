package site.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import site.facade.AdminService;
import site.facade.BranchService;
import site.facade.CSVService;
import site.facade.MailService;
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

    static final String ADMIN_SUBMISSION_VIEW_JSP = "admin/submission/view";
    static final String ADMIN_SUBMISSION_EDIT_JSP = "admin/submission/edit";
    public static final String REDIRECT = "redirect:";

    @Autowired
    private AdminService adminFacade;

    @Autowired
    private CSVService csvFacade;

    @Autowired
    private BranchService branchService;

    @Autowired
    private MailService mailService;

    @GetMapping("/view/all")
    public String listAllSubmissions(Model model, Pageable pageable) {
        Page<Submission> submissions = adminFacade.findAllSubmissions(pageable);
        model.addAttribute("submissions", submissions.getContent());
        model.addAttribute("number", submissions.getNumber());
        model.addAttribute("totalPages", submissions.getTotalPages());
        model.addAttribute("path", "/all");
        return ADMIN_SUBMISSION_VIEW_JSP;
    }

    @GetMapping("/view/{year}")
    public String listSubmissions(Model model, Pageable pageable, @PathVariable String year) {
        Branch branch = branchService.findBranchByYear(Integer.parseInt(year));
        return listSubmissionsForBranch(model, pageable, branch);
    }

    @GetMapping("/view")
    public String listSubmissions(Model model, Pageable pageable) {
        return listSubmissionsForBranch(model, pageable, branchService.getCurrentBranch());
    }

    private String listSubmissionsForBranch(Model model, Pageable pageable, Branch branch) {
        Page<Submission> submissions = adminFacade.findAllSubmissionsForBranch(branch, pageable);
        model.addAttribute("submissions", submissions.getContent());
        model.addAttribute("number", submissions.getNumber());
        model.addAttribute("totalPages", submissions.getTotalPages());
        model.addAttribute("path", "");
        model.addAttribute("isCurrentYearOnly", Boolean.TRUE);
        return ADMIN_SUBMISSION_VIEW_JSP;
    }

    @GetMapping("/add")
    public String showSubmissionForm(Model model) {
        model.addAttribute("branches", branchService.allBranches());
        updateCfpModel(model, new Submission(branchService.getCurrentBranch()));
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

        return REDIRECT + listSubmissions(model, pageable);
    }

    @GetMapping(path = "/exportCSV", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void exportSubmissionsToCSV(HttpServletResponse response) {
        List<Submission> findAllSubmittedSubmissionsForCurrentBranch =
            adminFacade.findAllSubmittedSubmissionsForCurrentBranch();
        File submissionsCSVFile;
        try {
            submissionsCSVFile = csvFacade.exportSubmissions(findAllSubmittedSubmissionsForCurrentBranch);
        } catch (IOException e) {
            logger.error("Could not create submissions file", e);
            return;
        }

        try (InputStream inputStream = new FileInputStream(submissionsCSVFile)) {
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader("Content-Disposition", "attachment; filename=" + submissionsCSVFile.getName());
            response.setHeader("Content-Length", String.valueOf(submissionsCSVFile.length()));
            FileCopyUtils.copy(inputStream, response.getOutputStream());
            if (!submissionsCSVFile.delete()) {
                logger.warn("Submission file: {} cannot be deleted", submissionsCSVFile::getAbsolutePath);
            }
        } catch (IOException e) {
            logger.error(() -> "Could not download file: " + submissionsCSVFile.getAbsolutePath(), e);
        }
    }

    private void sendEmails(Submission submission, String fileName) throws IOException, MessagingException {
        final String mailSubject = "Your jPrime talk proposal status";
        String messageText = buildMessage(submission, fileName);
        mailService.sendEmail(submission.getSpeaker().getEmail(), mailSubject, messageText);
        if (submission.getCoSpeaker() != null) {
            final String messageForCoSpeaker = messageText.replace(submission.getSpeaker().getFirstName(),
                submission.getCoSpeaker().getFirstName());
            mailService.sendEmail(submission.getCoSpeaker().getEmail(), mailSubject, messageForCoSpeaker);
        }
    }

    private String buildMessage(Submission submission, String fileName) throws IOException {
        String messageText = resourceAsString(fileName);
        messageText = messageText.replace("{speaker.firstName}", submission.getSpeaker().getFirstName());
        messageText = messageText.replace("{submission.title}", submission.getTitle());
        messageText = messageText.replace("{submission.year}",
            Integer.toString(branchService.getCurrentBranch().getYear()));
        return messageText;
    }

    @GetMapping("/edit/{submissionId}")
    public String editSubmissionForm(@PathVariable Long submissionId, Model model,
        @RequestParam(required = false) String sourcePage) {
        updateCfpModel(model, adminFacade.findOneSubmission(submissionId));
        model.addAttribute("sourcePage", sourcePage);
        return ADMIN_SUBMISSION_EDIT_JSP;
    }

    @PostMapping("/edit")
    public String editSubmission(@Valid final Submission submission, BindingResult bindingResult,
        @RequestParam MultipartFile speakerImage, @RequestParam MultipartFile coSpeakerImage,
        @RequestParam(required = false) String sourcePage, Model model) {
        if (bindingResult.hasErrors()) {
            updateCfpModel(model, submission);
            return ADMIN_SUBMISSION_EDIT_JSP;
        }

        String result = validateAndUpdateSpeaker(bindingResult, submission.getSpeaker(), "speaker",
            submission::setSpeaker, () -> {
                updateCfpModel(model, submission);
                return ADMIN_SUBMISSION_EDIT_JSP;
            });
        if (result != null) {
            return result;
        }
        if (hasCoSpeaker(submission)) {
            result = validateAndUpdateSpeaker(bindingResult, submission.getCoSpeaker(), "coSpeaker",
                submission::setCoSpeaker, () -> {
                    updateCfpModel(model, submission);
                    return ADMIN_SUBMISSION_EDIT_JSP;
                });
            if (result != null) {
                return result;
            }
        }

        submission.setBranch(branchService.getCurrentBranch());

        try {
            saveSubmission(submission, speakerImage, coSpeakerImage);
        } catch (Exception e) {
            logger.error("Can't save the submission", e);
            updateCfpModel(model, submission);
            return ADMIN_SUBMISSION_EDIT_JSP;
        }

        return REDIRECT + (StringUtils.isEmpty(sourcePage) ? "/admin/submission/view" : sourcePage);
    }
}
