package site.controller.ticket;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import site.config.Globals;
import site.controller.invoice.InvoiceLanguage;

import static org.apache.commons.beanutils.PropertyUtils.getProperty;
import static site.controller.invoice.InvoiceLanguage.EN;

@Service
public class TicketExporter {

    private static final Logger LOGGER = LogManager.getLogger(TicketExporter.class);

    public byte[] exportTicket(TicketData data, InvoiceLanguage language) {
        String resourceName = null;
        switch (language) {
            case EN:
                resourceName = "/ticket/ticket_en.jasper";
                break;
            case BG:
                resourceName = "/ticket/ticket_bg.jasper";
        }

        InputStream reportTemplate = getClass().getResourceAsStream(resourceName);
        JasperReport jasperReport = null;
        try {
            jasperReport = (JasperReport) JRLoader.loadObject(reportTemplate);
        } catch (JRException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        InputStream logoStream = getClass().getResourceAsStream("/ticket/jprime_ticket_logo.svg");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("logoStream", logoStream);
        parameters.put("organizer", data.getOrganizer());
        parameters.put("event", data.getEvent());

        // Fill in other parameters that have matching properties in invoice data.
        Arrays.stream(jasperReport.getParameters()).forEach(p -> {
            try {
                parameters.put(p.getName(), getProperty(data, p.getName()));
            } catch (IllegalAccessException | InvocationTargetException e) {
                // this should not happen but any way print exception just in case.
                LOGGER.error(e.getMessage(), e);
            } catch (NoSuchMethodException e) {
                // This exception happens when we are asked for property that is not part of our invoice data.
            }
        });

        List<TicketDetail> exportList = new ArrayList<>(data.getDetails());
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(exportList);

        try {
            JasperPrint jasperPrint =
                JasperFillManager.fillReport(jasperReport, parameters, beanColDataSource);

            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (JRException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    //demonstration purposes only!
    public static void main(String[] args) throws Exception {
        TicketData data = new TicketData();
        data.setOrganizer("JPrime Events");
        data.setEvent("JPrime " + Globals.CURRENT_BRANCH);
        data.addDetail(new TicketDetail(UUID.randomUUID().toString(), "Doychin Bondzhev", "Organizer"));
        data.addDetail(new TicketDetail(UUID.randomUUID().toString(), "Iva Bondzheva", "Volunteer"));
        data.addDetail(new TicketDetail(UUID.randomUUID().toString(), "Hristo Kolev", "Visitor - Sponsored"));
        data.addDetail(new TicketDetail(UUID.randomUUID().toString(), "Venkat Subramaniam", "Speaker"));

        Files.write(Paths.get("ticket_test.pdf"), new TicketExporter().exportTicket(data, EN));
    }

}