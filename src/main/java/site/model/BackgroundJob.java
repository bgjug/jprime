package site.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class BackgroundJob {

    @Id
    @Column(length = 40)
    private String jobId;

    @Column(length = 128)
    @NotNull
    private String description;

    @Column(length = 128)
    private String status;

    @Column(columnDefinition = "LONGTEXT")
    private String log;

    private LocalDateTime completed;

    public BackgroundJob(String jobId) {
        this.jobId = jobId;
    }

    public BackgroundJob() {
    }

    public LocalDateTime getCompleted() {
        return completed;
    }

    public void setCompleted(LocalDateTime completed) {
        this.completed = completed;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String jobLog) {
        this.log = jobLog;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String jobDescription) {
        this.description = jobDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String jobStatus) {
        this.status = jobStatus;
    }

    public void appendToJobLog(String logMessage) {
        if (log != null) {
            log += System.lineSeparator() + logMessage;
        } else {
            log = logMessage;
        }
    }
}
