package site.model;

import java.io.Serial;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Speaker extends User {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 1L;

    @Column(length = 3000, columnDefinition = "TEXT")
    @JsonIgnore
    private String bio;

    @JsonIgnore
    private String headline;

    private String twitter;

    private String bsky;

    @Lob
    @JsonIgnore
    private byte[] picture;

    @OneToMany(mappedBy = "speaker", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, targetEntity = Submission.class)
    @JsonIgnore
    private Set<Submission> submissions = new HashSet<>();

    @Transient
    private boolean accepted;

    @Transient
    private boolean featured;

    public Speaker() {
    }

    public Speaker(String firstName, String lastName, String email, String headline, String twitter) {
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        this.headline = headline;
        this.twitter = twitter;
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

    public String getBsky() {
        return bsky;
    }

    public void setBsky(String bsky) {
        this.bsky = bsky;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Speaker speaker)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        return Objects.equals(this.twitter, speaker.twitter) && Objects.equals(this.bsky, speaker.bsky);
    }

    @Override
    public int hashCode() {
        return 31 * super.hashCode() + Objects.hash(twitter, bsky);
    }

    @Transient
    public String getName() {
        return getFirstName() + " " + getLastName();
    }

    public Speaker updateFlags(Branch branch) {
        Collection<Submission> currentSubmissions = this.getSubmissions().stream().filter(s -> s.getBranch().equals(branch)).toList();
        this.accepted = currentSubmissions.stream().anyMatch(s->s.getStatus() == SubmissionStatus.ACCEPTED);
        this.featured = currentSubmissions.stream().anyMatch(s->Boolean.TRUE.equals(s.getFeatured()));
        return this;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }
}
