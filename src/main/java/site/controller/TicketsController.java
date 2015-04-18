package site.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import site.controller.util.EpayUtil;
import site.facade.RegistrantFacade;
import site.facade.UserFacade;
import site.model.Registrant;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Map;

/**
 * All the epay stuff is in the {@link EpayUtil} class.
 *
 * @author Mihail Stoynov
 */
@Controller
public class TicketsController {


    static final String TICKETS_JSP = "/tickets-intro.jsp";

    @Autowired
    @Qualifier(UserFacade.NAME)
    private UserFacade userFacade;

    @Autowired
    @Qualifier(RegistrantFacade.NAME)
    private RegistrantFacade registrantFacade;

    @RequestMapping(value = "/tickets", method = RequestMethod.GET)
    public String goToRegisterPage(Model model) {
        model.addAttribute("tags", userFacade.findAllTags());
        model.addAttribute("registrant", new Registrant());
        return "/tickets-register.jsp";
    }

    @Transactional
    @RequestMapping(value = "/tickets", method = RequestMethod.POST)
    public String register(Model model, @Valid final Registrant registrant, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return "/tickets-register.jsp";
        }

        Registrant savedRegistrant = registrantFacade.save(registrant);

        model.addAttribute("tags", userFacade.findAllTags());
//        model.addAttribute("registrant", registrant);
        prepareEpay(model, savedRegistrant);
        return "/tickets-buy.jsp";
    }

//    @RequestMapping(value = "/tickets/buy", method = RequestMethod.GET)
    public String prepareEpay(Model model, Registrant registrant) {
        String epayENCODED = EpayUtil.getEpayENCODED(registrant.getVisitors().size(), registrant.getInvoiceNumber());
        String epayCHECKSUM = EpayUtil.getEpayCHECKSUM(epayENCODED);
        model.addAttribute("ENCODED", epayENCODED);
        model.addAttribute("CHECKSUM", epayCHECKSUM);
//        model.addAttribute("facNo", registrant.getFacNo());
        model.addAttribute("epayUrl", EpayUtil.EPAY_URL);

        model.addAttribute("tags", userFacade.findAllTags());
        return "/tickets/buy";
    }

    /**
     * Receiving data from epay.bg
     * @param model
     * @return
     */
    @RequestMapping(value = "/tickets/from.epay", method = RequestMethod.POST)
    public String register(Model model) {
        for(Map.Entry entry: model.asMap().entrySet()) {
            System.out.println("FROM EPAY:"+entry.getKey()+ " " + entry.getValue() );
        }
        return "";
    }


    @RequestMapping(value = "/tickets/result/{r}", method = RequestMethod.GET)
    public String result(@PathVariable("r") final String r, Model model) {
        model.addAttribute("result", r.equals("ok"));
        return "/tickets-result.jsp";
    }
}