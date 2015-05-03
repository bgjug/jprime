package site.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
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
import site.model.PaymentTypeEditor;
import site.model.Registrant;
import site.model.Visitor;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Mihail
 */
@Controller
public class TicketsController {

    static final String TICKETS_JSP = "/tickets.jsp";
    public static final String TICKETS_EPAY_REGISTER_JSP = "/tickets-epay-register.jsp";
    public static final String TICKETS_EPAY_BUY_JSP = "/tickets-epay-buy.jsp";
    @Autowired
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
        model.addAttribute("paymentTypes", getPaymentTypes());
		return TICKETS_EPAY_REGISTER_JSP;
    }

    private List<String> getPaymentTypes() {
        return Arrays.stream(Registrant.PaymentType.values())
                .map(Registrant.PaymentType::toString)
                .collect(Collectors.toList());
    }

    @InitBinder
    public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
        binder.registerCustomEditor(Registrant.PaymentType.class, new PaymentTypeEditor());
    }

    /**
     * User submitted the form.
     */
    @Transactional
    @RequestMapping(value = "/tickets/epay", method = RequestMethod.POST)
    public String register(Model model, @Valid final Registrant registrant, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return TICKETS_EPAY_REGISTER_JSP;
        }

        //check empty users, server side validation
        List<Visitor> toBeRemoved = registrant.getVisitors().stream().filter(v -> v.getEmail() == null || v.getEmail().isEmpty() || v.getName() == null || v.getName().isEmpty()).collect(Collectors.toList());
        registrant.getVisitors().removeAll(toBeRemoved);


        if (!registrant.isCompany()) {
            handlePersonalRegistrant(registrant);
        }

        Registrant savedRegistrant = registrantFacade.save(registrant);

        model.addAttribute("tags", userFacade.findAllTags());

        if(savedRegistrant.getPaymentType().equals(Registrant.PaymentType.BANK_TRANSFER)) {
            byte[] pdf = createPDF(savedRegistrant);
            sendPDF(savedRegistrant, pdf);
            return result("ok", model);
        } else {
            prepareEpay(model, savedRegistrant);
            return TICKETS_EPAY_BUY_JSP;
        }
    }

    /** if registrant info is not filled, copy it from the first visitor */
    private void handlePersonalRegistrant(Registrant registrant) {
        Visitor firstVisitor = registrant.getVisitors().get(0);
        registrant.setName(firstVisitor.getName());
        registrant.setEmail(firstVisitor.getEmail());
    }

    /** encrypt the data and set the fields in the form */
    @RequestMapping(value = "/tickets/buy", method = RequestMethod.GET)
    public void prepareEpay(Model model, Registrant registrant) {
        EpayRaw demoEpayRaw = EpayUtil.encrypt(registrant.getVisitors().size(),
                registrant.getEpayInvoiceNumber(), false, 0);
        model.addAttribute("DEMO_ENCODED", demoEpayRaw.getEncoded());
        model.addAttribute("DEMO_CHECKSUM", demoEpayRaw.getChecksum());
        model.addAttribute("DEMO_epayUrl", demoEpayRaw.getEpayUrl());

        EpayRaw epayRaw = EpayUtil.encrypt(registrant.getVisitors().size(), registrant.getEpayInvoiceNumber(), true, 0);
        model.addAttribute("ENCODED", epayRaw.getEncoded());
        model.addAttribute("CHECKSUM", epayRaw.getChecksum());
        model.addAttribute("epayUrl", epayRaw.getEpayUrl());

        model.addAttribute("tags", userFacade.findAllTags());
    }

    /** Receiving data from epay.bg (back channel) */
    @RequestMapping(value = "/tickets/from.epay", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.HEAD})
    @ResponseBody//we return the string literal
    public String receiveFromEpay(HttpServletRequest request) {
        Map<String, String[]> parameters = request.getParameterMap();
        try {
            String encoded = parameters.get("encoded")[0];
            String checksum = parameters.get("checksum")[0];
            EpayRaw epayRaw = new EpayRaw(checksum, encoded, null);
            EpayResponse epayResponse = EpayUtil.decrypt(epayRaw);
            System.out.println("EPAY: "+epayResponse);

            Registrant registrant = registrantFacade.findByEpayInvoiceNumber(
                    epayResponse.getInvoiceNumber());
            if(registrant == null) {
                //we don't have that invoiceNumber in the database, probably testing
                System.out.println("EPAY:    InvoiceNumber "+epayResponse.getInvoiceNumber()+" missing in DB, return OK, so that epay will stop bugging me");
                return "INVOICE=" + epayResponse.getInvoiceNumber() + ":STATUS=OK";
            }

            if(registrant.getEpayResponse() != null && registrant.getEpayResponse().getStatus().equals(EpayResponse.Status.PAID)) {
                //so this guy already received an invoice, but epay is still bugging me
                System.out.println("EPAY:    InvoiceNumber "+epayResponse.getInvoiceNumber()+" already PAID in database, why is epay still bugging me?");
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
        String regMol = registrant.getMol(); // company mol
        String regAddress = registrant.getAddress(); //company address
        String regVat = registrant.getVatNumber(); //company DDS number
        int qty = registrant.getVisitors().size();

        InvoiceData data = new InvoiceData();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        data.setInvoiceDate(dateFormat.format(Calendar.getInstance().getTime()));
        data.setClient(regName);
        data.setClientAddress(regAddress);
        data.setClientEIK(regVat);
        data.setMol(regMol);
        if(registrant.getPaymentType().equals(Registrant.PaymentType.BANK_TRANSFER)) {
            data.setInvoiceType("Proforma");
            data.setInvoiceNumber(String.valueOf(registrant.getProformaInvoiceNumber()));
            data.setPaymentType("Bank Transfer");
        } else {
            data.setInvoiceType("Original");
            data.setInvoiceNumber(String.valueOf(registrant.getRealInvoiceNumber()));
            data.setPaymentType("ePay.bg");
        }

        String vatNumber = registrant.getVatNumber();
        if (vatNumber != null) {
            if (!vatNumber.startsWith("BG")) {
                vatNumber = "BG" + vatNumber;
            }
            data.setClientVAT(vatNumber);
        } else {
            data.setClientVAT("");
        }
        data.setPassQty(qty);

        return invoiceExporter.exportInvoice(data, registrant.isCompany());
    }

    private void sendPDF(Registrant registrant, byte[] pdf) throws MessagingException {
        String email = registrant.getEmail();
        mailFacade.sendInvoice(email, "JPrime.io invoice",
                "Thank you for registering to JPrime. Your invoice is attached as part of this mail.",
                pdf);
        String registrations = registrant.getVisitors().toString();
        mailFacade.sendInvoice("conference@jprime.io", "JPrime.io invoice",
                "We got some registrations: " + registrations, pdf);
    }
}