package site.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import site.controller.invoice.InvoiceData;
import site.controller.invoice.InvoiceExporter;
import site.facade.BranchService;
import site.facade.InvoiceService;
import site.facade.MailService;
import site.facade.RegistrantService;
import site.facade.UserService;
import site.model.Branch;
import site.model.Registrant;
import site.model.TicketPrice;
import site.model.TicketType;
import site.model.Visitor;
import site.model.VisitorStatus;

import static org.springframework.util.ObjectUtils.isEmpty;
import static site.controller.invoice.InvoiceLanguage.BG;

/**
 * @author Mihail
 */
@Controller
public class TicketsController {

    private static final Logger logger = LogManager.getLogger(TicketsController.class);

    public static final String TICKETS_END_JSP = "tickets";
    public static final String TICKETS_REGISTER_JSP = "tickets-register";
    public static final String TICKETS_RESULT_JSP = "tickets-result";

    @Autowired
    private MailService mailFacade;

    @Autowired
    private InvoiceExporter invoiceExporter;

    @Autowired
    private RegistrantService registrantFacade;

    @Autowired
    private InvoiceService invoiceService;

    @Value("${save.invoice.on.email.failure:false}")
    private boolean saveInvoiceOnFailure;

    @Value("${save.invoice.path.to.save:/tmp}")
    private String pathToSave;

    @Autowired
    private BranchService branchService;

    @GetMapping(value = "/tickets")
    public String goToRegisterPage(Model model) {
        model.addAttribute("registrant", new Registrant());

        Branch currentBranch = branchService.getCurrentBranch();
        Map<TicketType, TicketPrice> prices = branchService.getTicketPrices(currentBranch);

        model.addAttribute("early_bird_ticket_price", String.format("%.2f", prices.get(TicketType.EARLY_BIRD).getPrice()));
        model.addAttribute("regular_ticket_price", String.format("%.2f",prices.get(TicketType.REGULAR).getPrice()));
        model.addAttribute("student_ticket_price", String.format("%.2f",prices.get(TicketType.STUDENT).getPrice()));

        model.addAttribute("cfp_close_date", DateUtils.dateToStringWithMonthAndYear(currentBranch.getCfpCloseDate()));
        model.addAttribute("cfp_close_date_no_year", DateUtils.dateToStringWithMonth(currentBranch.getCfpCloseDate()));

        model.addAttribute("jprime_year", currentBranch.getStartDate().getYear());
        model.addAttribute("jprime_next_year", currentBranch.getStartDate().getYear() + 1);

		return currentBranch.isSoldOut() ? TICKETS_END_JSP : TICKETS_REGISTER_JSP;
    }

    /**
     * User submitted the form.
     */
    @Transactional
    @PostMapping(value = "/tickets")
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
        registrant.setBranch(branchService.getCurrentBranch());
        List<Visitor> toBeRemoved = registrant.getVisitors().stream().filter(v -> isEmpty(v.getEmail()) || isEmpty(v.getName())).toList();
        registrant.getVisitors().removeAll(toBeRemoved);
        registrant.getVisitors().forEach(visitor -> visitor.setStatus(VisitorStatus.REQUESTING));

        if (!registrant.isCompany()) {
            handlePersonalRegistrant(registrant);
        }

        registrant.setCreatedDate(LocalDateTime.now());
        registrant.setPaymentType(Registrant.PaymentType.BANK_TRANSFER);
        Registrant savedRegistrant = registrantFacade.save(registrant);

        InvoiceData invoiceData = buildInvoiceData(savedRegistrant);

        byte[] pdf = invoiceExporter.exportInvoice(invoiceData, registrant.isCompany(), BG);
        sendPDF(savedRegistrant, generatePdfFilename(registrant, invoiceData.getTotalPriceWithVAT()), pdf);
        return result("ok", model);
    }

    /** if registrant info is not filled, copy it from the first visitor */
    private void handlePersonalRegistrant(Registrant registrant) {
        Visitor firstVisitor = registrant.getVisitors().get(0);
        registrant.setName(firstVisitor.getName());
        registrant.setEmail(firstVisitor.getEmail());
    }

    private InvoiceData buildInvoiceData(Registrant registrant) {
        InvoiceData invoiceData = invoiceService.fromRegistrant(registrant);
        if(registrant.getPaymentType() == Registrant.PaymentType.BANK_TRANSFER) {
            invoiceData.setInvoiceType(InvoiceData.PROFORMA_BG);//currently hardcoded
            invoiceData.setInvoiceNumber(String.valueOf(registrant.getProformaInvoiceNumber()));
        } else {
            invoiceData.setInvoiceType(InvoiceData.ORIGINAL_BG);//currently hardcoded
            invoiceData.setInvoiceNumber(String.valueOf(registrant.getRealInvoiceNumber()));
        }
        return invoiceData;
    }

    private void sendPDF(Registrant registrant, String pdfFilename, byte[] pdfContent) {
        try {
            mailFacade.sendEmail(registrant.getEmail(), "jPrime.io invoice", """
                    Thank you for registering at jPrime! Your proforma invoice is attached as part of this mail.<br/>
                    Once we confirm your payment, we'll send you the original invoice.<br/>
                    The attendees that you registered will receive the tickets a few days before the event on their emails.""",
                    pdfContent, pdfFilename);
            String registrations = registrant.getVisitors().toString();
            mailFacade.sendEmail("conference@jprime.io", "jPrime.io invoice",
                    "We got some registrations: " + registrations, pdfContent, pdfFilename);
        } catch (Exception e) {
            if (saveInvoiceOnFailure) {
                try {
                    Files.write(Paths.get(pathToSave, pdfFilename), pdfContent);
                } catch (IOException e1) {
                    logger.error("Could not save confirmation email", e);
                }
            }
            logger.error("Could not send confirmation email", e);
        }
    }

    private String result(final String r, Model model) {
        model.addAttribute("result", "ok".equals(r));
        return TICKETS_RESULT_JSP;
    }

    public static String generatePdfFilename(Registrant registrant, BigDecimal totalPriceWithVAT) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        String date = dateFormat.format(Calendar.getInstance().getTime());

        if(registrant.getRealInvoiceNumber() != 0) {
            return date+", "+registrant.getRealInvoiceNumber()+", "+registrant.getVisitors().size()+"tickets, "+totalPriceWithVAT+".pdf";
        } else {
            return "P "+date+", "+registrant.getProformaInvoiceNumber()+", "+registrant.getVisitors().size()+"tickets, "+totalPriceWithVAT+".pdf";
        }
    }
}
