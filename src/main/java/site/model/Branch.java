package site.model;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public enum Branch {
    YEAR_2015(2015, LocalDateTime.parse("2015-05-27T00:00:00")), YEAR_2016(2016, LocalDateTime.parse("2016-05-28T00:00:00")),
    YEAR_2017(2017, LocalDateTime.parse("2017-05-30T00:00:00")), YEAR_2018(2018, LocalDateTime.parse("2018-05-29T00:00:00")),
    YEAR_2019(2019, LocalDateTime.parse("2019-05-28T00:00:00")), YEAR_2020(2020, LocalDateTime.parse("2020-05-27T00:00:00")),
    YEAR_2022(2022, LocalDateTime.parse("2022-05-25T00:00:00")),
    YEAR_2023(2023, LocalDateTime.parse("2023-05-30T00:00:00"), LocalDateTime.parse("2022-11-10T00:00:00"),
        LocalDateTime.parse("2023-02-15T23:59:59"), Arrays.asList(SponsorPackage.GOLD, SponsorPackage.PLATINUM, SponsorPackage.GOLD_LITE), true),
    YEAR_2024(2024, LocalDateTime.parse("2024-05-28T00:00:00"), LocalDateTime.parse("2023-11-01T00:00:00"),
        LocalDateTime.parse("2024-02-15T23:59:59"), Arrays.asList(SponsorPackage.GOLD, SponsorPackage.PLATINUM, SponsorPackage.GOLD_LITE, SponsorPackage.SILVER), false);

    private static final String BRANCH_PREFIX = "YEAR_";

    private final int year;

    private final LocalDateTime startDate;

    private final LocalDateTime cfpOpenDate;

    private final LocalDateTime cfpCloseDate;

    private final Set<SponsorPackage> soldOutPackages;

    private final boolean soldOut;

    private Branch(int year, LocalDateTime startDate) {
        if (year != startDate.getYear()) {
            throw new IllegalArgumentException("Invalid value for year or startDate");
        }

        this.year = year;
        this.startDate = startDate;
        this.cfpOpenDate = startDate.minusMonths(6).minusDays(20);
        this.cfpCloseDate = startDate.minusMonths(3).minusDays(15);
        soldOutPackages = new HashSet<>(Arrays.asList(SponsorPackage.values()));
        soldOut = false;
    }

    private Branch(int year, LocalDateTime startDate, LocalDateTime cfpOpenDate, LocalDateTime cfpCloseDate,
        Collection<SponsorPackage> soldOutPackages, boolean soldOut) {
        if (year != startDate.getYear()) {
            throw new IllegalArgumentException("Invalid value for year or startDate");
        }

        if (startDate.isBefore(cfpOpenDate)) {
            throw new IllegalArgumentException("Invalid value for cfpOpenDate or startDate");
        }

        if (startDate.isBefore(cfpCloseDate)) {
            throw new IllegalArgumentException("Invalid value for cfpCloseDate or startDate");
        }

        this.year = year;
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
        return Integer.toString(year);
    }

    public LocalDateTime getCfpOpenDate() {
        return cfpOpenDate;
    }

    public boolean isSoldOut(SponsorPackage sponsorPackage) {
        return soldOutPackages.contains(sponsorPackage);
    }

    public String getLabel() {
        return Integer.toString(year);
    }

    public int getYear() {
        return year;
    }

    public boolean isSoldOut() {
        return soldOut;
    }

}
