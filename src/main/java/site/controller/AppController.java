package site.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for Flutter PWA app
 */
@Controller
public class AppController {

    @GetMapping("/app")
    public String redirectToApp() {
        return "redirect:/app/index.html";
    }
}

