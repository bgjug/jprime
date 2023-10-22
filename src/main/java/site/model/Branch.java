package site.model;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public enum Branch {
    YEAR_2015("2015", LocalDateTime.parse("2015-05-27T00:00:00")), YEAR_2016("2016", LocalDateTime.parse("2016-05-28T00:00:00")),
    YEAR_2017("2017", LocalDateTime.parse("2017-05-30T00:00:00")), YEAR_2018("2018", LocalDateTime.parse("2018-05-29T00:00:00")),
    YEAR_2019("2019", LocalDateTime.parse("2019-05-28T00:00:00")), YEAR_2020("2020", LocalDateTime.parse("2020-05-27T00:00:00")),
    YEAR_2022("2022", LocalDateTime.parse("2022-05-25T00:00:00")),
    YEAR_2023("2023", LocalDateTime.parse("2023-05-30T00:00:00"), LocalDateTime.parse("2022-11-10T00:00:00"),
        LocalDateTime.parse("2023-02-15T23:59:59"), Arrays.asList(SponsorPackage.GOLD, SponsorPackage.PLATINUM, SponsorPackage.GOLD_LITE), true);

    private static final String BRANCH_PREFIX = "YEAR_";

    private final String label;

    private final LocalDateTime startDate;

    private final LocalDateTime cfpOpenDate;

    private final LocalDateTime cfpCloseDate;

    private final Set<SponsorPackage> soldOutPackages;

    private final boolean soldOut;

    Branch(String label, LocalDateTime startDate) {
        this.label = label;
        this.startDate = startDate;
        this.cfpOpenDate = startDate.minusMonths(6).minusDays(20);
        this.cfpCloseDate = startDate.minusMonths(3).minusDays(15);
        soldOutPackages = new HashSet<>(Arrays.asList(SponsorPackage.values()));
        soldOut = false;
    }

    Branch(String label, LocalDateTime startDate, LocalDateTime cfpOpenDate, LocalDateTime cfpCloseDate,
        Collection<SponsorPackage> soldOutPackages, boolean soldOut) {
        this.label = label;
        this.startDate = startDate;
        this.cfpOpenDate = cfpOpenDate;
        this.cfpCloseDate = cfpCloseDate;
        this.soldOutPackages = new HashSet<>(soldOutPackages);
        this.soldOut=soldOut;
    }

    public LocalDateTime getCfpCloseDate() {
        return cfpCloseDate;
    }

    public static Branch valueOfYear(String year) {
        return Branch.valueOf(BRANCH_PREFIX + year);
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    @Override
    public String toString() {
        return label;
    }

    public LocalDateTime getCfpOpenDate() {
        return cfpOpenDate;
    }

    public boolean isSoldOut(SponsorPackage sponsorPackage) {
        return soldOutPackages.contains(sponsorPackage);
    }

    public String getLabel() {
        return label;
    }

    public boolean isSoldOut() {
        return soldOut;
    }
}
