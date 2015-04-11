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
public class TicketsController {

    static final String TICKETS_JSP = "/tickets.jsp";
    
    @Autowired
    @Qualifier(UserFacade.NAME)
    private UserFacade userFacade;

    // TODO add mail facade and create a new one: epay facade

    @RequestMapping(value = "/tickets", method = RequestMethod.GET)
    public String submissionForm(Model model) {
    	model.addAttribute("tags", userFacade.findAllTags());
        return TICKETS_JSP;
    }

}
