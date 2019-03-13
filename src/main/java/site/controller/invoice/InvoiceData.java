package site.controller.invoice;

import site.config.Globals;
import site.model.Registrant;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * DTO for the PDF
 * NOTE: Checkout some of the getters, some magic happens there
 */
public class InvoiceData {

    public static final BigDecimal DEFAULT_TICKET_PRICE = BigDecimal.valueOf(200D);
    public static final BigDecimal STUDENT_TICKET_PRICE = BigDecimal.valueOf(100D);
    private static final BigDecimal VAT_DECREASE_RATIO = BigDecimal.valueOf(1.2D);
    private static final String DEFAULT_DESCRIPTION_BG = "jPrime "+ Globals.CURRENT_BRANCH.toString() + " билет за конференция";
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
    private BigDecimal singlePriceWithVAT = DEFAULT_TICKET_PRICE;
    private Integer passQty;
    private String paymentType;
    private String description = DEFAULT_DESCRIPTION_BG;

    private Long registrantId;

    public double getSinglePriceWithVAT() {
        return singlePriceWithVAT.doubleValue();
    }

    public void setSinglePriceWithVAT(double singlePriceWithVAT) {
        this.singlePriceWithVAT = BigDecimal.valueOf(singlePriceWithVAT);
    }

    public Double getPrice() {
        return singlePriceWithVAT.divide(VAT_DECREASE_RATIO, 2, RoundingMode.UP).doubleValue();//one ticket without VAT
    }

    public Double getTotalPrice() {
        return getPrice() * passQty ;
    }

    public Double getTotalPriceWithVAT() {
        return singlePriceWithVAT.doubleValue() * passQty;
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

    public Integer getPassQty() {
        return passQty;
    }

    public void setPassQty(Integer passQty) {
        this.passQty = passQty;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        result.setPassQty(registrant.getVisitors().size());
        result.setRegistrantId(registrant.getId());
        result.setPaymentType(registrant.getPaymentType() != null ?
                registrant.getPaymentType().getBulgarianValue() :
                "");
        result.setInvoiceNumber(registrant.getRealInvoiceNumber() + "");
        result.setInvoiceDate(LocalDate.now().format(FORMATTER));
        if (registrant.isStudent()) {
            result.setSinglePriceWithVAT(STUDENT_TICKET_PRICE.intValue());
        }
        return result;
    }
}