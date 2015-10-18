package site.model;

public enum Branch {
	YEAR_2015("2015"), YEAR_2016("2016");

    private String label;

    private Branch(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
