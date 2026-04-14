package site.controller;

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
import site.model.Branch;
import site.model.Registrant;
import site.model.Visitor;
import site.repository.RegistrantRepository;
import site.repository.VisitorRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for AdminVisitorController.
 * Tests visitor viewing and management operations.
 */
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Transactional
class AdminVisitorControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private VisitorRepository visitorRepository;

    @Autowired
    private RegistrantRepository registrantRepository;

    @Autowired
    private BranchService branchService;

    private MockMvc mockMvc;
    private Visitor testVisitor;
    private Registrant testRegistrant;
    private Branch currentBranch;

    @BeforeAll
    public static void beforeAll(@Autowired BranchService branchService) {
        DefaultBranchUtil.createDefaultBranch(branchService);
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        visitorRepository.deleteAll();
        registrantRepository.deleteAll();

        currentBranch = branchService.getCurrentBranch();

        testRegistrant = new Registrant("Test Company", "company@example.com", currentBranch);
        testRegistrant = registrantRepository.save(testRegistrant);

        testVisitor = new Visitor(testRegistrant, "John Doe", "john@example.com", "Tech Corp");
        testVisitor = visitorRepository.save(testVisitor);
    }

    @Test
    void getView_shouldReturnVisitorViewWithAllVisitors() throws Exception {
        mockMvc.perform(get("/admin/visitor/view"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/visitor/view"))
            .andExpect(model().attributeExists("visitors"))
            .andExpect(model().attributeExists("payedCount"))
            .andExpect(model().attributeExists("requestingCount"))
            .andExpect(model().attributeExists("sponsoredCount"));
    }

    @Test
    void getAdd_shouldReturnFormWithEmptyVisitor() throws Exception {
        mockMvc.perform(get("/admin/visitor/add"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/visitor/edit"))
            .andExpect(model().attributeExists("visitor"))
            .andExpect(model().attributeExists("statuses"));
    }

    @Test
    void getUpload_shouldReturnUploadForm() throws Exception {
        mockMvc.perform(get("/admin/visitor/upload"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/visitor/upload"))
            .andExpect(model().attributeExists("fileModel"))
            .andExpect(model().attributeExists("visitorTypes"))
            .andExpect(model().attributeExists("visitorStatuses"));
    }

    @Test
    void getExport_shouldReturnCsvFile() throws Exception {
        mockMvc.perform(get("/admin/visitor/export"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/octet-stream"));
    }
}
