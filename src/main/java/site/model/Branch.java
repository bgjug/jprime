package site.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTime;

public enum Branch {
    YEAR_2015("2015", DateTime.parse("2015-05-27")), YEAR_2016("2016", DateTime.parse("2016-05-28")),
    YEAR_2017("2017", DateTime.parse("2017-05-30")), YEAR_2018("2018", DateTime.parse("2018-05-29")),
    YEAR_2019("2019", DateTime.parse("2019-05-28")), YEAR_2020("2020", DateTime.parse("2020-05-27")),
    YEAR_2022("2022", DateTime.parse("2022-05-25")),
    YEAR_2023("2023", DateTime.parse("2023-05-30"), DateTime.parse("2022-11-10"),
        DateTime.parse("2023-02-15"), Collections.emptySet());
    // Arrays.asList(SponsorPackage.PLATINUM, SponsorPackage.GOLD_LIGHT)

    private static final String BRANCH_PREFIX = "YEAR_";

    private final String label;

    private final DateTime startDate;

    private final DateTime cfpOpenDate;

    private final DateTime cfpCloseDate;

    private final Set<SponsorPackage> soldOutPackages;

    Branch(String label, DateTime startDate) {
        this.label = label;
        this.startDate = startDate;
        this.cfpOpenDate = startDate.minusMonths(6).minusDays(20);
        this.cfpCloseDate = startDate.minusMonths(3).minusDays(15);
        soldOutPackages = new HashSet<>(Arrays.asList(SponsorPackage.values()));
    }

    Branch(String label, DateTime startDate, DateTime cfpOpenDate, DateTime cfpCloseDate,
        Collection<SponsorPackage> soldOutPackages) {
        this.label = label;
        this.startDate = startDate;
        this.cfpOpenDate = cfpOpenDate;
        this.cfpCloseDate = cfpCloseDate;
        this.soldOutPackages = new HashSet<>(soldOutPackages);
    }

    public DateTime getCfpCloseDate() {
        return cfpCloseDate;
    }

    public static Branch valueOfYear(String year) {
        return Branch.valueOf(BRANCH_PREFIX + year);
    }

    public DateTime getStartDate() {
        return startDate;
    }

    @Override
    public String toString() {
        return label;
    }

    public DateTime getCfpOpenDate() {
        return cfpOpenDate;
    }

    public boolean isSoldOut(SponsorPackage sponsorPackage) {
        return soldOutPackages.contains(sponsorPackage);
    }
}
