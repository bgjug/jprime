package site.controller.invoice;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Service;
import site.config.Globals;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by mitia on 10.04.15.
 */
@Service
public class InvoiceExporter {


    public byte[] exportInvoice(InvoiceData data, boolean isCompany) throws Exception{
        InputStream reportTemplate = null;
        if (isCompany) {
            reportTemplate = getClass().getResourceAsStream("/invoice/invoice_company_template_bg.jrxml");
        } else {
            reportTemplate = getClass().getResourceAsStream("/invoice/invoice_individual_template_bg.jrxml");
        }
        ByteArrayOutputStream result = new ByteArrayOutputStream();

        // device with some new functionality
        List<InvoiceDetail> exportList = new ArrayList<>(data.getInvoiceDetails());
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(
                exportList);

        JasperDesign jasperDesign = JRXmlLoader.load(reportTemplate);
        JasperReport jasperReport = JasperCompileManager
            .compileReport(jasperDesign);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("jprime.year", Globals.CURRENT_BRANCH.toString());

        // Fill in other parameters that have matching properties in invoice data.
        Arrays.stream(jasperReport.getParameters()).forEach(p-> {
            try {
                parameters.put(p.getName(), PropertyUtils.getProperty(data, p.getName()));
            } catch (IllegalAccessException | InvocationTargetException e) {
                // this should not happen but any way print exception just in case.
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                // This exception happens when we are asked for property that is not part of our invoice data.
            }
        });

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
                parameters, beanColDataSource);

        JRPdfExporter pdfExporter = new JRPdfExporter();
        pdfExporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
        pdfExporter
                .setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        pdfExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, result);
        pdfExporter.exportReport();

        return result.toByteArray();
    }
    //demonstration purposes only!
    public static void main(String[] args) throws Exception {
        InvoiceData data = new InvoiceData();
        data.setInvoiceDate("27.05.2016");
        data.setInvoiceNumber("11234");
        data.setClient("някъв клиент");
        data.setClientAddress("1111, Sofia, some улица 1, some entrance, some floor, some appt");
        data.setClientEIK("2464387775");
        data.setClientVAT("BG2464387775");
        data.setMol("fda");
        data.setInvoiceType("Проформа");
        data.setPaymentType("пеймънт");
        data.addInvoiceDetail(new InvoiceDetail(5));
        data.addInvoiceDetail(new InvoiceDetail( InvoiceData.STUDENT_TICKET_PRICE, 3, InvoiceData.STUDENT_DESCRIPTION_BG));

        Files.write(Paths.get("/tmp/result.pdf"), new InvoiceExporter().exportInvoice(data, true));
    }
}
