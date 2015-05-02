package site.model;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

/**
 * A person visiting the conference.
 * @author Mihail Stoynov
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Visitor extends AbstractEntity {

    @ManyToOne(optional = false)
    private Registrant registrant;
    private String name;
    private String email;
    private String company;

    public Visitor() {}

    public Visitor(Registrant invoiceTo, String name, String email,String company) {
        this.registrant = invoiceTo;
        this.name = name;
        this.email = email;
        this.company = company;
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
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "Visitor{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}