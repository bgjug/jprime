package site.controller.invoice;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.stereotype.Service;

import site.config.Globals;

import static org.apache.commons.beanutils.PropertyUtils.getProperty;
import static site.controller.invoice.InvoiceData.DEFAULT_DESCRIPTION_BG;
import static site.controller.invoice.InvoiceData.TicketPrices;
import static site.controller.invoice.InvoiceLanguage.EN;

/**
 * Created by mitia on 10.04.15.
 */
@Service
public class InvoiceExporter {

    public byte[] exportInvoice(InvoiceData data, boolean isCompany, InvoiceLanguage language)
        throws Exception {
        String resourceName = null;
        if (language == InvoiceLanguage.EN) {
            if (isCompany) {
                resourceName = "/invoice/invoice_company_template.jasper";
            } else {
                resourceName = "/invoice/invoice_individual_template.jasper";
            }
        } else if (language == InvoiceLanguage.BG) {
            if (isCompany) {
                resourceName = "/invoice/invoice_company_template_bg.jasper";
            } else {
                resourceName = "/invoice/invoice_individual_template_bg.jasper";
            }
        }

        InputStream reportTemplate = getClass().getResourceAsStream(resourceName);
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportTemplate);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("jprime.year", Globals.CURRENT_BRANCH.toString());

        // Fill in other parameters that have matching properties in invoice data.
        Arrays.stream(jasperReport.getParameters()).forEach(p -> {
            try {
                parameters.put(p.getName(), getProperty(data, p.getName()));
            } catch (IllegalAccessException | InvocationTargetException e) {
                // this should not happen but any way print exception just in case.
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                // This exception happens when we are asked for property that is not part of our invoice data.
            }
        });

        List<InvoiceDetail> exportList = new ArrayList<>(data.getInvoiceDetails());
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(exportList);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanColDataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    //demonstration purposes only!
    public static void main(String[] args) throws Exception {
        InvoiceData data = new InvoiceData();
        data.setInvoiceDate("27.05.2016");
        data.setInvoiceNumber("11234");
        data.setClient("Много дълго име на клиент за да видим как ще реагира JasperReports");
        data.setClientAddress("1111, Sofia, some улица 1, some entrance, some floor, some appt and few more symbols");
        data.setClientEIK("2464387775");
        data.setClientVAT("BG2464387775");
        data.setMol("fda");
        data.setInvoiceType("Проформа");
        data.setPaymentType("пеймънт");
        TicketPrices ticketPrices = InvoiceData.getPrices(Globals.CURRENT_BRANCH);
        data.addInvoiceDetail(new InvoiceDetail(ticketPrices.getStudentPrice(), 3, DEFAULT_DESCRIPTION_BG));
        data.addInvoiceDetail(new InvoiceDetail(ticketPrices.getPrice(Globals.CURRENT_BRANCH), 3, DEFAULT_DESCRIPTION_BG));

        Files.write(Paths.get("invoice_test.pdf"), new InvoiceExporter().exportInvoice(data, true, EN));
    }
}
