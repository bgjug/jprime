package site.model;

import javax.persistence.*;

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

    public Visitor() {}

    public Visitor(Registrant invoiceTo, String name, String email) {
        this.registrant = invoiceTo;
        this.name = name;
        this.email = email;
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
}