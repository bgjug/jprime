package site.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import site.controller.invoice.InvoiceData;
import site.controller.invoice.InvoiceExporter;
import site.facade.MailService;
import site.facade.RegistrantService;
import site.model.Registrant;

import javax.transaction.Transactional;
import javax.validation.Valid;

/**
 * @author Ivan St. Ivanov
 */
@Controller()
@RequestMapping(value = "/admin/invoice")
public class AdminInvoiceController {

    public static final String INVOICE_DATA_JSP = "/admin/invoice/invoiceData.jsp";

    @Autowired
    @Qualifier(RegistrantService.NAME)
    private RegistrantService registrantFacade;

    @Autowired
    private InvoiceExporter invoiceExporter;

    @Autowired
    @Lazy
    private MailService mailFacade;

    @RequestMapping(value = "/{itemId}", method = RequestMethod.GET)
    public String invoiceDataForm(@PathVariable("itemId") Long itemId, Model model) {
        InvoiceData invoiceData = InvoiceData.fromRegistrant(registrantFacade.findById(itemId));
        model.addAttribute("invoiceData", invoiceData);
        return INVOICE_DATA_JSP;
    }

    @Transactional
    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public String sendInvoice(@Valid final InvoiceData invoiceData, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return INVOICE_DATA_JSP;
        }

        Registrant registrant = registrantFacade.findById(invoiceData.getRegistrantId());
        registrantFacade.setRegistrantPaid(registrant);

        invoiceData.setInvoiceType(InvoiceData.ORIGINAL_BG);
        if (invoiceData.getInvoiceNumber().equals("0")) {
            registrantFacade.generateInvoiceNumber(registrant);
        }

        invoiceData.setInvoiceNumber(String.valueOf(registrant.getRealInvoiceNumber()));

        try {
            byte[] invoice = invoiceExporter.exportInvoice(invoiceData, registrant.isCompany());
            mailFacade.sendInvoice(registrant.getEmail(), "jPrime.io original invoice",
                    "Please find attached the invoice for the conference passes that you purchased.",
                    invoice, TicketsController.generatePdfFilename(registrant, invoiceData.getSinglePriceWithVAT()));
            mailFacade.sendInvoice("conference@jprime.io", "jPrime.io invoice",
                    "The attached invoice was sent to " + registrant.getEmail(), invoice, TicketsController.generatePdfFilename(registrant, invoiceData.getSinglePriceWithVAT()));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/admin/registrant/view";
    }
}
