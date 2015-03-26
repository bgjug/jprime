package site.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Ivan St. Ivanov
 */
@Controller
public class TicketsController {

    static final String TICKETS_JSP = "/tickets.jsp";

    // TODO add mail facade and create a new one: epay facade

    @RequestMapping(value = "/tickets", method = RequestMethod.GET)
    public String submissionForm(Model model) {
        return TICKETS_JSP;
    }

}
