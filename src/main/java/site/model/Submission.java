package site.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import site.config.Globals;

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

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Speaker.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "speaker", nullable = false, referencedColumnName = "id")
    private Speaker speaker = new Speaker();

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Speaker.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "coSpeaker", nullable = true, referencedColumnName = "id")
    private Speaker coSpeaker = null;

    @Enumerated(EnumType.STRING)
    private Branch branch = Globals.CURRENT_BRANCH;

    public Submission() {
    }

    public Submission(String title, String description, SessionLevel level, Speaker speaker) {
        this.title = title;
        this.description = description;
        this.level = level;
        this.speaker = speaker;
    }

    public Submission(String title, String description, SessionLevel level, Speaker speaker,
            Speaker coSpeaker) {
        this.title = title;
        this.description = description;
        this.level = level;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Submission))
            return false;

        Submission that = (Submission) o;

        if (speaker != null && !speaker.equals(that.speaker))
            return false;
        if (title != null && !title.equals(that.title))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + speaker.hashCode();
        return result;
    }
}
