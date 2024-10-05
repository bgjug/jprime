package site.controller.invoice;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import site.config.Globals;
import site.model.Branch;
import site.model.Registrant;

/**
 * DTO for the PDF
 * NOTE: Checkout some of the getters, some magic happens there
 */
public class InvoiceData {

    public static final String DEFAULT_DESCRIPTION_BG = "jPrime "+ Globals.CURRENT_BRANCH + " билет за конференция";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static final BigDecimal VAT_DECREASE_RATIO = BigDecimal.valueOf(1.2D);

    public static final String ORIGINAL_BG = "Оригинал";
    public static final String PROFORMA_BG = "Проформа";

    private static final Map<Branch, TicketPrices> TICKET_PRICE_MAP = MapUtils.putAll(new HashMap<>(),
        new Object[] {
            Branch.YEAR_2024,
            new TicketPrices(BigDecimal.valueOf(280D), BigDecimal.valueOf(180D), BigDecimal.valueOf(100D)),
            Branch.YEAR_2025,
            new TicketPrices(BigDecimal.valueOf(280D), BigDecimal.valueOf(180D), BigDecimal.valueOf(100D))
        });

    private String invoiceNumber;
    private String invoiceDate;
    private String invoiceType;
    private String client;
    private String clientAddress;
    private String clientEIK;
    private String clientVAT;
    private String mol;
    private String paymentType;
    private Long registrantId;
    private final List<InvoiceDetail> invoiceDetails = new ArrayList<>();
    // Used to store data from model
    private Double singlePriceWithVAT;
    private String description;

    public BigDecimal getTotalPrice() {
        return invoiceDetails.stream()
            .map(InvoiceDetail::getTotalPrice)
            .reduce(BigDecimal::add)
            .orElse(BigDecimal.valueOf(0.0));
    }

    public BigDecimal getTotalPriceWithVAT() {
        return invoiceDetails.stream()
            .map(d -> d.getSinglePriceWithVAT().multiply(new BigDecimal(d.getPassQty())))
            .reduce(BigDecimal::add)
            .orElse(BigDecimal.valueOf(0.0));
    }

    public BigDecimal getTotalPriceVAT() {
        return getTotalPriceWithVAT().subtract(getTotalPrice());
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public String getClientEIK() {
        return clientEIK;
    }

    public void setClientEIK(String clientEIK) {
        this.clientEIK = clientEIK;
    }

    public String getClientVAT() {
        return clientVAT;
    }

    public void setClientVAT(String clientVAT) {
        this.clientVAT = clientVAT;
    }

    public String getMol() {
        return mol;
    }

    public void setMol(String mol) {
        this.mol = mol;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Long getRegistrantId() {
        return registrantId;
    }

    public void setRegistrantId(Long registrantId) {
        this.registrantId = registrantId;
    }

    public static InvoiceData fromRegistrant(Registrant registrant) {
        InvoiceData result = new InvoiceData();
        result.setClient(registrant.getName());
        result.setClientAddress(registrant.getAddress());
        result.setClientEIK(registrant.getEik());
        if (registrant.getVatNumber() != null) {
            result.setClientVAT(registrant.getVatNumber());
        } else {
            result.setClientVAT("");
        }
        result.setMol(registrant.getMol());
        result.setRegistrantId(registrant.getId());
        result.setPaymentType(
            registrant.getPaymentType() != null ? registrant.getPaymentType().getBulgarianValue() : "");
        result.setInvoiceNumber(registrant.getRealInvoiceNumber() + "");
        result.setInvoiceDate(LocalDate.now().format(FORMATTER));

        int tickets = registrant.getVisitors().size();
        Branch branch = registrant.getBranch();
        TicketPrices ticketPrices = TICKET_PRICE_MAP.get(branch);
        if (registrant.isStudent()) {
            BigDecimal studentPrice = ticketPrices.getStudentPrice();
            result.addInvoiceDetail(
                new InvoiceDetail(studentPrice, tickets, DEFAULT_DESCRIPTION_BG));
        } else {
            BigDecimal ticketPrice = ticketPrices.getPrice(branch);

            LocalDateTime registrationDate =
                registrant.getCreatedDate() != null ? registrant.getCreatedDate() : branch.getCfpOpenDate();

            if (registrationDate.isBefore(branch.getCfpCloseDate()) && Duration.between(
                registrationDate, LocalDateTime.now()).abs().getSeconds() <= Duration.of(3, ChronoUnit.DAYS).getSeconds()) {
                ticketPrice = ticketPrices.getPrice(branch, registrationDate);
            }
            result.addInvoiceDetail(new InvoiceDetail(ticketPrice, tickets));
        }

        return result;
    }

    public static TicketPrices getPrices(Branch branch) {
        return TICKET_PRICE_MAP.get(branch);
    }

    public void addInvoiceDetail(InvoiceDetail invoiceDetail) {
        invoiceDetails.add(invoiceDetail);
        invoiceDetail.setIdx(invoiceDetails.size());
    }

    public List<InvoiceDetail> getInvoiceDetails() {
        return invoiceDetails;
    }

    public Double getSinglePriceWithVAT() {
        return invoiceDetails.isEmpty() ? singlePriceWithVAT : invoiceDetails.get(0).getSinglePriceWithVAT().doubleValue();
    }

    public void setSinglePriceWithVAT(Double singlePriceWithVAT) {
        if (invoiceDetails.isEmpty()) {
            this.singlePriceWithVAT = singlePriceWithVAT;
        } else {
            invoiceDetails.get(0).setSinglePriceWithVAT(singlePriceWithVAT);
        }
    }

    public String getDescription() {
        return invoiceDetails.isEmpty() ? description : invoiceDetails.get(0).getDescription();
    }

    public void setDescription(String description) {
        if (invoiceDetails.isEmpty()) {
            this.description = description;
        } else {
            invoiceDetails.get(0).setDescription(description);
        }
    }

    public static class TicketPrices {

        private final BigDecimal regularPrice;

        private final BigDecimal earlyBirdPrice;

        private final BigDecimal studentPrice;

        public TicketPrices(BigDecimal regularPrice, BigDecimal earlyBirdPrice, BigDecimal studentPrice) {
            this.regularPrice = regularPrice;
            this.earlyBirdPrice = earlyBirdPrice;
            this.studentPrice = studentPrice;
        }

        public BigDecimal getPrice(Branch branch) {
            return getPrice(branch, LocalDateTime.now());
        }

        public BigDecimal getPrice(Branch branch, LocalDateTime atDateTime) {
            atDateTime = atDateTime != null ? atDateTime : LocalDateTime.now();
            return branch.getCfpCloseDate().isAfter(atDateTime) ? earlyBirdPrice : regularPrice;
        }

        public BigDecimal getRegularPrice() {
            return regularPrice;
        }

        public BigDecimal getEarlyBirdPrice() {
            return earlyBirdPrice;
        }

        public BigDecimal getStudentPrice() {
            return studentPrice;
        }
    }
}
