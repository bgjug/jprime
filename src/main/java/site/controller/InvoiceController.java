package site.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import site.controller.invoice.InvoiceData;
import site.facade.RegistrantFacade;

/**
 * @author Ivan St. Ivanov
 */
@Controller()
@RequestMapping(value = "/admin/invoice")
public class InvoiceController {

    public static final String INVOICE_DATA_JSP = "/admin/invoice/invoiceData.jsp";

    @Autowired
    @Qualifier(RegistrantFacade.NAME)
    private RegistrantFacade registrantFacade;

    @RequestMapping(value = "/{itemId}", method = RequestMethod.GET)
    public String submissionForm(@PathVariable("itemId") Long itemId, Model model) {
        InvoiceData invoiceData = InvoiceData.fromRegistrant(registrantFacade.findById(itemId));
        model.addAttribute("invoiceData", invoiceData);
        return INVOICE_DATA_JSP;
    }

}
