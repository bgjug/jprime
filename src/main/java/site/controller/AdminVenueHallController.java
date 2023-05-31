package site.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import site.facade.AdminService;
import site.model.VenueHall;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

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

    @GetMapping("/view")
    public String viewVenueHalls(Model model) {
        model.addAttribute("halls", adminFacade.findAllVenueHalls());
        return HALLS_VIEW_JSP;
    }

    @GetMapping("/add")
    public String getNewVenueHallForm(Model model) {
        return getModelAndView(model, new VenueHall());
    }

    @GetMapping("/edit/{itemId}")
    public String getEditVenueHallForm(@PathVariable Long itemId, Model model) {
        return getModelAndView(model, adminFacade.findOneVenueHall(itemId));
    }

    private String getModelAndView(Model model, VenueHall venueHall) {
        model.addAttribute("hall", venueHall);
        return HALLS_EDIT_JSP;
    }

    @Transactional
        @PostMapping("/add")
    public String addVenueHall(@Valid final VenueHall venueHall, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return HALLS_EDIT_JSP;
        }
        adminFacade.saveVenueHall(venueHall);
        return "redirect:/admin/hall/view";
    }

    @Transactional
        @GetMapping("/remove/{itemId}")
    public String remove(@PathVariable Long itemId, Model model) {
        adminFacade.deleteVenueHall(itemId);
        return "redirect:/admin/hall/view";
    }


}
