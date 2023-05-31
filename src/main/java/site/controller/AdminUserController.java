package site.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import site.facade.AdminService;
import site.model.User;

import jakarta.validation.Valid;

/**
 * @author Ivan St. Ivanov
 */
@Controller()
@RequestMapping(value = "/admin/user")
public class AdminUserController {

    public static final String USER_VIEW_JSP = "/admin/user/view.jsp";
    public static final String USER_EDIT_JSP = "/admin/user/edit.jsp";

    @Autowired
    @Qualifier(AdminService.NAME)
    private AdminService adminFacade;

    @GetMapping("/view")
    public String viewUsers(Model model, Pageable pageable) {
        model.addAttribute("users", adminFacade.findAllUsers(pageable));
        return USER_VIEW_JSP;
    }

    @GetMapping("/add")
    public String getNewUserForm(Model model) {
        model.addAttribute("user", new User());
        return USER_EDIT_JSP;
    }

    @PostMapping("/add")
    public String addUser(@Valid final User user, BindingResult bindingResult,Model model) {
        if (bindingResult.hasErrors()) {
            return USER_EDIT_JSP;
        }
        adminFacade.saveUser(user);
        return "redirect:/admin/user/view";
    }

    @GetMapping("/remove/{itemId}")
    public String remove(@PathVariable Long itemId) {
        adminFacade.deleteUser(itemId);
        return "redirect:/admin/user/view";
    }
}
