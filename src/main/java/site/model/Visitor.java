package site.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.StringUtils;

/**
 * A person visiting the conference.
 * @author Mihail Stoynov
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Visitor extends AbstractEntity {

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "registrant", foreignKey = @ForeignKey(name = "FK_registrant_visitor"))
    private Registrant registrant;
    private String name;
    private String email;
    private String company;

    private String ticket;

    private boolean isPresent;

    private boolean isRegistered;

    @Enumerated(EnumType.STRING)
    private VisitorStatus status;

    public Visitor() {
        registrant = new Registrant();
    }

    public Visitor(Registrant invoiceTo, String name, String email,String company) {
        this.registrant = invoiceTo;
        this.name = name;
        this.email = email;
        this.company = company;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public Registrant getRegistrant() {
        return registrant;
    }

    public void setRegistrant(Registrant registrant) {
        this.registrant = registrant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        if (name != null) {
            this.name = name.trim();
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * retrieves the company related to the visitor. <br />
     * Check <a href="https://github.com/bgjug/jprime/issues/28">...</a>
     */
    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
        if (company != null) {
            this.company = company.trim();
        }
    }

    public VisitorStatus getStatus() {
        return status;
    }

    public void setStatus(VisitorStatus status) {
        this.status = status;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }

    public String getRegistrantName() {
        return registrant.getName();
    }

    @Override
    public String toString() {
        return "Visitor{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", company='" + company + '\'' +
                ", isPresent='" + isPresent + '\'' +
                ", ticket='" + ticket + '\'' +
                ", isRegistered='" + isRegistered + '\'' +
                '}';
    }

    /**
     * Returns visitor email or if not present the email from registrant
     * @return email address
     */
    public String anyEmail() {
        return StringUtils.isNotBlank(email) ? email : registrant.getEmail();
    }
}
