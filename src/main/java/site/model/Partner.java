package site.model;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Partner extends AbstractEntity {
	/**
     * Default serial version uid.
     */
    private static final long serialVersionUID = 1L;
	
    private String companyName;
	
	@Lob
	private byte[] logo;
	
	private String companyWebsite;
	
	private String description;

	private Boolean active;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Partner))
            return false;

        Partner user = (Partner) o;

        if (companyName != null && !companyName.equals(user.companyName))
            return false;
        if (companyWebsite != null && !companyWebsite.equals(user.companyWebsite))
            return false;
        if (description != null && !description.equals(user.description))
            return false;

        return true;
    }

	@Override
    public int hashCode() {
        int result = companyName.hashCode();
        result = 31 * result + companyWebsite.hashCode();
        result = 31 * result + description.hashCode();
        return result;
    }

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public byte[] getLogo() {
		return logo;
	}

	public void setLogo(byte[] logo) {
		this.logo = logo;
	}

	public String getCompanyWebsite() {
		return companyWebsite;
	}

	public void setCompanyWebsite(String companyWebsite) {
		this.companyWebsite = companyWebsite;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getActive() {
		if (active == null)
			return false;
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
}
