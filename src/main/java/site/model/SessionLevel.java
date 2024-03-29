package site.model;

/**
 * @author Ivan St. Ivanov
 */
public enum SessionLevel {

    BEGINNER("Beginner"), INTERMEDIATE("Intermediate"), ADVANCED("Advanced");

    private final String level;

    SessionLevel(String level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return level;
    }
}
