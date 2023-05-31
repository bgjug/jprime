package site.facade;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import site.model.BackgroundJob;
import site.repository.BackgroundJobRepository;

@Service
public class BackgroundJobService {

    Logger logger = LogManager.getLogger(BackgroundJobService.class);

    private final BackgroundJobRepository jobRepository;

    public BackgroundJobService(BackgroundJobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public String createBackgroundJob(String description) {
        BackgroundJob newJob = new BackgroundJob(UUID.randomUUID().toString());
        newJob.setDescription(description);
        newJob.setStatus("Initiated");
        jobRepository.save(newJob);

        return newJob.getJobId();
    }

    @Async
    public <T> void runJob(String jobId, List<T> items, Function<T, Boolean> jobExecutor,
        Function<T, String> logFunction) {
        if (jobExecutor == null || CollectionUtils.isEmpty(items) || StringUtils.isEmpty(jobId)) {
            logger.error("[initiateJob] Invalid parameter value!!!");
            return;
        }

        BackgroundJob job = jobRepository.findById(jobId)
            .orElseThrow(() -> new RuntimeException("Invalid or missing job Id"));

        int complete = 0;
        int failed = 0;
        for (T item : items) {
            boolean success = jobExecutor.apply(item);
            String logMessage;
            if (!success) {
                failed++;
                logMessage = logFunction != null ?
                    "Job execution failed for %s!!!".formatted(logFunction.apply(item)) :
                    "Job execution failed for item %d".formatted(complete);
            } else {
                logMessage = null;
            }

            complete++;
            String status = "%d from %d has been completed.".formatted(complete, items.size());
            if (failed > 0) {
                status += " There are also %d failed jobs!!!".formatted(failed);
            }
            job.setStatus(status);
            if (StringUtils.isNotEmpty(logMessage)) {
                job.appendToJobLog(logMessage);
            }

            jobRepository.save(job);
        }

        job.setCompleted(LocalDateTime.now());
        jobRepository.save(job);
    }
}
