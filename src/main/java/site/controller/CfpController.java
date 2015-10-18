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

import site.config.Globals;
import site.facade.MailService;
import site.facade.UserService;
import site.model.Submission;

import javax.validation.Valid;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Ivan St. Ivanov
 */
@Controller
public class CfpController extends AbstractCfpController {

    private static final Logger logger = Logger.getLogger(CfpController.class);

    public static final String CFP_OPEN_JSP = "/proposal.jsp";
    public static final String CFP_CLOSED_JSP = "/cfp-closed.jsp";

    public static final String JPRIME_CONF_MAIL_ADDRESS = "conference@jprime.io";

    @Autowired
    @Qualifier(UserService.NAME)
    private UserService userFacade;

    @Autowired
    @Qualifier(MailService.NAME)
    private MailService mailFacade;

    @RequestMapping(value = "/cfp", method = RequestMethod.GET)
    public String submissionForm(Model model) {
    	model.addAttribute("tags", userFacade.findAllTags());
        buildCfpFormModel(model, new Submission());
        return Globals.CFP;
    }

    @RequestMapping(value = "/cfp", method = RequestMethod.POST)
    public String submitSession(@Valid final Submission submission, BindingResult bindingResult, @RequestParam("file") MultipartFile file) {
        if (bindingResult.hasErrors()) {
            return "/cfp";
        }
        saveSubmission(submission, file, userFacade);
        try {
            mailFacade.sendEmail(submission.getSpeaker().getEmail(), "jPrime talk proposal",
                    loadMailContentTemplate("submissionContent.html"));
            mailFacade.sendEmail(JPRIME_CONF_MAIL_ADDRESS, "New talk proposal",
                    prepareNewSubmissionContent(submission, loadMailContentTemplate("newSubmission.html")
            ));
        } catch (Exception e) {
            logger.error("Could not send confirmation email", e);
        }

        return "redirect:/";
    }

    private String loadMailContentTemplate(String templateFileName)
            throws IOException, URISyntaxException {
        return new String(Files.readAllBytes(Paths.get(getClass().getResource("/" + templateFileName).toURI())));
    }

    private String prepareNewSubmissionContent(Submission submission, String template) {
        return template.replace("{session.title}", submission.getTitle())
                        .replace("{session.abstract}", submission.getDescription())
                        .replace("{speaker.name}", submission.getSpeaker().getFirstName() + " " + submission.getSpeaker().getLastName())
                        .replace("{speaker.bio}", submission.getSpeaker().getBio());
    }

}
