package site.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import java.util.Objects;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public class Sponsor extends User {
	
	@Enumerated(EnumType.STRING)
	private SponsorPackage sponsorPackage;

	private String companyName;
	
	@Lob
	private byte[] logo;
	
	private String companyWebsite;
	
	private String description;

	private Boolean active;

    public Sponsor() {
    }

    public Sponsor(SponsorPackage sponsorPackage, String companyName, String companyWebsite, String email) {
        this(sponsorPackage, companyName, companyWebsite, email, true);
    }

    public Sponsor(SponsorPackage sponsorPackage, String companyName, String companyWebsite, String email, Boolean active) {
        this.sponsorPackage = sponsorPackage;
        this.companyName = companyName;
        this.companyWebsite = companyWebsite;
        setEmail(email);
        this.active = active;
    }

    public SponsorPackage getSponsorPackage() {
		return sponsorPackage;
	}

	public void setSponsorPackage(SponsorPackage sponsorPackage) {
		this.sponsorPackage = sponsorPackage;
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

	public Boolean getActive() {
        if (active == null)
            return false;
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Sponsor))
            return false;

        Sponsor sponsor = (Sponsor) o;

        if (!companyName.equals(sponsor.companyName))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 31;
        result = 31 * result + companyName.hashCode();
        return result;
    }
}
