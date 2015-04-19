package site.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Speaker extends User {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Column(length = 1024)
    private String bio;

    private String headline;

    private String twitter;

    private Boolean featured = false;

    @Lob
    private byte[] picture;

    @OneToMany(mappedBy = "speaker", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, targetEntity = Submission.class)
    private Set<Submission> submissions = new HashSet<>();

    public Speaker() {
    }

    public Speaker(String firstName, String lastName, String email, String headline, String twitter, boolean featured) {
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        this.headline = headline;
        this.twitter = twitter;
        this.featured = featured;
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
}
