package site.controller.branch;

import java.time.Duration;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;

import site.controller.DateUtils;
import site.model.Branch;
import site.model.SponsorPackage;
import site.model.TicketPrice;
import site.model.TicketType;

public class BranchForm {

    private static final Set<Duration> VALID_DURATIONS = Set.of(Duration.ofHours(4), Duration.ofDays(1), Duration.ofDays(2), Duration.ofDays(3), Duration.ofDays(4), Duration.ofDays(5));

    protected static final Map<String, Duration> DURATIONS_MAP = VALID_DURATIONS.stream().collect(
        Collectors.toMap(DateUtils::formatDuration, Function.identity()));

    @Min(2015)
    @Max(2050)
    private Integer year;

    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date startDate;

    @NotBlank
    private String duration;

    private TicketPriceForm ticketPrices = new TicketPriceForm();

    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date cfpOpenDate;

    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date cfpCloseDate;

    private boolean soldOut;

    private boolean agendaPublished;

    private boolean platinumSoldOut;

    private boolean goldSoldOut;

    private boolean goldLiteSoldOut;

    private boolean goldOpenSoldOut;

    private boolean silverSoldOut;

    private boolean coOrgSoldOut;

    public BranchForm() {
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public TicketPriceForm getTicketPrices() {
        return ticketPrices;
    }

    public void setTicketPrices(TicketPriceForm ticketPrices) {
        this.ticketPrices = ticketPrices;
    }

    public Date getCfpOpenDate() {
        return cfpOpenDate;
    }

    public void setCfpOpenDate(Date cfpOpenDate) {
        this.cfpOpenDate = cfpOpenDate;
    }

    public Date getCfpCloseDate() {
        return cfpCloseDate;
    }

    public void setCfpCloseDate(Date cfpCloseDate) {
        this.cfpCloseDate = cfpCloseDate;
    }

    public boolean isSoldOut() {
        return soldOut;
    }

    public void setSoldOut(boolean soldOut) {
        this.soldOut = soldOut;
    }

    public boolean isAgendaPublished() {
        return agendaPublished;
    }

    public void setAgendaPublished(boolean agendaPublished) {
        this.agendaPublished = agendaPublished;
    }

    public boolean isPlatinumSoldOut() {
        return platinumSoldOut;
    }

    public void setPlatinumSoldOut(boolean platinumSoldOut) {
        this.platinumSoldOut = platinumSoldOut;
    }

    public boolean isGoldSoldOut() {
        return goldSoldOut;
    }

    public void setGoldSoldOut(boolean goldSoldOut) {
        this.goldSoldOut = goldSoldOut;
    }

    public boolean isGoldLiteSoldOut() {
        return goldLiteSoldOut;
    }

    public void setGoldLiteSoldOut(boolean goldLiteSoldOut) {
        this.goldLiteSoldOut = goldLiteSoldOut;
    }

    public boolean isGoldOpenSoldOut() {
        return goldOpenSoldOut;
    }

    public void setGoldOpenSoldOut(boolean goldOpenSoldOut) {
        this.goldOpenSoldOut = goldOpenSoldOut;
    }

    public boolean isSilverSoldOut() {
        return silverSoldOut;
    }

    public void setSilverSoldOut(boolean silverSoldOut) {
        this.silverSoldOut = silverSoldOut;
    }

    public boolean isCoOrgSoldOut() {
        return coOrgSoldOut;
    }

    public void setCoOrgSoldOut(boolean coOrgSoldOut) {
        this.coOrgSoldOut = coOrgSoldOut;
    }

    public static BranchForm fromBranchAndPriceMap(Branch branch,
        Map<TicketType, TicketPrice> priceMap) {
        BranchForm branchForm = new BranchForm();
        branchForm.duration = branch.getDurationString();
        branchForm.year = branch.getYear();
        branchForm.startDate = branch.getStartDateTime();
        branchForm.cfpOpenDate = branch.getCfpOpenDateTime();
        branchForm.cfpCloseDate = branch.getCfpCloseDateTime();
        branchForm.soldOut = branch.isSoldOut();
        branchForm.agendaPublished = branch.isAgendaPublished();
        branchForm.platinumSoldOut = branch.soldOutPackages().contains(SponsorPackage.PLATINUM);
        branchForm.goldSoldOut = branch.soldOutPackages().contains(SponsorPackage.GOLD);
        branchForm.goldLiteSoldOut = branch.soldOutPackages().contains(SponsorPackage.GOLD_LITE);
        branchForm.goldOpenSoldOut = branch.soldOutPackages().contains(SponsorPackage.GOLD_OPEN);
        branchForm.silverSoldOut = branch.soldOutPackages().contains(SponsorPackage.SILVER);
        branchForm.coOrgSoldOut = branch.soldOutPackages().contains(SponsorPackage.CO_ORG);

        updatePrices(priceMap, branchForm);

        return branchForm;
    }

    public static void updatePrices(Map<TicketType, TicketPrice> priceMap, BranchForm branchForm) {
        branchForm.ticketPrices.setRegularPrice(priceMap.get(TicketType.REGULAR).getPrice());
        branchForm.ticketPrices.setStudentPrice(priceMap.get(TicketType.STUDENT).getPrice());
        branchForm.ticketPrices.setEarlyBirdPrice(priceMap.get(TicketType.EARLY_BIRD).getPrice());
    }

    public Branch toBranch() {
        Branch branch = new Branch();
        branch.setYear(year);

        branch.setDuration(DURATIONS_MAP.get(duration));
        branch.setStartDate(DateUtils.toLocalDateTime(startDate));
        branch.setCfpOpenDate(DateUtils.toLocalDateTime(cfpOpenDate));
        branch.setCfpCloseDate(DateUtils.toLocalDateTime(cfpCloseDate).plusDays(1).minusSeconds(1));

        branch.setAgendaPublished(agendaPublished);
        branch.setSoldOut(soldOut);
        Set<SponsorPackage> soldOutPackages = new HashSet<>();
        if (platinumSoldOut) {
            soldOutPackages.add(SponsorPackage.PLATINUM);
        }
        if (goldSoldOut) {
            soldOutPackages.add(SponsorPackage.GOLD);
        }
        if (goldLiteSoldOut) {
            soldOutPackages.add(SponsorPackage.GOLD_LITE);
        }
        if (goldOpenSoldOut) {
            soldOutPackages.add(SponsorPackage.GOLD_OPEN);
        }
        if (silverSoldOut) {
            soldOutPackages.add(SponsorPackage.SILVER);
        }
        if (coOrgSoldOut) {
            soldOutPackages.add(SponsorPackage.CO_ORG);
        }
        branch.soldOutPackages(soldOutPackages);
        return branch;
    }

    public List<TicketPrice> toTicketPrices(Branch branch) {
        return List.of(new TicketPrice(ticketPrices.getRegularPrice(), TicketType.REGULAR, branch),
            new TicketPrice(ticketPrices.getStudentPrice(), TicketType.STUDENT, branch),
            new TicketPrice(ticketPrices.getEarlyBirdPrice(), TicketType.EARLY_BIRD, branch,
                branch.getCfpOpenDate(), branch.getCfpCloseDate()));
    }
}
