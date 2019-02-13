package site.controller.invoice;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class InvoiceDetail {

    private Integer idx;

    private BigDecimal singlePriceWithVAT = InvoiceData.DEFAULT_TICKET_PRICE;
    private Integer passQty;
    private String description = InvoiceData.DEFAULT_DESCRIPTION_BG;
    public InvoiceDetail(Integer passQty) {
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

    public double getSinglePriceWithVAT() {
        return singlePriceWithVAT.doubleValue();
    }

    public void setSinglePriceWithVAT(double singlePriceWithVAT) {
        this.singlePriceWithVAT = BigDecimal.valueOf(singlePriceWithVAT);
    }

    public Double getPrice() {
        return singlePriceWithVAT.divide(InvoiceData.VAT_DECREASE_RATIO, 2, RoundingMode.UP).doubleValue();//one ticket without VAT
    }

    public Double getTotalPrice() {
        return getPrice() * passQty ;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
