package site.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping({"/error", "/404"})
    public String errorPage(HttpServletRequest request, HttpServletResponse response, Model model) {
        Integer errorStatus = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int status = errorStatus != null ? errorStatus : HttpServletResponse.SC_NOT_FOUND;
        response.setStatus(status);
        model.addAttribute("errorStatus", status);
        return "404";
    }
}
