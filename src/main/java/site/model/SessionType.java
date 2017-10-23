package site.model;

public enum SessionType {
    ConferenceSession("Conference session"), Workshop("Workshop");

    private final String type;

    private SessionType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
