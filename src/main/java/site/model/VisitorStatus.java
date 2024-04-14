package site.model;

/**
 * Created by mitia on 03.05.15.
 */
public enum VisitorStatus {
    REQUESTING("Requesting"), PAYED("Payed"), SPONSORED("Sponsored");

    private final String status;

    VisitorStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}
