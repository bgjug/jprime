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
import site.model.Submission;

import javax.validation.Valid;
import java.nio.file.Files;
import java.nio.file.Paths;

import static site.controller.util.CfpControllersHelper.buildCfpFormModel;
import static site.controller.util.CfpControllersHelper.saveSubmission;

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
        buildCfpFormModel(model, new Submission());
        return PROPOSAL_JSP;
    }

    @RequestMapping(value = "/cfp", method = RequestMethod.POST)
    public String submitSession(@Valid final Submission submission, BindingResult bindingResult, @RequestParam("file") MultipartFile file) {
        if (bindingResult.hasErrors()) {
            return "/cfp";
        }
        saveSubmission(submission, file, userFacade);
        try {
            mailFacade.sendEmail(submission.getSpeaker().getEmail(), "jPrime talk proposal",
                    new String(Files.readAllBytes(Paths.get(getClass().getResource("/submissionContent.html").toURI()))));
        } catch (Exception e) {
            logger.error("Could not send confirmation email", e);
        }

        return "redirect:/";
    }


}
