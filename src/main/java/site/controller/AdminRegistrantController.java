package site.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import site.config.Globals;
import site.facade.AdminService;
import site.facade.MailService;
import site.model.Branch;
import site.model.Registrant;
import site.model.Visitor;
import site.model.VisitorStatus;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Ivan St. Ivanov
 */
@Controller()
@RequestMapping(value = "/admin/registrant")
public class AdminRegistrantController {

    private static final Logger logger = LogManager.getLogger(AdminRegistrantController.class);

    public static final String REGISTRANT_VIEW_JSP = "/admin/registrant/view.jsp";
    public static final String REGISTRANT_EDIT_JSP = "/admin/registrant/edit.jsp";

    @Autowired
    @Qualifier(AdminService.NAME)
    private AdminService adminFacade;

    @Autowired
    @Lazy
    private MailService mailFacade;

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String viewRegistrants(Model model) {
        model.addAttribute("registrants", adminFacade.findAllRegistrants());
        return REGISTRANT_VIEW_JSP;
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String getNewRegistrantForm(Model model) {
        model.addAttribute("registrant", new Registrant());
        model.addAttribute("paymentTypes", Registrant.PaymentType.values());
        model.addAttribute("branches", Branch.values());
        return REGISTRANT_EDIT_JSP;
    }

    @RequestMapping(value = "/edit/{itemId}", method = RequestMethod.GET)
    public String getEditRegistrantForm(@PathVariable("itemId") Long itemId, Model model) {
        model.addAttribute("registrant", adminFacade.findOneRegistrant(itemId));
        model.addAttribute("paymentTypes", Registrant.PaymentType.values());
        model.addAttribute("branches", Branch.values());
        return REGISTRANT_EDIT_JSP;
    }

    @RequestMapping(value = "/{itemId}/addVisitor", method = RequestMethod.GET)
    public String getAddVisitorToRegistrantForm(@PathVariable("itemId") Long itemId, Model model) {
        Visitor visitor = new Visitor();
        visitor.setRegistrant(adminFacade.findOneRegistrant(itemId));
        model.addAttribute("visitor", visitor);
        model.addAttribute("statuses", VisitorStatus.values());
        return AdminVisitorController.VISITOR_EDIT_JSP;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addRegistrant(@Valid final Registrant registrant, BindingResult bindingResult,Model model) {
        if (bindingResult.hasErrors()) {
        	model.addAttribute("branches", Branch.values());
            return REGISTRANT_EDIT_JSP;
        }
        adminFacade.saveRegistrant(registrant);
        return "redirect:/admin/registrant/view";
    }

    @RequestMapping(value = "/remove/{itemId}", method = RequestMethod.GET)
    public String remove(@PathVariable("itemId") Long itemId) {
        adminFacade.deleteRegistrant(itemId);
        return "redirect:/admin/registrant/view";
    }

    @RequestMapping(value = "/send-tickets/{itemId}", method = RequestMethod.GET)
    public String sendTicketsForRegistrant(@PathVariable("itemId") Long itemId, Model model) {
        Registrant registrant = adminFacade.findOneRegistrant(itemId);

        List<Visitor> visitors = registrant.getVisitors();
        if (visitors != null && visitors.size() > 0) {
            for (Visitor visitor : visitors) {
                if (visitor.getStatus() != null && visitor.getStatus() != VisitorStatus.REQUESTING && visitor.getEmail() != null && !visitor.getEmail().isEmpty()) {
                    try {
                        mailFacade.sendEmail(registrant.getEmail(), "jPrime " + Globals.CURRENT_BRANCH + " Conference ticket !",
                                             getTicketEmailBody(visitor));
                    } catch (MessagingException e) {
                        logger.error("Could not send invoice email", e);
                    }
                }
            }
        }

        return "redirect:/admin/registrant/view";
    }

    private String getTicketEmailBody(Visitor visitor) {
        return "<strong>DON'T PANIC !!!</string> jPrime " + Globals.CURRENT_BRANCH + " is here !<br/>"
                        + "Like every other year, the ticket is your NAME! Which in this case should be " + visitor.getName() + ". This is all you need, no printing (we care about environment).<br/> "
                        + "<br>So why is this mail then? <br/> Each and every year we are asked for tickets, mostly because there are a lot of people coming for the first time. This is the main reason why we send you this email. So to all: DON'T WORRY, DON'T PANIC you have a ticket!<br/>"
                        + "The registration is open on the first day morning. We would advice you to come early.<br/>"
                        + "Some other information : <br/>"
                        + "Location :  <a href='https://jprime.io/venue' target='_blank'>Sofia Tech Park</a><br/>"
                        + "Your name (again) : " + visitor.getName() +"<br/>"
                        + "Your ID : " + visitor.getId() +" <br/>"
                        + "The conference website : <a href='https://jprime.io/' target='_blank'>https://jprime.io</a> <br/>"
                        + "<br/>"
                        + "Wait, why do I need the ID? (Yep, this is the database ID...at least since last full schema drop... ops \uD83D\uDE09 ) - It will be used on the raffle! It should be printed on your badges as well, but just in case you can have it also here \uD83D\uDE09\n"
                        + "And finally, if for some reason you cannot come, a friend of yours or a colleague or someone can use your ticket (and again ticket means your name). Also if you are privacy fanatic, you can use a nickname :+)"
                        + "<br/><br/>"
                        + "See you at jPrime !<br/>"
                        + "The Gang of 6 of the Bulgarian Java User Group!";

    }
}
