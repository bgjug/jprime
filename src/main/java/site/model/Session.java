package site.model;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Session extends AbstractEntity {
	/**
     * Default serial version uid.
     */
    private static final long serialVersionUID = 1L;
	
    @OneToOne(fetch = FetchType.EAGER, targetEntity = Submission.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "submission", nullable = false, referencedColumnName = "id")
	private Submission submission;
    
    @Column(name = "start_time")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime startTime;
    
    @Column(name = "end_time")
   	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime endTime;
    
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = VenueHall.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "hall", nullable = false, referencedColumnName = "id")
	private VenueHall hall;

	public Session() {
	}

	public Session(Submission submission, DateTime startTime, DateTime endTime, VenueHall hall) {
		this.submission = submission;
		this.startTime = startTime;
		this.endTime = endTime;
		this.hall = hall;
	}

	public Submission getSubmission() {
		return submission;
	}

	public void setSubmission(Submission submission) {
		this.submission = submission;
	}

	public DateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(DateTime startTime) {
		this.startTime = startTime;
	}

	public DateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(DateTime endTime) {
		this.endTime = endTime;
	}

	public VenueHall getHall() {
		return hall;
	}

	public void setHall(VenueHall hall) {
		this.hall = hall;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return Objects.equals(submission, session.submission) &&
                Objects.equals(startTime, session.startTime) &&
                Objects.equals(endTime, session.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(submission, startTime, endTime);
    }
}
