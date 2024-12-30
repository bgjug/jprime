package site.model;

import java.math.BigDecimal;

public enum SponsorPackage {
    PLATINUM("Platinum", true, BigDecimal.valueOf(8500.0)), GOLD("Gold", true, BigDecimal.valueOf(6500.0)), GOLD_LITE("Gold Lite", true,
        BigDecimal.valueOf(5300.0)),
    GOLD_OPEN("Gold Open", true, BigDecimal.valueOf(4500.0)), SILVER("Silver", true,
        BigDecimal.valueOf(2000.0)), CO_ORG("Co-Org", false, BigDecimal.valueOf(10000.0));

    private final String packageName;

    private final boolean showOnSite;

    private final BigDecimal price;

    SponsorPackage(String packageName, boolean showOnSite, BigDecimal price) {
        this.packageName = packageName;
        this.showOnSite = showOnSite;
        this.price = price;
    }

    public String getPackageName() {
        return packageName;
    }

    public boolean isShowOnSite() {
        return showOnSite;
    }
}
