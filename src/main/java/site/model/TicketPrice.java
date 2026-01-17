package site.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "TicketPrice")
public class TicketPrice extends AbstractEntity {

    private BigDecimal price;

    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private TicketType ticketType;

    @ManyToOne
    @JoinColumn(name = "branch", referencedColumnName = "label",
        foreignKey = @ForeignKey(name = "fk_ticket_price_branch"))
    private Branch branch;

    private LocalDateTime validFrom;

    private LocalDateTime validUntil;

    public TicketPrice() {
    }

    public TicketPrice(BigDecimal price, TicketType ticketType) {
        this.price = price;
        this.ticketType = ticketType;
    }

    public TicketPrice(BigDecimal price, TicketType ticketType, Branch branch) {
        this.price = price;
        this.ticketType = ticketType;
        this.branch = branch;
    }

    public TicketPrice(BigDecimal price, TicketType ticketType, LocalDateTime validFrom,
        LocalDateTime validUntil) {
        this.price = price;
        this.ticketType = ticketType;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
    }

    public TicketPrice(BigDecimal price, TicketType ticketType, Branch branch, LocalDateTime validFrom,
        LocalDateTime validUntil) {
        this.price = price;
        this.ticketType = ticketType;
        this.branch = branch;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDateTime validUntil) {
        this.validUntil = validUntil;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TicketPrice that))
            return false;
        return getTicketType() == that.getTicketType() && getBranch() == that.getBranch();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTicketType(), getBranch());
    }

    public TicketPrice branchIfNotSet(Branch branch) {
        if (this.branch != null) {
            return this;
        }
        this.branch = branch;
        return this;
    }
}
