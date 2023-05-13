package site.controller.ticket;

import java.util.ArrayList;
import java.util.List;

public class TicketData {
    private final List<TicketDetail> details = new ArrayList<>();

    private String organizer = "JPrime Events";

    private String event;

    public List<TicketDetail> getDetails() {
        return details;
    }

    public void addDetail(TicketDetail detail) {
        this.details.add(detail);
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
