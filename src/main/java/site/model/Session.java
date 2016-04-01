package site.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import java.util.Collection;
import java.util.HashSet;

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
}
