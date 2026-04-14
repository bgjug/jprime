package site.controller;

import java.time.LocalDateTime;
import java.util.List;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for AdminBranchController.
 * Tests branch CRUD operations in admin panel.
 */
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Transactional
class AdminBranchControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private BranchService branchService;

    private MockMvc mockMvc;
    private Branch testBranch;

    @BeforeAll
    public static void beforeAll(@Autowired BranchService branchService) {
        DefaultBranchUtil.createDefaultBranch(branchService);
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        testBranch = branchService.createBranch(2025, LocalDateTime.of(2025, 5, 27, 9, 0));
    }

    @Test
    void getView_shouldReturnBranchViewWithAllBranches() throws Exception {
        mockMvc.perform(get("/admin/branch/view"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/branch/view"))
            .andExpect(model().attributeExists("branches"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attributeExists("number"));
    }

    @Test
    void getAdd_shouldReturnFormWithEmptyBranch() throws Exception {
        mockMvc.perform(get("/admin/branch/add"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/branch/edit"))
            .andExpect(model().attributeExists("branchForm"));
    }

    @Test
    void getEdit_shouldReturnFormWithExistingBranch() throws Exception {
        mockMvc.perform(get("/admin/branch/edit/" + testBranch.getLabel()))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/branch/edit"))
            .andExpect(model().attributeExists("branchForm"));
    }

    @Test
    void postAdd_shouldCreateNewBranch() throws Exception {
        mockMvc.perform(post("/admin/branch/add")
                .param("year", "2026")
                .param("startDate", "2026-05-26T09:00")
                .param("cfpOpenDate", "2025-12-01T00:00")
                .param("cfpCloseDate", "2026-03-01T23:59"))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/branch/view"));

        Branch saved = branchService.findBranchByYear(2026);
        assertThat(saved).isNotNull();
        assertThat(saved.getYear()).isEqualTo(2026);
    }

    @Test
    void getRemove_shouldDeleteBranch() throws Exception {
        String branchLabel = testBranch.getLabel();

        mockMvc.perform(get("/admin/branch/remove/" + branchLabel))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/branch/view"));

        Branch deleted = branchService.findById(branchLabel);
        assertThat(deleted).isNull();
    }
}
