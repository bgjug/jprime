package site.facade;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Set;

import site.model.Branch;
import site.model.SponsorPackage;
import site.model.TicketPrice;
import site.model.TicketType;

public class DefaultBranchUtil {
    public static void createDefaultBranch(BranchService branchService) {
        if (!branchService.allBranches().isEmpty()) {
            return;
        }

        LocalDateTime startDate = LocalDateTime.of(2024, Month.MAY, 28, 0, 0, 0, 0);
        LocalDateTime cfpOpenDate = LocalDateTime.of(2023, Month.NOVEMBER, 1, 0, 0, 0, 0);
        LocalDateTime cfpCloseDate = LocalDateTime.of(2024, Month.FEBRUARY, 15, 23, 59, 59, 999);
        branchService.createBranch(
            new Branch(2024, startDate, Duration.ofDays(2), cfpOpenDate, cfpCloseDate, Set.of(SponsorPackage.GOLD, SponsorPackage.PLATINUM, SponsorPackage.GOLD_LITE), true),
            List.of(new TicketPrice(BigDecimal.valueOf(280.0), TicketType.REGULAR),
                new TicketPrice(BigDecimal.valueOf(180.0), TicketType.EARLY_BIRD, cfpOpenDate, cfpCloseDate),
                new TicketPrice(BigDecimal.valueOf(100.0), TicketType.STUDENT)));

        startDate = LocalDateTime.of(2025, Month.MAY, 14, 0, 0, 0, 0);
        cfpOpenDate = LocalDateTime.of(2024, Month.NOVEMBER, 1, 0, 0, 0, 0);
        cfpCloseDate = LocalDateTime.of(2025, Month.FEBRUARY, 15, 23, 59, 59, 999);
        Branch branch = branchService.createBranch(
            new Branch(2025, startDate, Duration.ofDays(2), cfpOpenDate, cfpCloseDate, Set.of(), false),
            List.of(new TicketPrice(BigDecimal.valueOf(340.0), TicketType.REGULAR),
                new TicketPrice(BigDecimal.valueOf(230.0), TicketType.EARLY_BIRD, cfpOpenDate, cfpCloseDate),
                new TicketPrice(BigDecimal.valueOf(130.0), TicketType.STUDENT)));
        branchService.setAsCurrent(branch.getLabel());
    }
}
