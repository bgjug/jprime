package site.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Teodor Tunev
 */
@Controller
public class PWAController {

    @RequestMapping(value={"/pwa", "/pwa/**"})
    public String getPwaPage() {
        return "/pwa.jsp";
    }
}
