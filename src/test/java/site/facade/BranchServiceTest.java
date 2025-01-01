package site.facade;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import site.app.Application;
import site.model.Branch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = Application.class)
@WebAppConfiguration
class BranchServiceTest {

    @Autowired
    private BranchService branchService;

    @BeforeAll
    public static void beforeAll(@Autowired BranchService branchService) {
        DefaultBranchUtil.createDefaultBranch(branchService);
    }

    @Test
    void testAllBranches() {
        List<Branch> branches = branchService.allBranches();
        assertNotNull(branches);
        assertFalse(branches.isEmpty());
    }

    @Test
    void testCurrentBranch() {
        Branch currentBranch = branchService.getCurrentBranch();
        assertNotNull(currentBranch);

        Branch branch = branchService.findBranchByYear(2024);
        branchService.setAsCurrent(branch.getLabel());

        currentBranch = branchService.getCurrentBranch();
        assertNotNull(currentBranch);
        assertEquals(branch.getLabel(), currentBranch.getLabel());
    }

}