package site.controller;

import org.apache.log4j.Logger;
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
import site.model.VisitorStatus;

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

    private static final Logger logger = Logger.getLogger(TicketsController.class);

    static final String TICKETS_JSP = "/tickets.jsp";
    public static final String TICKETS_EPAY_REGISTER_JSP = "/tickets-epay-register.jsp";
    public static final String TICKETS_EPAY_BUY_JSP = "/tickets-epay-buy.jsp";
    public static final String TICKETS_EPAY_RESULT_JSP = "/tickets-epay-result.jsp";

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
        registrant.getVisitors().forEach(visitor -> visitor.setStatus(VisitorStatus.REQUESTING));

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
        int numberOfTickets = registrant.getVisitors().size();
        EpayRaw demoEpayRaw = EpayUtil.encrypt(numberOfTickets,
                registrant.getEpayInvoiceNumber(), false, 0);
        model.addAttribute("DEMO_ENCODED", demoEpayRaw.getEncoded());
        model.addAttribute("DEMO_CHECKSUM", demoEpayRaw.getChecksum());
        model.addAttribute("DEMO_epayUrl", demoEpayRaw.getEpayUrl());

        EpayRaw epayRaw = EpayUtil.encrypt(numberOfTickets, registrant.getEpayInvoiceNumber(), true, 0);
        model.addAttribute("ENCODED", epayRaw.getEncoded());
        model.addAttribute("CHECKSUM", epayRaw.getChecksum());
        model.addAttribute("epayUrl", epayRaw.getEpayUrl());

        model.addAttribute("credit_wt_kin", EpayUtil.EPAY_KIN);
        model.addAttribute("credit_wt_amount", numberOfTickets*100);
        model.addAttribute("credit_wt_description", (numberOfTickets == 1 ? "One jPrime.io ticket" : numberOfTickets + " jPrime.io tickets") + ", invoice number: " + registrant.getEpayInvoiceNumber());
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
        return TICKETS_EPAY_RESULT_JSP;
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
        data.setPaymentType(registrant.getPaymentType().getBulgarianValue());
        if(registrant.getPaymentType().equals(Registrant.PaymentType.BANK_TRANSFER)) {
            data.setInvoiceType(InvoiceData.PROFORMA_BG);//currently hardcoded
            data.setInvoiceNumber(String.valueOf(registrant.getProformaInvoiceNumber()));
        } else {
            data.setInvoiceType(InvoiceData.ORIGINAL_BG);//currently hardcoded
            data.setInvoiceNumber(String.valueOf(registrant.getRealInvoiceNumber()));
        }

        String vatNumber = registrant.getVatNumber();
        if (vatNumber != null) {
            data.setClientVAT(vatNumber);
        } else {
            data.setClientVAT("");
        }
        data.setPassQty(qty);

        return invoiceExporter.exportInvoice(data, registrant.isCompany());
    }

    private void sendPDF(Registrant registrant, byte[] pdf) throws MessagingException {
        try {
            String email = registrant.getEmail();
            mailFacade.sendInvoice(email, "jPrime.io invoice",
                    "Thank you for registering to JPrime. Your proforma is attached as part of this mail.",
                    pdf, generatePdfFilename(registrant, 100));
            String registrations = registrant.getVisitors().toString();
            mailFacade.sendInvoice("conference@jprime.io", "jPrime.io invoice",
                    "We got some registrations: " + registrations, pdf, generatePdfFilename(registrant, 100));
        } catch (Exception e) {
            logger.error("Could not send confirmation email", e);
        }
    }

    public static String generatePdfFilename(Registrant registrant, double singlePriceWithVAT) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        String date = dateFormat.format(Calendar.getInstance().getTime());

        String invoiceNumber;
        if(registrant.getPaymentType().equals(Registrant.PaymentType.BANK_TRANSFER)) {
//            data.setInvoiceType(InvoiceData.PROFORMA_BG);//currently hardcoded
//            data.setInvoiceNumber(String.valueOf(registrant.getProformaInvoiceNumber()));
            invoiceNumber=registrant.getProformaInvoiceNumber()+"";
            return "P "+date+", "+invoiceNumber+", "+registrant.getName()+", "+registrant.getVisitors().size()+"tickets, "+(registrant.getVisitors().size()*singlePriceWithVAT)+".pdf";
        } else {
//            data.setInvoiceType(InvoiceData.ORIGINAL_BG);//currently hardcoded
//            data.setInvoiceNumber(String.valueOf(registrant.getRealInvoiceNumber()));
            invoiceNumber=registrant.getRealInvoiceNumber()+"";
            return "P "+date+", "+invoiceNumber+", "+registrant.getName()+", "+registrant.getVisitors().size()+"tickets, "+(registrant.getVisitors().size()*singlePriceWithVAT)+".pdf";
        }
    }
}