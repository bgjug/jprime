package site.controller.ticket;

public class TicketDetail {
    private String number;

    private String holder;

    private String type;

    public TicketDetail(String number, String holder, String type) {
        this.number = number;
        this.holder = holder;
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
