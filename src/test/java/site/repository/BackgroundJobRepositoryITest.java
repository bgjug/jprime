package site.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import site.app.Application;
import site.facade.BranchService;
import site.facade.DefaultBranchUtil;
import site.model.BackgroundJob;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for BackgroundJobRepository custom queries.
 * Tests pending and completed job queries.
 */
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Transactional
class BackgroundJobRepositoryITest {

    @Autowired
    private BackgroundJobRepository backgroundJobRepository;

    private BackgroundJob pendingJob1;
    private BackgroundJob pendingJob2;
    private BackgroundJob recentlyCompletedJob;
    private BackgroundJob oldCompletedJob;

    @BeforeAll
    public static void beforeAll(@Autowired BranchService branchService) {
        DefaultBranchUtil.createDefaultBranch(branchService);
    }

    @BeforeEach
    void setUp() {
        backgroundJobRepository.deleteAll();

        // Pending job 1 (no completed timestamp)
        pendingJob1 = new BackgroundJob("job-pending-001");
        pendingJob1.setDescription("Pending Job 1");
        pendingJob1.setStatus("Running");
        pendingJob1 = backgroundJobRepository.save(pendingJob1);

        // Pending job 2 (no completed timestamp)
        pendingJob2 = new BackgroundJob("job-pending-002");
        pendingJob2.setDescription("Pending Job 2");
        pendingJob2.setStatus("Queued");
        pendingJob2 = backgroundJobRepository.save(pendingJob2);

        // Recently completed job
        recentlyCompletedJob = new BackgroundJob("job-completed-001");
        recentlyCompletedJob.setDescription("Recently Completed Job");
        recentlyCompletedJob.setStatus("Success");
        recentlyCompletedJob.setCompleted(LocalDateTime.now().minusHours(1));
        recentlyCompletedJob = backgroundJobRepository.save(recentlyCompletedJob);

        // Old completed job
        oldCompletedJob = new BackgroundJob("job-completed-002");
        oldCompletedJob.setDescription("Old Completed Job");
        oldCompletedJob.setStatus("Success");
        oldCompletedJob.setCompleted(LocalDateTime.now().minusDays(7));
        oldCompletedJob = backgroundJobRepository.save(oldCompletedJob);
    }

    @Test
    void findPendingJobs_shouldReturnOnlyJobsWithoutCompletedTimestamp() {
        List<BackgroundJob> pending = backgroundJobRepository.findPendingJobs();

        assertThat(pending).hasSize(2);
        assertThat(pending).containsExactlyInAnyOrder(pendingJob1, pendingJob2);
        assertThat(pending).doesNotContain(recentlyCompletedJob, oldCompletedJob);
    }

    @Test
    void findCompletedJobs_shouldReturnJobsCompletedAfterGivenTimestamp() {
        LocalDateTime twoDaysAgo = LocalDateTime.now().minusDays(2);
        List<BackgroundJob> recentJobs = backgroundJobRepository.findCompletedJobs(twoDaysAgo);

        assertThat(recentJobs).hasSize(1);
        assertThat(recentJobs).containsExactly(recentlyCompletedJob);
        assertThat(recentJobs).doesNotContain(oldCompletedJob, pendingJob1, pendingJob2);
    }

    @Test
    void findCompletedJobs_shouldReturnAllCompletedJobsWhenTimestampIsOld() {
        LocalDateTime tenDaysAgo = LocalDateTime.now().minusDays(10);
        List<BackgroundJob> allCompleted = backgroundJobRepository.findCompletedJobs(tenDaysAgo);

        assertThat(allCompleted).hasSize(2);
        assertThat(allCompleted).containsExactlyInAnyOrder(recentlyCompletedJob, oldCompletedJob);
        assertThat(allCompleted).doesNotContain(pendingJob1, pendingJob2);
    }

    @Test
    void findCompletedJobs_shouldReturnEmptyWhenNoJobsAfterTimestamp() {
        LocalDateTime future = LocalDateTime.now().plusDays(1);
        List<BackgroundJob> futureJobs = backgroundJobRepository.findCompletedJobs(future);

        assertThat(futureJobs).isEmpty();
    }

    @Test
    void findPendingJobs_shouldReturnEmptyWhenAllJobsCompleted() {
        // Complete all pending jobs
        pendingJob1.setCompleted(LocalDateTime.now());
        backgroundJobRepository.save(pendingJob1);
        pendingJob2.setCompleted(LocalDateTime.now());
        backgroundJobRepository.save(pendingJob2);

        List<BackgroundJob> pending = backgroundJobRepository.findPendingJobs();

        assertThat(pending).isEmpty();
    }

    @Test
    void saveAndRetrieve_shouldPersistJobCorrectly() {
        BackgroundJob newJob = new BackgroundJob("job-new-001");
        newJob.setDescription("New Test Job");
        newJob.setStatus("Initiated");
        newJob.appendToJobLog("Starting job");

        BackgroundJob saved = backgroundJobRepository.save(newJob);

        assertThat(saved.getJobId()).isEqualTo("job-new-001");
        assertThat(saved.getDescription()).isEqualTo("New Test Job");
        assertThat(saved.getStatus()).isEqualTo("Initiated");
        assertThat(saved.getLog()).contains("Starting job");
        assertThat(saved.getCompleted()).isNull();

        // Verify it's in pending jobs
        List<BackgroundJob> pending = backgroundJobRepository.findPendingJobs();
        assertThat(pending).contains(saved);
    }
}
