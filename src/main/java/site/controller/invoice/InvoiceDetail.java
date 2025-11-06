package site.controller.invoice;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_EVEN;
import static site.controller.invoice.InvoiceData.VAT_DECREASE_RATIO;

public class InvoiceDetail {

    private Integer idx;

    private BigDecimal singlePriceWithVAT_BGN;
    private BigDecimal singlePriceWithVAT_EUR;
    private Integer passQty;
    private String description;
    public InvoiceDetail(BigDecimal singlePriceWithVAT_BGN, BigDecimal singlePriceWithVAT_EUR, Integer passQty) {
        this.singlePriceWithVAT_BGN = singlePriceWithVAT_BGN;
        this.singlePriceWithVAT_EUR = singlePriceWithVAT_EUR;
        this.passQty = passQty;
    }

    public InvoiceDetail(BigDecimal singlePriceWithVAT_BGN, BigDecimal singlePriceWithVAT_EUR, Integer passQty, String description) {
        this.singlePriceWithVAT_BGN = singlePriceWithVAT_BGN;
        this.singlePriceWithVAT_EUR = singlePriceWithVAT_EUR;
        this.passQty = passQty;
        this.description = description;
    }

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
    }

    public Integer getPassQty() {
        return passQty;
    }

    public void setPassQty(Integer passQty) {
        this.passQty = passQty;
    }

    public BigDecimal getSinglePriceWithVAT_BGN() {
        return singlePriceWithVAT_BGN;
    }

    public void setSinglePriceWithVAT_BGN(double singlePriceWithVAT) {
        this.singlePriceWithVAT_BGN = BigDecimal.valueOf(singlePriceWithVAT);
    }

    public void setSinglePriceWithVAT_EUR(double singlePriceWithVAT) {
        this.singlePriceWithVAT_EUR = BigDecimal.valueOf(singlePriceWithVAT);
    }

    public void setSinglePriceWithVAT_BGN(BigDecimal singlePriceWithVAT) {
        this.singlePriceWithVAT_BGN = singlePriceWithVAT;
    }

    public BigDecimal getSinglePriceWithVAT_EUR() {
        return singlePriceWithVAT_EUR;
    }

    public void setSinglePriceWithVAT_EUR(BigDecimal singlePriceWithVAT_EUR) {
        this.singlePriceWithVAT_EUR = singlePriceWithVAT_EUR;
    }

    /**
     * Calculates price for single item without VAT
     * @return
     */
    public BigDecimal getPrice_BGN() {
        return getTotalPrice_BGN().divide(BigDecimal.valueOf(passQty), 2, HALF_EVEN);
    }
    public BigDecimal getPrice_EUR() {
        return getTotalPrice_EUR().divide(BigDecimal.valueOf(passQty), 2, HALF_EVEN);
    }

    public BigDecimal getTotalPrice_BGN() {
        return singlePriceWithVAT_BGN.multiply(BigDecimal.valueOf(passQty))
                                 .divide(VAT_DECREASE_RATIO, 2, HALF_EVEN);
    }

    public BigDecimal getTotalPrice_EUR() {
        return singlePriceWithVAT_EUR.multiply(BigDecimal.valueOf(passQty))
            .divide(VAT_DECREASE_RATIO, 2, HALF_EVEN);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
