package site.controller.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for the PDF
 * NOTE: Checkout some of the getters, some magic happens there
 */
public class InvoiceData {

    public static final BigDecimal VAT_DECREASE_RATIO = BigDecimal.valueOf(1.2D);

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
    private final List<InvoiceDetail> invoiceDetails = new ArrayList<>();
    // Used to store data from the model
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
}
