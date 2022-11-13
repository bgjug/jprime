package site.model;

public enum SessionType {
    CONFERENCE_SESSION("Conference session"), DEEP_DIVE("Deep Dive"), WORKSHOP("Workshop");

    private final String type;

    SessionType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
