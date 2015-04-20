package site.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import site.controller.epay.EpayRaw;
import site.controller.epay.EpayResponse;
import site.controller.epay.EpayUtil;
import site.controller.invoice.InvoiceData;
import site.controller.invoice.InvoiceExporter;
import site.facade.MailFacade;
import site.facade.RegistrantFacade;
import site.facade.UserFacade;
import site.model.JprimeException;
import site.model.Registrant;
import site.model.Visitor;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;import java.util.Map;

/**
 * @author Mihail
 */
@Controller
public class TicketsController {

    static final String TICKETS_JSP = "/tickets.jsp";
    public static final String TICKETS_EPAY_REGISTER_JSP = "/tickets-epay-register.jsp";    @Autowired
    @Qualifier(MailFacade.NAME)
    private MailFacade mailFacade;

    @Autowired
    @Qualifier(UserFacade.NAME)
    private UserFacade userFacade;

    @Autowired
    private InvoiceExporter invoiceExporter;

    @Autowired
    @Qualifier(RegistrantFacade.NAME)
    private RegistrantFacade registrantFacade;

    //Mihail: old tickets page
    @RequestMapping(value = "/tickets", method = RequestMethod.GET)
    public String submissionForm(Model model) {
    	model.addAttribute("tags", userFacade.findAllTags());
        return TICKETS_JSP;
    }

    @RequestMapping(value = "/tickets/epay", method = RequestMethod.GET)
    public String goToRegisterPage(Model model) {
        model.addAttribute("tags", userFacade.findAllTags());
        model.addAttribute("registrant", new Registrant());
		return TICKETS_EPAY_REGISTER_JSP;
    }

    @Transactional
    @RequestMapping(value = "/tickets/epay", method = RequestMethod.POST)
    public String register(Model model, @Valid final Registrant registrant, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return TICKETS_EPAY_REGISTER_JSP;
        }

        if (!registrant.isCompany()) {
            handlePersonalRegistrant(registrant);
        }        Registrant savedRegistrant = registrantFacade.save(registrant);

        model.addAttribute("tags", userFacade.findAllTags());
//        model.addAttribute("registrant", registrant);
        prepareEpay(model, savedRegistrant);
        return "redirect:/tickets/buy";
    }

    private void handlePersonalRegistrant(Registrant registrant) {
        Visitor firstVisitor = registrant.getVisitors().get(0);
        registrant.setName(firstVisitor.getName());
        registrant.setEmail(firstVisitor.getEmail());
    }

    @RequestMapping(value = "/tickets/buy", method = RequestMethod.GET)
    public String prepareEpay(Model model, Registrant registrant) {
        EpayRaw epayRaw = EpayUtil.encrypt(registrant.getVisitors().size(), registrant.getInvoiceNumber());
        model.addAttribute("ENCODED", epayRaw.getEncoded());
        model.addAttribute("CHECKSUM", epayRaw.getChecksum());
//        model.addAttribute("facNo", registrant.getFacNo());
        model.addAttribute("epayUrl", EpayUtil.EPAY_URL);

        model.addAttribute("tags", userFacade.findAllTags());
        return "/tickets-epay-buy.jsp";
    }

    /** Receiving data from epay.bg (back channel) */
    @RequestMapping(value = "/tickets/from.epay", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.HEAD})
    @ResponseBody//we return the string literal
    public String receiveFromEpay(HttpServletRequest request) {
        Map<String, String[]> parameters = request.getParameterMap();
        try {
            String encoded = parameters.get("encoded")[0];
            String checksum = parameters.get("checksum")[0];
            EpayRaw epayRaw = new EpayRaw(checksum, encoded);
            EpayResponse epayResponse = EpayUtil.decrypt(epayRaw);
            System.out.println("EPAY: "+epayResponse);

            Registrant registrant = registrantFacade.findByInvoiceNumber(epayResponse.getInvoiceNumber());
            if(registrant == null) {
                //we don't have that invoiceNumber in the database, probably testing
                System.out.println("InvoiceNumber "+epayResponse.getInvoiceNumber()+" missing in DB, return OK, so that epay will stop bugging me");
                return "INVOICE=" + epayResponse.getInvoiceNumber() + ":STATUS=OK";
            }
            registrant.setEpayResponse(epayResponse);
            registrant = registrantFacade.save(registrant);
            byte[] pdf = createPDF(registrant);
            sendPDF(registrant, pdf);
            return epayResponse.getEpayAnswer();
        } catch (Throwable t) {
            throw new JprimeException("epay response parsing failed", t);
        }
    }

    /** result page after epay payment */
    @RequestMapping(value = "/tickets/result/{r}", method = RequestMethod.GET)
    public String result(@PathVariable("r") final String r, Model model) {
        model.addAttribute("result", r.equals("ok"));
        return "/tickets-epay-result.jsp";
    }

    private byte[] createPDF(Registrant registrant) throws Exception {
        String regName = registrant.getName(); // company name

        // FIXME: set MOL in PDF
        String regMol = registrant.getMol(); // company mol
        String regAddress = registrant.getAddress(); //company address
        String regVat = registrant.getVatNumber(); //company BULSTAT
        int qty = registrant.getVisitors().size();

        InvoiceData data = new InvoiceData();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        data.setInvoiceDate(dateFormat.format(Calendar.getInstance().getTime()));
        data.setInvoiceNumber(String.valueOf(registrant.getInvoiceNumber()));
        data.setClient(regName);
        data.setClientAddress(regAddress);
        data.setClientEIK(regVat);

        // FIXME: append country code ?
        data.setClientVAT("BG" + registrant.getVatNumber());
        data.setPassQty(qty);
        data.setPrice(Double.valueOf(qty*100));

        byte[] pdf = invoiceExporter.exportInvoice(data);
        return pdf;
    }

    private void sendPDF(Registrant registrant, byte[] pdf) {
        String email = registrant.getEmail();
        mailFacade.sendEmail(email, "JPrime.io invoice",
                "Thank you for registering to JPrime. Your invoice is attached as part of this mail.", pdf);
    }
}
