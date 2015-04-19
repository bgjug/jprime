package site.controller.epay;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

/**
 * What epay sends us on the back channel.
 * @author Mihail
 */
@Embeddable//because it's used in Registrant
public class EpayResponse extends EpayRaw {
    private boolean isValid;
    private long invoiceNumber;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Date date;
    private String borikaAuthorizationCode;//BCODE
    private String transactionNumberIfPaidByCard;//STAN

    //INVOICE=123456:STATUS=OK - if we want epay to stop spamming us
    //INVOICE=123457:STATUS=ERR - if we want epay to continue spamming us
    private String epayAnswer;


    public boolean isValid() {
        return isValid;
    }

    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }

    public long getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(long invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getBorikaAuthorizationCode() {
        return borikaAuthorizationCode;
    }

    public void setBorikaAuthorizationCode(String borikaAuthorizationCode) {
        this.borikaAuthorizationCode = borikaAuthorizationCode;
    }

    public String getTransactionNumberIfPaidByCard() {
        return transactionNumberIfPaidByCard;
    }

    public void setTransactionNumberIfPaidByCard(String transactionNumberIfPaidByCard) {
        this.transactionNumberIfPaidByCard = transactionNumberIfPaidByCard;
    }

    public String getEpayAnswer() {
        return epayAnswer;
    }

    public void setEpayAnswer(String epayAnswer) {
        this.epayAnswer = epayAnswer;
    }

    @Override
    public String toString() {
        return "EpayResponse{" +
                "isValid=" + isValid +
                ", invoiceNumber=" + invoiceNumber +
                ", status=" + status +
                ", date=" + date +
                ", borikaAuthorizationCode='" + borikaAuthorizationCode + '\'' +
                ", transactionNumberIfPaidByCard='" + transactionNumberIfPaidByCard + '\'' +
                ", epayAnswer='" + epayAnswer + '\'' +
                '}';
    }

    /**
     * Statuses returned by EPAY
     * @author Mihail
     */
    public enum Status {
        PAID,
        EXPIRED,
        DENIED;
    }
}
