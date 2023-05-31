package site.model;

import jakarta.persistence.*;

/**
 * A person visiting the conference.
 * @author Mihail Stoynov
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class VisitorJPro extends AbstractEntity {

    private String name;
    private String email;
    private String company;

    private boolean isPresent;

    @Enumerated(EnumType.STRING)
    private VisitorStatus status;

    public VisitorJPro() {
    }

    public VisitorJPro(String name, String email,String company) {
        this.name = name;
        this.email = email;
        this.company = company;
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
     * Check https://github.com/bgjug/jprime/issues/28
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


    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }

    @Override
    public String toString() {
        return """
                VisitorJPro{\
                name='\
                """ + name + '\'' +
                ", email='" + email + '\'' +
                ", company='" + company + '\'' +
                ", isPresent='" + isPresent + '\'' +
                '}';
    }

}
