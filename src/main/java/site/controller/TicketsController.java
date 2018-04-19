package site.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import site.controller.invoice.InvoiceData;
import site.controller.invoice.InvoiceExporter;
import site.facade.MailService;
import site.facade.RegistrantService;
import site.facade.UserService;
import site.model.Registrant;
import site.model.Visitor;
import site.model.VisitorStatus;

/**
 * @author Mihail
 */
@Controller
public class TicketsController {

    private static final Logger logger = LogManager.getLogger(TicketsController.class);

    public static final String TICKETS_END_JSP = "/tickets.jsp";
    public static final String TICKETS_REGISTER_JSP = "/tickets-register.jsp";
    public static final String TICKETS_RESULT_JSP = "/tickets-result.jsp";

    @Autowired
    @Qualifier(MailService.NAME)
    @Lazy
    private MailService mailFacade;

    @Autowired
    @Qualifier(UserService.NAME)
    private UserService userFacade;

    @Autowired
    private InvoiceExporter invoiceExporter;

    @Autowired
    @Qualifier(RegistrantService.NAME)
    private RegistrantService registrantFacade;

    @RequestMapping(value = "/tickets/epay", method = RequestMethod.GET)
    public String goToRegisterPage(Model model) {
        model.addAttribute("tags", userFacade.findAllTags());
        model.addAttribute("registrant", new Registrant());
		return site.config.Globals.PAGE_TICKETS;
    }

    /**
     * User submitted the form.
     */
    @Transactional
    @RequestMapping(value = "/tickets/epay", method = RequestMethod.POST)
    public String register(Model model, @Valid final Registrant registrant, BindingResult bindingResult, HttpServletRequest request) throws Exception {
		boolean invalidCaptcha = false;
    	if (registrant.getCaptcha() == null || !registrant.getCaptcha()
				.equals(request.getSession().getAttribute(CaptchaController.SESSION_PARAM_CAPTCHA_IMAGE))) {
    		bindingResult.rejectValue("captcha", "invalid");
			invalidCaptcha = true;
		}
		if (bindingResult.hasErrors() || invalidCaptcha) {
			return TICKETS_REGISTER_JSP;
		}

        //check empty users, server side validation
        List<Visitor> toBeRemoved = registrant.getVisitors().stream().filter(v -> v.getEmail() == null || v.getEmail().isEmpty() || v.getName() == null || v.getName().isEmpty()).collect(Collectors.toList());
        registrant.getVisitors().removeAll(toBeRemoved);
        registrant.getVisitors().forEach(visitor -> visitor.setStatus(VisitorStatus.REQUESTING));

        if (!registrant.isCompany()) {
            handlePersonalRegistrant(registrant);
        }

        registrant.setPaymentType(Registrant.PaymentType.BANK_TRANSFER);
        Registrant savedRegistrant = registrantFacade.save(registrant);

        model.addAttribute("tags", userFacade.findAllTags());

        InvoiceData invoiceData = buildInvoiceData(savedRegistrant);

        byte[] pdf = invoiceExporter.exportInvoice(invoiceData, registrant.isCompany());
        sendPDF(savedRegistrant, generatePdfFilename(registrant, invoiceData.getSinglePriceWithVAT()), pdf);
        return result("ok", model);
    }

    /** if registrant info is not filled, copy it from the first visitor */
    private void handlePersonalRegistrant(Registrant registrant) {
        Visitor firstVisitor = registrant.getVisitors().get(0);
        registrant.setName(firstVisitor.getName());
        registrant.setEmail(firstVisitor.getEmail());
    }

    private InvoiceData buildInvoiceData(Registrant registrant) {
        InvoiceData invoiceData = InvoiceData.fromRegistrant(registrant);
        if(registrant.getPaymentType().equals(Registrant.PaymentType.BANK_TRANSFER)) {
            invoiceData.setInvoiceType(InvoiceData.PROFORMA_BG);//currently hardcoded
            invoiceData.setInvoiceNumber(String.valueOf(registrant.getProformaInvoiceNumber()));
        } else {
            invoiceData.setInvoiceType(InvoiceData.ORIGINAL_BG);//currently hardcoded
            invoiceData.setInvoiceNumber(String.valueOf(registrant.getRealInvoiceNumber()));
        }
        return invoiceData;
    }

    private void sendPDF(Registrant registrant, String pdfFilename, byte[] pdfContent) throws MessagingException {
        try {
            mailFacade.sendInvoice(registrant.getEmail(), "jPrime.io invoice",
                    "Thank you for registering at jPrime. Your proforma invoice is attached as part of this mail.",
                    pdfContent, pdfFilename);
            String registrations = registrant.getVisitors().toString();
            mailFacade.sendInvoice("conference@jprime.io", "jPrime.io invoice",
                    "We got some registrations: " + registrations, pdfContent, pdfFilename);
        } catch (Exception e) {
            logger.error("Could not send confirmation email", e);
        }
    }

    private String result(final String r, Model model) {
        model.addAttribute("result", r.equals("ok"));
        return TICKETS_RESULT_JSP;
    }

    public static String generatePdfFilename(Registrant registrant, double singlePriceWithVAT) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        String date = dateFormat.format(Calendar.getInstance().getTime());

        if(registrant.getRealInvoiceNumber() != 0) {
            return date+", "+registrant.getRealInvoiceNumber()+", "+registrant.getName()+", "+registrant.getVisitors().size()+"tickets, "+(registrant.getVisitors().size()*singlePriceWithVAT)+".pdf";
        } else {
            return "P "+date+", "+registrant.getProformaInvoiceNumber()+", "+registrant.getName()+", "+registrant.getVisitors().size()+"tickets, "+(registrant.getVisitors().size()*singlePriceWithVAT)+".pdf";
        }
    }
}