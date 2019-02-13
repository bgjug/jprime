package site.controller.invoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import site.config.Globals;
import site.model.Registrant;
import site.model.Visitor;

/**
 * DTO for the PDF
 * NOTE: Checkout some of the getters, some magic happens there
 */
public class InvoiceData {

    public static final BigDecimal DEFAULT_TICKET_PRICE = BigDecimal.valueOf(140D);
    public static final BigDecimal STUDENT_TICKET_PRICE = BigDecimal.valueOf(100D);

    static final String DEFAULT_DESCRIPTION_BG = "jPrime " + Globals.CURRENT_BRANCH.toString() + " билет за конференция";
    static final String STUDENT_DESCRIPTION_BG = "jPrime " + Globals.CURRENT_BRANCH.toString() + " билет за конференция - студентски";

    static final BigDecimal VAT_DECREASE_RATIO = BigDecimal.valueOf(1.2D);

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static final String ORIGINAL_BG = "Оригинал";
    public static final String PROFORMA_BG = "Проформа";

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
    private List<InvoiceDetail> invoiceDetails = new ArrayList<>();

    public Double getTotalPrice() {
        return invoiceDetails.stream()
                             .map(InvoiceDetail::getTotalPrice)
                             .reduce((d, d1) -> d + d1)
                             .orElse(0.0);
    }

    public Double getTotalPriceWithVAT() {
        return invoiceDetails.stream()
                             .map(d -> d.getSinglePriceWithVAT() * d.getPassQty())
                             .reduce((d, d1) -> d + d1)
                             .orElse(0.0);
    }

    public Double getTotalPriceVAT() {
        return getTotalPriceWithVAT() - getTotalPrice();
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

        long studentTickets = registrant.getVisitors().stream().filter(Visitor::isStudent).count();
        long regularTickets = registrant.getVisitors().size() - studentTickets;

        if (regularTickets > 0) {
            result.addInvoiceDetail(new InvoiceDetail((int) regularTickets));
        }

        if (studentTickets > 0) {
            result.addInvoiceDetail(
                new InvoiceDetail(STUDENT_TICKET_PRICE, (int) studentTickets, STUDENT_DESCRIPTION_BG));
        }

        return result;
    }

    public void addInvoiceDetail(InvoiceDetail invoiceDetail) {
        invoiceDetails.add(invoiceDetail);
        invoiceDetail.setIdx(invoiceDetails.size());
    }

    public List<InvoiceDetail> getInvoiceDetails() {
        return invoiceDetails;
    }
}