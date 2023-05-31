package site.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import site.config.Globals;

import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Speaker extends User {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Column(length = 1024)
    @JsonIgnore
    private String bio;

    @JsonIgnore
    private String headline;

    private String twitter;

    private Boolean featured = false;

    private Boolean accepted = false;

    @Lob
    @JsonIgnore
    private byte[] picture;

    @OneToMany(mappedBy = "speaker", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, targetEntity = Submission.class)
    @JsonIgnore
    private Set<Submission> submissions = new HashSet<>();
    
    @Enumerated(EnumType.STRING)
    private Branch branch = Globals.CURRENT_BRANCH;

    public Speaker() {
    }

    public Speaker(String firstName, String lastName, String email, String headline, String twitter, boolean featured, boolean accepted) {
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        this.headline = headline;
        this.twitter = twitter;
        this.featured = featured;
        this.accepted = accepted;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public Set<Submission> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(final Set<Submission> submissions) {
        this.submissions = submissions;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Speaker))
            return false;
        if (!super.equals(o))
            return false;

        Speaker speaker = (Speaker) o;

        if (twitter != null && !twitter.equals(speaker.twitter))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + twitter.hashCode();
        return result;
    }

    @Transient
    public String getName() {
        return getFirstName() + " " + getLastName();
    }
}
