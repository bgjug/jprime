package site.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import site.facade.UserFacade;

/**
 * @author Ivan St. Ivanov
 */
@Controller
public class CfpController extends AbstractCfpController {

    static final String CFP_CLOSED_JSP = "/cfp-closed.jsp";

    @Autowired
    @Qualifier(UserFacade.NAME)
    private UserFacade userFacade;

    @RequestMapping(value = "/cfp", method = RequestMethod.GET)
    public String submissionForm(Model model) {
    	model.addAttribute("tags", userFacade.findAllTags());
        return CFP_CLOSED_JSP;
    }

}
