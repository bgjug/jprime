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
    private Double singlePriceWithVAT_BGN;
    private Double singlePriceWithVAT_EUR;
    private String description;

    public BigDecimal getTotalPrice_BGN() {
        return invoiceDetails.stream()
            .map(InvoiceDetail::getTotalPrice_BGN)
            .reduce(BigDecimal::add)
            .orElse(BigDecimal.valueOf(0.0));
    }

    public BigDecimal getTotalPriceWithVAT_BGN() {
        return invoiceDetails.stream()
            .map(d -> d.getSinglePriceWithVAT_BGN().multiply(new BigDecimal(d.getPassQty())))
            .reduce(BigDecimal::add)
            .orElse(BigDecimal.valueOf(0.0));
    }

    public BigDecimal getTotalPrice_EUR() {
        return invoiceDetails.stream()
            .map(InvoiceDetail::getTotalPrice_EUR)
            .reduce(BigDecimal::add)
            .orElse(BigDecimal.valueOf(0.0));
    }

    public BigDecimal getTotalPriceWithVAT_EUR() {
        return invoiceDetails.stream()
            .map(d -> d.getSinglePriceWithVAT_EUR().multiply(new BigDecimal(d.getPassQty())))
            .reduce(BigDecimal::add)
            .orElse(BigDecimal.valueOf(0.0));
    }

    public BigDecimal getTotalPriceVAT_BGN() {
        return getTotalPriceWithVAT_BGN().subtract(getTotalPrice_BGN());
    }

    public BigDecimal getTotalPriceVAT_EUR() {
        return getTotalPriceWithVAT_EUR().subtract(getTotalPrice_EUR());
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

    public Double getSinglePriceWithVAT_BGN() {
        return invoiceDetails.isEmpty() ? singlePriceWithVAT_BGN : invoiceDetails.get(0).getSinglePriceWithVAT_BGN().doubleValue();
    }

    public Double getSinglePriceWithVAT_EUR() {
        return invoiceDetails.isEmpty() ? singlePriceWithVAT_EUR : invoiceDetails.get(0).getSinglePriceWithVAT_EUR().doubleValue();
    }

    public void setSinglePriceWithVAT_BGN(Double singlePriceWithVAT) {
        if (invoiceDetails.isEmpty()) {
            this.singlePriceWithVAT_BGN = singlePriceWithVAT;
        } else {
            invoiceDetails.get(0).setSinglePriceWithVAT_BGN(singlePriceWithVAT);
        }
    }


    public void setSinglePriceWithVAT_EUR(Double singlePriceWithVAT) {
        if (invoiceDetails.isEmpty()) {
            this.singlePriceWithVAT_EUR = singlePriceWithVAT;
        } else {
            invoiceDetails.get(0).setSinglePriceWithVAT_EUR(singlePriceWithVAT);
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
