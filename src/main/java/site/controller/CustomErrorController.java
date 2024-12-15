package site.controller;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import site.config.Globals;

@Controller
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    public void error(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect("/404");
    }

    @GetMapping("/404")
    public String errorPage(Model model) throws IOException {
        model.addAttribute("jprime_year", Globals.CURRENT_BRANCH.getStartDate().getYear());
        return "404";
    }
}