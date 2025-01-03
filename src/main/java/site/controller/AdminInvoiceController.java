package site.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import site.controller.invoice.InvoiceData;
import site.controller.invoice.InvoiceExporter;
import site.facade.InvoiceService;
import site.facade.MailService;
import site.facade.RegistrantService;
import site.model.Registrant;

import static site.controller.TicketsController.generatePdfFilename;
import static site.controller.invoice.InvoiceLanguage.BG;

/**
 * @author Ivan St. Ivanov
 */
@Controller
@RequestMapping(value = "/admin/invoice")
public class AdminInvoiceController {

    private static final Logger logger = LogManager.getLogger(AdminInvoiceController.class);

    public static final String INVOICE_DATA_JSP = "admin/invoice/invoiceData";

    @Autowired
    private RegistrantService registrantFacade;

    @Autowired
    private InvoiceExporter invoiceExporter;

    @Autowired
    @Lazy
    private MailService mailFacade;

    @Autowired
    private InvoiceService invoiceService;

    @Value("${save.invoice.on.email.failure:false}")
    private boolean saveInvoiceOnFailure;

    @Value("${save.invoice.path.to.save:/tmp}")
    private String pathToSave;

    @GetMapping(value = "/{itemId}")
    public String invoiceDataForm(@PathVariable Long itemId, Model model) {
        InvoiceData invoiceData = invoiceService.fromRegistrant(registrantFacade.findById(itemId));
        model.addAttribute("invoiceData", invoiceData);
        return INVOICE_DATA_JSP;
    }

    @Transactional
        @PostMapping("/send")
    public String sendInvoice(@Valid final InvoiceData modelInvoiceData, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return INVOICE_DATA_JSP;
        }

        Registrant  registrant = registrantFacade.findById(modelInvoiceData.getRegistrantId());

        registrantFacade.setRegistrantPaid(registrant);

        InvoiceData invoiceData = invoiceService.fromRegistrant(registrant);
        // Copy data from model into actual invoice data bean
        invoiceData.setClient(modelInvoiceData.getClient());
        invoiceData.setClientAddress(modelInvoiceData.getClientAddress());
        invoiceData.setClientEIK(modelInvoiceData.getClientEIK());
        invoiceData.setClientVAT(modelInvoiceData.getClientVAT());
        invoiceData.setMol(modelInvoiceData.getMol());
        invoiceData.setInvoiceDate(modelInvoiceData.getInvoiceDate());
        invoiceData.setSinglePriceWithVAT(modelInvoiceData.getSinglePriceWithVAT());
        invoiceData.setDescription(modelInvoiceData.getDescription());

        invoiceData.setInvoiceType(InvoiceData.ORIGINAL_BG);
        if ("0".equals(invoiceData.getInvoiceNumber())) {
            registrantFacade.generateInvoiceNumber(registrant);
        }

        invoiceData.setInvoiceNumber(String.valueOf(registrant.getRealInvoiceNumber()));

        byte[] invoice;
        try {
            invoice = invoiceExporter.exportInvoice(invoiceData, registrant.isCompany(), BG);
        } catch (Exception ex) {
            logger.error("Could not generate invoice pdf", ex);

            return "redirect:/admin/registrant/view";
        }

        String pdfFileName = generatePdfFilename(registrant, invoiceData.getTotalPriceWithVAT());

        try {
            mailFacade.sendEmail(registrant.getEmail(), "jPrime.io original invoice", """
                    Please find attached the invoice for the conference passes that you purchased.
                    
                    The attendees that you registered will receive the tickets a few days before the event on their emails.""",
                    invoice, pdfFileName);
            mailFacade.sendEmail("conference@jprime.io", "jPrime.io invoice",
                    "The attached invoice was sent to " + registrant.getEmail(), invoice,
                pdfFileName);
        } catch (Exception e) {
            if (saveInvoiceOnFailure) {
                try {
                    Files.write(Paths.get(pathToSave, pdfFileName), invoice);
                } catch (IOException e1) {
                    logger.error("Could not save invoice pdf", e);
                }
            }
            logger.error("Could not send invoice email", e);
        }

        return "redirect:/admin/registrant/view";
    }
}
