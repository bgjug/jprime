package site.controller.branch;

import java.math.BigDecimal;

public class TicketPriceForm {

    private BigDecimal regularPrice = BigDecimal.ZERO;

    private BigDecimal studentPrice = BigDecimal.ZERO;

    private BigDecimal earlyBirdPrice = BigDecimal.ZERO;

    public BigDecimal getRegularPrice() {
        return regularPrice;
    }

    public void setRegularPrice(BigDecimal regularPrice) {
        this.regularPrice = regularPrice;
    }

    public BigDecimal getStudentPrice() {
        return studentPrice;
    }

    public void setStudentPrice(BigDecimal studentPrice) {
        this.studentPrice = studentPrice;
    }

    public BigDecimal getEarlyBirdPrice() {
        return earlyBirdPrice;
    }

    public void setEarlyBirdPrice(BigDecimal earlyBirdPrice) {
        this.earlyBirdPrice = earlyBirdPrice;
    }
    }
