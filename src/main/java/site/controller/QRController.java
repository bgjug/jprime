package site.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import site.facade.UserService;
import site.model.Visitor;

@Controller()
@RequestMapping(value = "/qr")
public class QRController {
    public static final String QR_JSP = "/qr.jsp";
    public static final String REG_PRESENCE_JSP = "/reg-presence.jsp";
    public static final String SET_PRESENT_SUCCESSFUL_JSP = "/set-present-successful.jsp";

    @Autowired
    UserService userService;

    @GetMapping
    public String qr(){
        return QR_JSP;
    }

    @RequestMapping(value = "/tuk", method = RequestMethod.GET)
    public String registerPresence(Model model) {
        model.addAttribute(new Visitor());
        return REG_PRESENCE_JSP;
    }

    @RequestMapping(value = "/tuk", method = RequestMethod.POST)
    public String setIsPresent(Visitor visitor, Model model) {
        boolean updateSuccessful = false;

        if (isNameFilled(visitor) && isCompanyFilled(visitor)) {
            updateSuccessful = userService.setPresentByNameIgnoreCaseAndCompanyIgnoreCase(visitor);
        } else if (isNameFilled(visitor)) {
            updateSuccessful = userService.setPresentByNameIgnoreCase(visitor);
        }

        if (!updateSuccessful) {
            model.addAttribute("message", "Your name has not been filled properly since ... well I have no idea - you are welcome to debug it ;))) ");
            return REG_PRESENCE_JSP;
        }

        return SET_PRESENT_SUCCESSFUL_JSP;
    }

    private boolean isCompanyFilled(Visitor visitor){
        return visitor.getCompany() != null && !StringUtils.isEmpty(visitor.getCompany());
    }

    private boolean isNameFilled(Visitor visitor){
        return visitor.getName() != null && !StringUtils.isEmpty(visitor.getName());
    }
}
