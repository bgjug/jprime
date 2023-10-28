package site.model;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Session extends AbstractEntity {

	private String title;

	@Column(name = "start_time")
	private LocalDateTime startTime;

	@Column(name = "end_time")
	private LocalDateTime endTime;

	public void setTitle(String title) {
		this.title = title;
	}

	@OneToOne(fetch = FetchType.EAGER, targetEntity = Submission.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "submission", referencedColumnName = "id")
	private Submission submission;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = VenueHall.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "hall", referencedColumnName = "id")
	private VenueHall hall;

	public Session() {
	}

	public Session(Submission submission, LocalDateTime startTime, LocalDateTime endTime, VenueHall hall) {
		this.submission = submission;
		setStartTime(startTime);
		setEndTime(endTime);
		this.hall = hall;
	}

	public Submission getSubmission() {
		return submission;
	}

	public void setSubmission(Submission submission) {
		this.submission = submission;
	}

	public VenueHall getHall() {
		return hall;
	}

	public void setHall(VenueHall hall) {
		this.hall = hall;
	}

    public String getTitle() {
        if (submission != null) {
            return submission.getTitle();
        }

        return title;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Session session = (Session) o;
        return Objects.equals(getTitle(), session.getTitle()) &&
                Objects.equals(getStartTime(), session.getStartTime()) &&
                Objects.equals(getEndTime(), session.getEndTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getStartTime(), getEndTime());
    }
}
