package site.model;

import site.config.Globals;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
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

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Speaker.class, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
    @JoinColumn(name = "speaker", nullable = false, referencedColumnName = "id")
    private Speaker speaker = new Speaker();

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Speaker.class, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
    @JoinColumn(name = "coSpeaker", nullable = true, referencedColumnName = "id")
    private Speaker coSpeaker;

    @Enumerated(EnumType.STRING)
    private Branch branch = Globals.CURRENT_BRANCH;

    @Transient
    private String captcha;

    public Submission() {
    }

    public Submission(String title, String description, SessionLevel level, SessionType type, Speaker speaker) {
        this.title = title;
        this.description = description;
        this.level = level;
        this.speaker = speaker;
        this.type = type;
    }

    public Submission(String title, String description, SessionLevel level, SessionType type, Speaker speaker, Speaker coSpeaker) {
        this.title = title;
        this.description = description;
        this.level = level;
        this.type = type;
        this.speaker = speaker;
        this.coSpeaker = coSpeaker;
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

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
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
        if (!(o instanceof Submission)) {
            return false;
        }

        Submission that = (Submission) o;

        if (speaker != null && !speaker.equals(that.speaker)) {
            return false;
        }
        return !(title != null && !title.equals(that.title));
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + speaker.hashCode();
        return result;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
