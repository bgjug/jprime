package site.controller.invoice;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mitia on 10.04.15.
 */
public class InvoiceExporter {


    public byte[] exportInvoice(InvoiceData data) throws Exception{

        InputStream reportTemplate = getClass().getResourceAsStream("/invoice/invoicetemplate.jrxml");
        ByteArrayOutputStream result = new ByteArrayOutputStream();

        ArrayList<InvoiceData> exportList = new ArrayList<>();
        // device with some new functionality
        exportList.add(data);
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(
                exportList);
        Map<String, Object> parameters = new HashMap<>();


        JasperDesign jasperDesign = JRXmlLoader.load(reportTemplate);
        JasperReport jasperReport = JasperCompileManager
                .compileReport(jasperDesign);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
                parameters, beanColDataSource);

        JRPdfExporter pdfExporter = new JRPdfExporter();
        pdfExporter
                .setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        pdfExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, result);
        pdfExporter.exportReport();

        return result.toByteArray();
    }
    //demonstration purposes only!
    public static void main(String[] args) throws Exception {
        InvoiceData data = new InvoiceData();
        data.setInvoiceDate("27.05.2015");
        data.setInvoiceNumber("11234");
        data.setClient("Some client");
        data.setClientAddress("1111, Sofia, some street 1, some entrance, some floor, some appt");
        data.setClientEIK("2464387775");
        data.setClientVAT("BG2464387775");
        data.setPassQty(5);
        data.setPrice(55.5);

        Files.write(Paths.get("/Users/mitia/Desktop/result.pdf"), new InvoiceExporter().exportInvoice(data));
    }
}
