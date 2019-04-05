package site.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import site.controller.invoice.*;
import site.facade.MailService;
import site.facade.RegistrantService;
import site.model.Registrant;

import javax.transaction.Transactional;
import javax.validation.Valid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static site.controller.TicketsController.*;
import static site.controller.invoice.InvoiceLanguage.*;

/**
 * @author Ivan St. Ivanov
 */
@Controller()
@RequestMapping(value = "/admin/invoice")
public class AdminInvoiceController {

    private static final Logger logger = LogManager.getLogger(AdminInvoiceController.class);

    public static final String INVOICE_DATA_JSP = "/admin/invoice/invoiceData.jsp";

    @Autowired
    @Qualifier(RegistrantService.NAME)
    private RegistrantService registrantFacade;

    @Autowired
    private InvoiceExporter invoiceExporter;

    @Autowired
    @Lazy
    private MailService mailFacade;

    @Value("${save.invoice.on.email.failure:false}")
    private boolean saveInvoiceOnFailure;

    @Value("${save.invoice.path.to.save:/tmp}")
    private String pathToSave;

    @RequestMapping(value = "/{itemId}", method = RequestMethod.GET)
    public String invoiceDataForm(@PathVariable("itemId") Long itemId, Model model) {
        InvoiceData invoiceData = InvoiceData.fromRegistrant(registrantFacade.findById(itemId));
        model.addAttribute("invoiceData", invoiceData);
        return INVOICE_DATA_JSP;
    }

    @Transactional
    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public String sendInvoice(@Valid final InvoiceData modelInvoiceData, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return INVOICE_DATA_JSP;
        }

        Registrant  registrant = registrantFacade.findById(modelInvoiceData.getRegistrantId());
        InvoiceData invoiceData = InvoiceData.fromRegistrant(registrant);
        invoiceData.setSinglePriceWithVAT(modelInvoiceData.getSinglePriceWithVAT());
        invoiceData.setDescription(modelInvoiceData.getDescription());
        registrantFacade.setRegistrantPaid(registrant);

        invoiceData.setInvoiceType(InvoiceData.ORIGINAL_BG);
        if (invoiceData.getInvoiceNumber().equals("0")) {
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
            mailFacade.sendInvoice(registrant.getEmail(), "jPrime.io original invoice",
                    "Please find attached the invoice for the conference passes that you purchased.",
                    invoice, pdfFileName);
            mailFacade.sendInvoice("conference@jprime.io", "jPrime.io invoice",
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
