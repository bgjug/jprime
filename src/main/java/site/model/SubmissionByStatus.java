package site.model;

public class SubmissionByStatus {
    private final SubmissionStatus status;

    private final long count;

    public SubmissionByStatus(SubmissionStatus status, long count) {
        this.status = status;
        this.count = count;
    }

    public SubmissionStatus getStatus() {
        return status;
    }

    public long getCount() {
        return count;
    }
}
