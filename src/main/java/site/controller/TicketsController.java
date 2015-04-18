package site.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.ResponseBody;
import site.facade.UserFacade;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Mihail
 */
@Controller
public class TicketsController {

    static final String TICKETS_JSP = "/tickets.jsp";
    
    @Autowired
    @Qualifier(UserFacade.NAME)
    private UserFacade userFacade;

    // TODO add mail facade and create a new one: epay facade

    @RequestMapping(value = "/tickets", method = RequestMethod.GET)
    public String submissionForm(Model model) {
    	model.addAttribute("tags", userFacade.findAllTags());
        return TICKETS_JSP;
    }

    /**
     * Receiving data from epay.bg
     */
    @RequestMapping(value = "/tickets/from.epay", method = RequestMethod.POST)
    @ResponseBody//we return the string literal
    public String receiveFromEpay(HttpServletRequest request) {
        Map<String, String[]> parameters = request.getParameterMap();

        for (String key : parameters.keySet()) {
            System.out.print(key);
            String[] vals = parameters.get(key);
            for (String val : vals)
                System.out.println(" -> " + val);
        }
//        for(Map.Entry entry: model.asMap().entrySet()) {
//            System.out.println("FROM EPAY:"+entry.getKey()+ " " + entry.getValue() );
//        }
        return "OK";
    }

}
