package site.controller;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import site.facade.BranchService;

@Controller
public class CustomErrorController implements ErrorController {

    private final BranchService branchService;

    public CustomErrorController(BranchService branchService) {
        this.branchService = branchService;
    }

    @GetMapping("/error")
    public void error(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect("/404");
    }

    @GetMapping("/404")
    public String errorPage(Model model) {
        if (branchService.getCurrentBranch() == null) {
            model.addAttribute("jprime_year", -1);
        } else {
            model.addAttribute("jprime_year", branchService.getCurrentBranch().getStartDate().getYear());
        }
        return "404";
    }
}