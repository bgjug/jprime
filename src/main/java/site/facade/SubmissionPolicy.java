package site.facade;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.stereotype.Component;

import site.model.Branch;
import site.model.Speaker;
import site.model.Submission;
import site.model.SubmissionStatus;

@Component
public class SubmissionPolicy {

    /**
     * Determine if a speaker can view a submission.
     * A speaker can view if they are the owner or the co-speaker.
     *
     * @param speaker the speaker attempting to view
     * @param submission the submission being viewed
     * @return true if the speaker can view the submission
     */
    public boolean canView(Speaker speaker, Submission submission) {
        if (speaker == null || submission == null) {
            return false;
        }

        // Check if speaker is the owner
        if (Objects.equals(speaker.getId(), submission.getSpeaker().getId())) {
            return true;
        }

        // Check if speaker is the co-speaker
        if (submission.getCoSpeaker() != null
                && Objects.equals(speaker.getId(), submission.getCoSpeaker().getId())) {
            return true;
        }

        return false;
    }

    /**
     * Determine if a speaker can edit a submission.
     * A speaker can edit if:
     * - They can view the submission
     * - The submission status is SUBMITTED
     * - The submission branch matches the current branch
     * - The current time is within the CFP window (after open, before close)
     *
     * @param speaker the speaker attempting to edit
     * @param submission the submission being edited
     * @param current the current branch
     * @param now the current time
     * @return true if the speaker can edit the submission
     */
    public boolean canEdit(Speaker speaker, Submission submission, Branch current, LocalDateTime now) {
        if (!canView(speaker, submission)) {
            return false;
        }

        // Check submission status is SUBMITTED
        if (submission.getStatus() != SubmissionStatus.SUBMITTED) {
            return false;
        }

        // Check submission branch matches current branch
        if (submission.getBranch() == null || current == null) {
            return false;
        }
        if (!Objects.equals(submission.getBranch().getLabel(), current.getLabel())) {
            return false;
        }

        // Check CFP window: strict comparison (after open AND before close)
        LocalDateTime cfpOpenDate = current.getCfpOpenDate();
        LocalDateTime cfpCloseDate = current.getCfpCloseDate();

        if (cfpOpenDate == null || cfpCloseDate == null) {
            return false;
        }

        if (!now.isAfter(cfpOpenDate) || !now.isBefore(cfpCloseDate)) {
            return false;
        }

        return true;
    }
}
