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
import site.model.VenueHall;

import javax.transaction.Transactional;
import javax.validation.Valid;

/**
 * @author Ivan St. Ivanov
 */
@Controller
@RequestMapping("/admin/hall")
public class AdminVenueHallController {

    public static final String HALLS_VIEW_JSP = "/admin/hall/view.jsp";
    public static final String HALLS_EDIT_JSP = "/admin/hall/edit.jsp";

    @Autowired
    @Qualifier(AdminService.NAME)
    private AdminService adminFacade;

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String viewVenueHalls(Model model) {
        model.addAttribute("halls", adminFacade.findAllVenueHalls());
        return HALLS_VIEW_JSP;
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String getNewVenueHallForm(Model model) {
        return getModelAndView(model, new VenueHall());
    }

    @RequestMapping(value = "/edit/{itemId}", method = RequestMethod.GET)
    public String getEditVenueHallForm(@PathVariable("itemId") Long itemId, Model model) {
        return getModelAndView(model, adminFacade.findOneVenueHall(itemId));
    }

    private String getModelAndView(Model model, VenueHall venueHall) {
        model.addAttribute("hall", venueHall);
        return HALLS_EDIT_JSP;
    }

    @Transactional
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addVenueHall(@Valid final VenueHall venueHall, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return HALLS_EDIT_JSP;
        }
        adminFacade.saveVenueHall(venueHall);
        return "redirect:/admin/hall/view";
    }

    @Transactional
    @RequestMapping(value = "/remove/{itemId}", method = RequestMethod.GET)
    public String remove(@PathVariable("itemId") Long itemId, Model model) {
        adminFacade.deleteVenueHall(itemId);
        return "redirect:/admin/hall/view";
    }


}
