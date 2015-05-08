package site.model;

/**
 * Created by mitia on 03.05.15.
 */
public enum VisitorStatus {
    REQUESTING("Requesting"), PAYED("Payed"), Sponsored("Sponsored");

    private String status;

    private VisitorStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}
