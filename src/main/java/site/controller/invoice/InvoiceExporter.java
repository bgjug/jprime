package site.controller.invoice;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.stereotype.Service;

import site.facade.BranchService;
import site.model.Branch;
import site.model.TicketPrice;

import static org.apache.commons.beanutils.PropertyUtils.getProperty;

/**
 * Created by mitia on 10.04.15.
 */
@Service
public class InvoiceExporter {

    private final BranchService branchService;

    public InvoiceExporter(BranchService branchService) {
        this.branchService = branchService;
    }

    public byte[] exportInvoice(InvoiceData data, boolean isCompany, InvoiceLanguage language) throws
        JRException {
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
        parameters.put("jprime.year", Integer.toString(branchService.getCurrentBranch().getYear()));

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
        data.setClientAddress(
            "1111, Sofia, some улица 1, some entrance, some floor, some apartment and few more symbols");
        data.setClientEIK("2464387775");
        data.setClientVAT("BG2464387775");
        data.setMol("fda");
        data.setInvoiceType("Проформа");
        data.setPaymentType("пеймънт");

        String description = "jPrime 2025 билет за конференция";

        BigDecimal singlePriceWithVAT_Student = BigDecimal.valueOf(65.0);
        BigDecimal singlePriceWithVAT_Regular = BigDecimal.valueOf(160.0);

        data.addInvoiceDetail(
            new InvoiceDetail(singlePriceWithVAT_Student, 3, description));

        data.addInvoiceDetail(
            new InvoiceDetail(singlePriceWithVAT_Regular, 3, description));

        BranchService mockedService = new BranchService(null, null) {

            @Override
            public Branch getCurrentBranch() {
                return new Branch(2025);
            }
        };

        Files.write(Paths.get("invoice_test.pdf"),
            new InvoiceExporter(mockedService).exportInvoice(data, true, InvoiceLanguage.EN));
    }
}
