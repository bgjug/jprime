package site.controller;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import site.facade.AdminService;
import site.facade.BackgroundJobService;
import site.facade.TicketService;
import site.model.Branch;
import site.model.Registrant;
import site.model.Visitor;
import site.model.VisitorStatus;

import jakarta.validation.Valid;
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
    private TicketService ticketService;

    @Autowired
    private BackgroundJobService jobService;

    @GetMapping("/view")
    public String viewRegistrants(Model model) {
        model.addAttribute("registrants", adminFacade.findAllRegistrants());
        return REGISTRANT_VIEW_JSP;
    }

    @GetMapping("/add")
    public String getNewRegistrantForm(Model model) {
        model.addAttribute("registrant", new Registrant());
        model.addAttribute("paymentTypes", Registrant.PaymentType.values());
        model.addAttribute("branches", Branch.values());
        return REGISTRANT_EDIT_JSP;
    }

    @GetMapping("/edit/{itemId}")
    public String getEditRegistrantForm(@PathVariable Long itemId, Model model) {
        model.addAttribute("registrant", adminFacade.findOneRegistrant(itemId));
        model.addAttribute("paymentTypes", Registrant.PaymentType.values());
        model.addAttribute("branches", Branch.values());
        return REGISTRANT_EDIT_JSP;
    }

    @GetMapping("/{itemId}/addVisitor")
    public String getAddVisitorToRegistrantForm(@PathVariable Long itemId, Model model) {
        Visitor visitor = new Visitor();
        visitor.setRegistrant(adminFacade.findOneRegistrant(itemId));
        model.addAttribute("visitor", visitor);
        model.addAttribute("statuses", VisitorStatus.values());
        return AdminVisitorController.VISITOR_EDIT_JSP;
    }

    @PostMapping("/add")
    public String addRegistrant(@Valid final Registrant registrant, BindingResult bindingResult,
        Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("branches", Branch.values());
            return REGISTRANT_EDIT_JSP;
        }
        adminFacade.saveRegistrant(registrant);
        return "redirect:/admin/registrant/view";
    }

    @GetMapping(value = "/remove/{itemId}")
    public String remove(@PathVariable Long itemId) {
        adminFacade.deleteRegistrant(itemId);
        return "redirect:/admin/registrant/view";
    }

    @GetMapping(value = "/send-tickets/{itemId}")
    public String sendTicketsForRegistrant(@PathVariable Long itemId, Model model) {
        Registrant registrant = adminFacade.findOneRegistrant(itemId);

        List<Visitor> visitors = registrant.getVisitors();
        if (CollectionUtils.isEmpty(visitors)) {
            return "redirect:/admin/registrant/view";
        }

        if (visitors.size() > 2) {
            String jobId =
                jobService.createBackgroundJob("Sending tickets to registrant " + registrant.getName());
            jobService.runJob(jobId, visitors, this::sendTicket, this::emailErrorLog);
            return "redirect:/admin/jobs/view";
        } else {
            visitors.forEach(this::sendTicket);
        }

        return "redirect:/admin/registrant/view";
    }

    private String emailErrorLog(Visitor visitor) {
        return visitor.anyEmail();
    }

    private boolean sendTicket(Visitor visitor) {
        if (StringUtils.isEmpty(visitor.anyEmail())) {
            return false;
        }

        List<Pair<Visitor, Boolean>> result = ticketService.generateAndSendTicketEmail(visitor.anyEmail(),
            List.of(visitor));

        return result.stream().map(Pair::getValue).reduce(true, (a, b) -> a && b);
    }
}
