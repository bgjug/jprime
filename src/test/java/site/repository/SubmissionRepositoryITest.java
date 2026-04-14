package site.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import site.app.Application;
import site.facade.BranchService;
import site.facade.DefaultBranchUtil;
import site.model.Branch;
import site.model.SessionLevel;
import site.model.SessionType;
import site.model.Speaker;
import site.model.Submission;
import site.model.SubmissionByStatus;
import site.model.SubmissionStatus;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for SubmissionRepository custom queries.
 * Tests branch and status filtering functionality.
 */
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Transactional
class SubmissionRepositoryITest {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private SpeakerRepository speakerRepository;

    @Autowired
    private BranchService branchService;

    private Branch currentBranch;
    private Branch otherBranch;
    private Submission confirmedSubmission;
    private Submission submittedSubmission;
    private Submission rejectedSubmission;
    private Submission otherBranchSubmission;
    private Speaker speaker;

    @BeforeAll
    public static void beforeAll(@Autowired BranchService branchService) {
        DefaultBranchUtil.createDefaultBranch(branchService);
    }

    @BeforeEach
    void setUp() {
        submissionRepository.deleteAll();
        speakerRepository.deleteAll();

        currentBranch = branchService.getCurrentBranch();
        try {
            otherBranch = branchService.findBranchByYear(2016);
        } catch (IllegalArgumentException e) {
            otherBranch = branchService.createBranch(2016, LocalDateTime.of(2016, 5, 24, 9, 0));
        }

        speaker = new Speaker("Test", "Speaker", "speaker@example.com", "Headline", "@testspeaker");
        speaker = speakerRepository.save(speaker);

        // Confirmed submission in current branch
        confirmedSubmission = new Submission(
            "Confirmed Talk", "Description", SessionLevel.ADVANCED,
            SessionType.CONFERENCE_SESSION, speaker, SubmissionStatus.CONFIRMED, false
        );
        confirmedSubmission.branch(currentBranch);
        confirmedSubmission = submissionRepository.save(confirmedSubmission);

        // Submitted submission in current branch
        submittedSubmission = new Submission(
            "Submitted Talk", "Description", SessionLevel.INTERMEDIATE,
            SessionType.CONFERENCE_SESSION, speaker, SubmissionStatus.SUBMITTED, false
        );
        submittedSubmission.branch(currentBranch);
        submittedSubmission = submissionRepository.save(submittedSubmission);

        // Rejected submission in current branch
        rejectedSubmission = new Submission(
            "Rejected Talk", "Description", SessionLevel.BEGINNER,
            SessionType.CONFERENCE_SESSION, speaker, SubmissionStatus.REJECTED, false
        );
        rejectedSubmission.branch(currentBranch);
        rejectedSubmission = submissionRepository.save(rejectedSubmission);

        // Confirmed submission in other branch
        otherBranchSubmission = new Submission(
            "Other Branch Talk", "Description", SessionLevel.ADVANCED,
            SessionType.CONFERENCE_SESSION, speaker, SubmissionStatus.CONFIRMED, false
        );
        otherBranchSubmission.branch(otherBranch);
        otherBranchSubmission = submissionRepository.save(otherBranchSubmission);
    }

    @Test
    void findByStatus_shouldReturnSubmissionsWithGivenStatus() {
        List<Submission> confirmed = submissionRepository.findByStatus(SubmissionStatus.CONFIRMED);

        assertThat(confirmed).hasSizeGreaterThanOrEqualTo(2);
        assertThat(confirmed).contains(confirmedSubmission, otherBranchSubmission);
    }

    @Test
    void findByBranchAndStatus_shouldReturnSubmissionsForBranchAndStatus() {
        List<Submission> currentBranchConfirmed = 
            submissionRepository.findByBranchAndStatus(currentBranch, SubmissionStatus.CONFIRMED);

        assertThat(currentBranchConfirmed).hasSize(1);
        assertThat(currentBranchConfirmed).containsExactly(confirmedSubmission);
    }

    @Test
    void findByBranchAndStatusPageable_shouldReturnPagedSubmissions() {
        Page<Submission> submissions = submissionRepository.findByBranchAndStatus(
            currentBranch, SubmissionStatus.SUBMITTED, PageRequest.of(0, 10)
        );

        assertThat(submissions.getContent()).hasSize(1);
        assertThat(submissions.getContent()).containsExactly(submittedSubmission);
    }

    @Test
    void findAllByBranch_shouldReturnAllSubmissionsForBranch() {
        Page<Submission> currentBranchSubmissions = 
            submissionRepository.findAllByBranch(currentBranch, PageRequest.of(0, 10));

        assertThat(currentBranchSubmissions.getContent()).hasSize(3);
        assertThat(currentBranchSubmissions.getContent())
            .containsExactlyInAnyOrder(confirmedSubmission, submittedSubmission, rejectedSubmission);
    }

    @Test
    void countSubmissionsByStatusForBranch_shouldReturnCountsGroupedByStatus() {
        List<SubmissionByStatus> counts = submissionRepository.countSubmissionsByStatusForBranch(currentBranch);

        assertThat(counts).hasSize(3);
        
        // Find the count for CONFIRMED status
        SubmissionByStatus confirmedCount = counts.stream()
            .filter(sbs -> sbs.getStatus() == SubmissionStatus.CONFIRMED)
            .findFirst()
            .orElse(null);
        assertThat(confirmedCount).isNotNull();
        assertThat(confirmedCount.getCount()).isEqualTo(1L);

        // Find the count for SUBMITTED status
        SubmissionByStatus submittedCount = counts.stream()
            .filter(sbs -> sbs.getStatus() == SubmissionStatus.SUBMITTED)
            .findFirst()
            .orElse(null);
        assertThat(submittedCount).isNotNull();
        assertThat(submittedCount.getCount()).isEqualTo(1L);

        // Find the count for REJECTED status
        SubmissionByStatus rejectedCount = counts.stream()
            .filter(sbs -> sbs.getStatus() == SubmissionStatus.REJECTED)
            .findFirst()
            .orElse(null);
        assertThat(rejectedCount).isNotNull();
        assertThat(rejectedCount.getCount()).isEqualTo(1L);
    }
}
