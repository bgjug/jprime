package site.controller;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import site.app.Application;
import site.facade.BranchService;
import site.facade.DefaultBranchUtil;
import site.model.BackgroundJob;
import site.repository.BackgroundJobRepository;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for AdminControllerJobs.
 * Tests background jobs viewing functionality.
 */
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Transactional
class AdminControllerJobsTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private BackgroundJobRepository backgroundJobRepository;

    private MockMvc mockMvc;

    @BeforeAll
    public static void beforeAll(@Autowired BranchService branchService) {
        DefaultBranchUtil.createDefaultBranch(branchService);
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        backgroundJobRepository.deleteAll();

        // Create test jobs
        BackgroundJob job1 = new BackgroundJob("job-001");
        job1.setDescription("Test Job 1");
        job1.setStatus("Running");
        backgroundJobRepository.save(job1);

        BackgroundJob job2 = new BackgroundJob("job-002");
        job2.setDescription("Test Job 2");
        job2.setStatus("Completed");
        job2.setCompleted(LocalDateTime.now());
        backgroundJobRepository.save(job2);
    }

    @Test
    void getView_shouldReturnJobsViewWithAllJobs() throws Exception {
        mockMvc.perform(get("/admin/jobs/view"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/jobs/view"))
            .andExpect(model().attributeExists("jobs"))
            .andExpect(model().attribute("jobs", hasSize(greaterThanOrEqualTo(2))));
    }
}
