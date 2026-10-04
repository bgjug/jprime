package site.facade;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import site.model.Branch;
import site.model.Speaker;
import site.model.Submission;
import site.model.SubmissionStatus;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SubmissionPolicyTest {

    private SubmissionPolicy policy;
    private Speaker owner;
    private Speaker co;
    private Speaker stranger;
    private Submission sub;
    private Branch current;
    private Branch other;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        policy = new SubmissionPolicy();

        // Create speakers
        owner = new Speaker();
        owner.setId(1L);

        co = new Speaker();
        co.setId(2L);

        stranger = new Speaker();
        stranger.setId(3L);

        // Create branches
        now = LocalDateTime.now();
        current = new Branch(2027);
        current.setCfpOpenDate(now.minusDays(1));
        current.setCfpCloseDate(now.plusDays(1));

        other = new Branch(2026);

        // Create submission
        sub = new Submission();
        sub.setSpeaker(owner);
        sub.setCoSpeaker(co);
        sub.setBranch(current);
        sub.setStatus(SubmissionStatus.SUBMITTED);
    }

    @Test
    void ownerAndCoSpeakerCanViewStrangerCannot() {
        assertTrue(policy.canView(owner, sub));
        assertTrue(policy.canView(co, sub));
        assertFalse(policy.canView(stranger, sub));
    }

    @Test
    void canViewWithoutCoSpeaker() {
        sub.setCoSpeaker(null);
        assertFalse(policy.canView(co, sub));
    }

    @Test
    void editableWhenSubmittedCurrentBranchCfpOpen() {
        assertTrue(policy.canEdit(owner, sub, current, now));
        assertTrue(policy.canEdit(co, sub, current, now));
        assertFalse(policy.canEdit(stranger, sub, current, now));
    }

    @Test
    void notEditableWhenAccepted() {
        sub.setStatus(SubmissionStatus.ACCEPTED);
        assertFalse(policy.canEdit(owner, sub, current, now));
    }

    @Test
    void notEditableAfterCfpClose() {
        assertFalse(policy.canEdit(owner, sub, current, now.plusDays(2)));
    }

    @Test
    void notEditableForOtherBranch() {
        sub.setBranch(other);
        assertFalse(policy.canEdit(owner, sub, current, now));
    }
}
