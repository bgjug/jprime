package site.controller.invoice;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_EVEN;
import static site.controller.invoice.InvoiceData.DEFAULT_DESCRIPTION_BG;
import static site.controller.invoice.InvoiceData.VAT_DECREASE_RATIO;

public class InvoiceDetail {

    private Integer idx;

    private BigDecimal singlePriceWithVAT;
    private Integer passQty;
    private String description = DEFAULT_DESCRIPTION_BG;
    public InvoiceDetail(BigDecimal singlePriceWithVAT, Integer passQty) {
        this.singlePriceWithVAT = singlePriceWithVAT;
        this.passQty = passQty;
    }

    public InvoiceDetail(BigDecimal singlePriceWithVAT, Integer passQty, String description) {
        this.singlePriceWithVAT = singlePriceWithVAT;
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

    public BigDecimal getSinglePriceWithVAT() {
        return singlePriceWithVAT;
    }

    public void setSinglePriceWithVAT(double singlePriceWithVAT) {
        this.singlePriceWithVAT = BigDecimal.valueOf(singlePriceWithVAT);
    }

    public void setSinglePriceWithVAT(BigDecimal singlePriceWithVAT) {
        this.singlePriceWithVAT = singlePriceWithVAT;
    }

    /**
     * Calculates price for single item without VAT
     * @return
     */
    public BigDecimal getPrice() {
        return getTotalPrice().divide(BigDecimal.valueOf(passQty), 2, HALF_EVEN);
    }

    public BigDecimal getTotalPrice() {
        return singlePriceWithVAT.multiply(BigDecimal.valueOf(passQty))
                                 .divide(VAT_DECREASE_RATIO, 2, HALF_EVEN);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
