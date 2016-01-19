package site.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import site.facade.AdminService;
import site.model.Branch;
import site.model.Registrant;
import site.model.Visitor;
import site.model.VisitorStatus;

import javax.validation.Valid;

/**
 * @author Ivan St. Ivanov
 */
@Controller()
@RequestMapping(value = "/admin/registrant")
public class AdminRegistrantController {

    public static final String REGISTRANT_VIEW_JSP = "/admin/registrant/view.jsp";
    public static final String REGISTRANT_EDIT_JSP = "/admin/registrant/edit.jsp";

    @Autowired
    @Qualifier(AdminService.NAME)
    private AdminService adminFacade;

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
}
