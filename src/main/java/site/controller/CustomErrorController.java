package site.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping(value = "/error")
    public void error(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect("/404");
    }

    @RequestMapping(value = "/404")
    public String errorPage(Model model) throws IOException {
        return "/404.jsp";
    }

    @Override
    public String getErrorPath() {
        return "/404";
    }
}