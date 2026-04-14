package site.facade;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import site.app.Application;
import site.model.BackgroundJob;
import site.repository.BackgroundJobRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(classes = Application.class)
@WebAppConfiguration
class BackgroundJobServiceUTest {

    @Autowired
    private BackgroundJobService backgroundJobService;

    @Autowired
    private BackgroundJobRepository backgroundJobRepository;

    @AfterEach
    void cleanup() {
        backgroundJobRepository.deleteAll();
    }

    @Test
    void createBackgroundJob_createsJob_withInitiatedStatus() {
        String jobId = backgroundJobService.createBackgroundJob("Test job");

        assertThat(jobId).isNotNull();
        BackgroundJob job = backgroundJobRepository.findById(jobId).orElse(null);
        assertThat(job).isNotNull();
        assertThat(job.getDescription()).isEqualTo("Test job");
        assertThat(job.getStatus()).isEqualTo("Initiated");
        assertThat(job.getCompleted()).isNull();
    }

    @Test
    void runJob_completesJob_afterAllItemsProcessed() {
        String jobId = backgroundJobService.createBackgroundJob("Run test");
        backgroundJobService.runJob(jobId, List.of("a", "b", "c"), item -> true, item -> item);

        await().atMost(5, TimeUnit.SECONDS)
            .until(() -> backgroundJobRepository.findById(jobId).map(j -> j.getCompleted() != null)
                .orElse(false));

        BackgroundJob job = backgroundJobRepository.findById(jobId).orElse(null);
        assertThat(job).isNotNull();
        assertThat(job.getCompleted()).isNotNull();
        assertThat(job.getStatus()).contains("3 from 3");
    }

    @Test
    void runJob_tracksFailedItems_inJobStatus() {
        String jobId = backgroundJobService.createBackgroundJob("Failing job");
        backgroundJobService.runJob(jobId, List.of("x", "y"), item -> false, item -> item);

        await().atMost(5, TimeUnit.SECONDS)
            .until(() -> backgroundJobRepository.findById(jobId).map(j -> j.getCompleted() != null)
                .orElse(false));

        BackgroundJob job = backgroundJobRepository.findById(jobId).orElse(null);
        assertThat(job).isNotNull();
        assertThat(job.getStatus()).contains("failed");
        assertThat(job.getCompleted()).isNotNull();
    }
}
