package site.controller.invoice;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.springframework.stereotype.Service;
import site.config.Globals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

        ArrayList<InvoiceData> exportList = new ArrayList<>();
        // device with some new functionality
        exportList.add(data);
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(
                exportList);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("jprime.year", Globals.CURRENT_BRANCH.toString());

        JasperDesign jasperDesign = JRXmlLoader.load(reportTemplate);
        JasperReport jasperReport = JasperCompileManager
                .compileReport(jasperDesign);
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
        data.setPassQty(5);
        data.setInvoiceType("Проформа");
        data.setPaymentType("пеймънт");
//        data.setPrice(55.5);

        Files.write(Paths.get("/tmp/result.pdf"), new InvoiceExporter().exportInvoice(data, false));
    }
}
