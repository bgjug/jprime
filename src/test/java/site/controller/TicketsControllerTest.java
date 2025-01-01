package site.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import site.app.Application;
import site.facade.BranchService;
import site.facade.DefaultBranchUtil;
import site.model.Branch;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * @author Ivan St. Ivanov
 */
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@AutoConfigureMockMvc
class TicketsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BranchService branchService;

    @BeforeAll
    public static void beforeAll(@Autowired BranchService branchService) {
        DefaultBranchUtil.createDefaultBranch(branchService);
    }

    @Test
    void getShouldReturnTicketsJsp() throws Exception {
        Branch currentBranch = branchService.getCurrentBranch();
        Assertions.assertThat(currentBranch).isNotNull();

        mockMvc.perform(get("/tickets"))
                .andExpect(status().isOk())
                .andExpect(view().name(
                    currentBranch.isSoldOut() ? TicketsController.TICKETS_END_JSP : TicketsController.TICKETS_REGISTER_JSP));
    }

}
