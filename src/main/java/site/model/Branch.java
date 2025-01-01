package site.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import site.controller.DateUtils;

@Entity
@Table(name = "Branch", uniqueConstraints = @UniqueConstraint(name = "UNQ_YEAR", columnNames = {"branch_year"}))
public class Branch extends EntityBase {

    private static final String BRANCH_PREFIX = "YEAR_";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Column(name = "branch_year", nullable = false)
    private int year;

    private LocalDateTime startDate;

    private Duration duration;

    private LocalDateTime cfpOpenDate;

    private LocalDateTime cfpCloseDate;

    @Column(length = 1024)
    private String soldOutPackages;

    private boolean soldOut;

    private boolean currentBranch;

    private boolean agendaPublished;

    public Branch() {
    }

    public Branch(int year) {
        this.year = year;
    }

    public Branch(int year, LocalDateTime startDate, Duration duration, LocalDateTime cfpOpenDate,
        LocalDateTime cfpCloseDate, Set<SponsorPackage> soldOutPackages, boolean soldOut) {
        this.year = year;
        this.startDate = startDate;
        this.duration = duration;
        this.cfpOpenDate = cfpOpenDate;
        this.cfpCloseDate = cfpCloseDate;
        this.soldOutPackages(soldOutPackages);
        this.soldOut = soldOut;
    }

    @Column(name="branch_year")
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Id
    @Column(length = 16)
    public String getLabel() {
        return BRANCH_PREFIX + year;
    }

    public void setLabel(String label) {
        //
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getCfpOpenDate() {
        return cfpOpenDate;
    }

    public void setCfpOpenDate(LocalDateTime cfpOpenDate) {
        this.cfpOpenDate = cfpOpenDate;
    }

    public LocalDateTime getCfpCloseDate() {
        return cfpCloseDate;
    }

    public void setCfpCloseDate(LocalDateTime cfpCloseDate) {
        this.cfpCloseDate = cfpCloseDate;
    }

    public Set<SponsorPackage> soldOutPackages() {
        try {
            return StringUtils.isNotBlank(soldOutPackages) ? OBJECT_MAPPER.readValue(soldOutPackages, new TypeReference<Set<SponsorPackage>>() {}) :
                   Collections.emptySet();
        } catch (JsonProcessingException e) {
            throw new JPrimeException(e);
        }
    }

    public void soldOutPackages(Set<SponsorPackage> soldOutPackages) {
        try {
            this.soldOutPackages =
                soldOutPackages != null ? OBJECT_MAPPER.writeValueAsString(soldOutPackages) : "";
        } catch (JsonProcessingException e) {
            throw new JPrimeException(e);
        }
    }

    public boolean isSoldOut() {
        return soldOut;
    }

    public void setSoldOut(boolean soldOut) {
        this.soldOut = soldOut;
    }

    public String getSoldOutPackages() {
        return soldOutPackages;
    }

    public void setSoldOutPackages(String soldOutPackages) {
        this.soldOutPackages = soldOutPackages;
    }

    public boolean isCurrentBranch() {
        return currentBranch;
    }

    public void setCurrentBranch(boolean currentBranch) {
        this.currentBranch = currentBranch;
    }

    public boolean isAgendaPublished() {
        return agendaPublished;
    }

    public void setAgendaPublished(boolean agendaPublished) {
        this.agendaPublished = agendaPublished;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Branch that))
            return false;
        return getYear() == that.getYear();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getYear());
    }

    @Override
    public String toString() {
        return Integer.toString(year);
    }

    @Transient
    public String getDurationString() {
        return duration != null ? DateUtils.formatDuration(duration) : "Not Set";
    }

    @Transient
    public Date getStartDateTime() {
        return DateUtils.fromLocalDateTime(startDate);
    }

    @Transient
    public Date getCfpOpenDateTime() {
        return DateUtils.fromLocalDateTime(cfpOpenDate);
    }

    @Transient
    public Date getCfpCloseDateTime() {
        return DateUtils.fromLocalDateTime(cfpCloseDate);
    }


}
