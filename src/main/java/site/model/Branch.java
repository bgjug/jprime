package site.model;

public enum Branch {
	YEAR_2015("2015"), YEAR_2016("2016"), YEAR_2017("2017"), YEAR_2018("2018");

	private static final String BRANCH_PREFIX = "YEAR_";
	
    private String label;

    private Branch(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
    
    public static Branch valueOfYear(String year) {
        return Branch.valueOf(BRANCH_PREFIX + year);
    }
}
