package site.model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

import site.controller.DateUtils;

@Entity
public class Session extends AbstractEntity {

    private String title;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @OneToOne(fetch = FetchType.EAGER, targetEntity = Submission.class,
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "submission", referencedColumnName = "id", foreignKey = @ForeignKey(name="fk_session_submission"))
    private Submission submission;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = VenueHall.class,
        cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "hall", referencedColumnName = "id", foreignKey = @ForeignKey(name="fk_session_hall"))
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

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Date getStartDateTime() {
        return DateUtils.fromLocalDateTime(startTime);
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public Date getEndDateTime() {
        return DateUtils.fromLocalDateTime(endTime);
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }

        if (!(o instanceof Session session)) {
            return false;
        }

        return Objects.equals(getTitle(), session.getTitle()) && Objects.equals(getStartTime(),
            session.getStartTime()) && Objects.equals(getEndTime(),
            session.getEndTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getStartTime(), getEndTime());
    }

    @Override
    public String toString() {
        return "Session{" + "title='" + title + '\'' + ", startTime=" + getStartTime() + ", " + "endTime=" + getEndTime() + '}';
    }
}
