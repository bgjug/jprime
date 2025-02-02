package site.model;

import java.util.Objects;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * @author Ivan St. Ivanov
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Submission extends AbstractEntity {

    @NotBlank
    private String title;

    @NotBlank
    @Column(length = 3000)
    private String description;

    @Enumerated(EnumType.STRING)
    private SubmissionStatus status = SubmissionStatus.SUBMITTED;

    @Enumerated(EnumType.STRING)
    private SessionLevel level;

    @Enumerated(EnumType.STRING)
    private SessionType type = SessionType.CONFERENCE_SESSION;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Speaker.class,
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "speaker", nullable = false, referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_submission_speaker"))
    private Speaker speaker = new Speaker();

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Speaker.class,
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "coSpeaker", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_submission_coSpeaker"))
    private Speaker coSpeaker;

    @ManyToOne
    @JoinColumn(name = "branch", referencedColumnName = "label", foreignKey = @ForeignKey(name = "fk_submission_branch"))
    private Branch branch;

    @Transient
    private String captcha;

    private Boolean featured = false;

    public Submission() {
    }

    public Submission(Branch branch) {
        this.branch = branch;
    }

    public Submission(String title, String description, SessionLevel level, SessionType type, Speaker speaker,
        SubmissionStatus status, boolean featured) {
        this.title = title;
        this.description = description;
        this.level = level;
        this.speaker = speaker;
        this.type = type;
        this.status = status;
        this.featured = featured;
    }

    public Submission(String title, String description, SessionLevel level, SessionType type, Speaker speaker,
        Speaker coSpeaker, SubmissionStatus status, boolean featured) {
        this.title = title;
        this.description = description;
        this.level = level;
        this.type = type;
        this.speaker = speaker;
        this.coSpeaker = coSpeaker;
        this.status = status;
        this.featured = featured;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SessionLevel getLevel() {
        return level;
    }

    public void setLevel(SessionLevel level) {
        this.level = level;
    }

    public SubmissionStatus getStatus() {
        return status;
    }

    public void setStatus(SubmissionStatus status) {
        this.status = status;
    }

    public Speaker getSpeaker() {
        return speaker;
    }

    public void setSpeaker(Speaker speaker) {
        this.speaker = speaker;
    }

    public Speaker getCoSpeaker() {
        return coSpeaker;
    }

    public void setCoSpeaker(Speaker coSpeaker) {
        this.coSpeaker = coSpeaker;
    }

    public SessionType getType() {
        return type;
    }

    public void setType(SessionType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Submission submission)) {
            return false;
        }

        if (!Objects.equals(speaker, submission.speaker)) {
            return false;
        }
        return Objects.equals(title, submission.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, speaker);
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Submission branch(Branch branch) {
        this.branch = branch;
        return this;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }
}
