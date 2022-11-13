package site.model;

public enum SessionType {
    ConferenceSession("Conference session"), DeepDive("Deep Dive"), Workshop("Workshop");

    private final String type;

    SessionType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
