package site.model;

import java.io.Serial;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

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

    @OneToMany(mappedBy = "speaker", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY,
        targetEntity = Submission.class)
    @JsonIgnore
    private Set<Submission> submissions = new HashSet<>();

    @OneToMany(mappedBy = "coSpeaker", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY,
        targetEntity = Submission.class)
    @JsonIgnore
    private Set<Submission> coSpeakerSubmissions = new HashSet<>();

    @Transient
    private SubmissionStatus status;

    @Transient
    private boolean featured;

    @Transient
    private Branch branch;

    @Transient
    private String speakerType;

    @Transient
    private int numberOfSubmissions;

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

    @JsonIgnore
    public List<Submission> getAllSubmissions() {
        return Stream.concat(getSubmissions().stream(), getCoSpeakerSubmissions().stream())
            .sorted(Comparator.comparing(s -> s.getBranch().getYear()))
            .toList();
    }

    public List<Submission> branchSubmissions(String branch) {
        return Stream.concat(getSubmissions().stream(), getCoSpeakerSubmissions().stream())
            .filter(s -> s.getBranch().getLabel().equals(branch))
            .toList();
    }

    public void setSubmissions(final Set<Submission> submissions) {
        this.submissions = submissions;
    }

    public Set<Submission> getCoSpeakerSubmissions() {
        return coSpeakerSubmissions;
    }

    public void setCoSpeakerSubmissions(Set<Submission> coSpeakerSubmissions) {
        this.coSpeakerSubmissions = coSpeakerSubmissions;
    }

    public String getBsky() {
        return bsky;
    }

    public void setBsky(String bsky) {
        this.bsky = bsky;
    }

    public String getSpeakerType() {
        return speakerType;
    }

    public void setSpeakerType(String speakerType) {
        this.speakerType = speakerType;
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

    @JsonIgnore
    public int getNumberOfSubmissions() {
        return numberOfSubmissions;
    }

    public void setNumberOfSubmissions(int numberOfSubmissions) {
        this.numberOfSubmissions = numberOfSubmissions;
    }

    public Speaker updateFlags(Branch branch) {
        Collection<Submission> currentSubmissions =
            Stream.concat(this.getSubmissions().stream(), this.getCoSpeakerSubmissions().stream())
                .filter(s -> s.getBranch().equals(branch))
                .toList();
        this.featured = currentSubmissions.stream().anyMatch(s -> Boolean.TRUE.equals(s.getFeatured()));
        this.status = currentSubmissions.stream().map(Submission::getStatus).max(
            Comparator.comparing(SubmissionStatus::ordinal)).orElse(SubmissionStatus.ABORTED);

        this.branch = Stream.concat(this.getSubmissions().stream(), this.getCoSpeakerSubmissions().stream())
            .map(Submission::getBranch)
            .max(Comparator.comparing(Branch::getYear))
            .orElse(null);

        List<String> speakerTypeList = currentSubmissions.stream()
            .map(s -> s.getSpeaker().getId().equals(this.getId()) ? "Speaker" : "Co-Speaker")
            .distinct()
            .sorted(Comparator.reverseOrder())
            .toList();

        this.speakerType = String.join("/", speakerTypeList);
        this.numberOfSubmissions = currentSubmissions.size();

        return this;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    @JsonIgnore
    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public SubmissionStatus getStatus() {
        return status;
    }

    public void setStatus(SubmissionStatus status) {
        this.status = status;
    }
}
