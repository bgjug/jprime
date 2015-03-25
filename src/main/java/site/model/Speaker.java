package site.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import java.util.Set;
import java.util.HashSet;

import site.model.Submission;

import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Speaker extends User {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String bio;

    private String headline;

    private String twitter;

    @Lob
    private byte[] picture;

    @OneToMany(mappedBy = "speaker", fetch = FetchType.LAZY, targetEntity = Submission.class)
    private Set<Submission> submissions = new HashSet<>();

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
