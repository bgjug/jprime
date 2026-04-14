package site.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import site.app.Application;
import site.facade.BranchService;
import site.facade.DefaultBranchUtil;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for PrivacyPolicyController.
 * Tests privacy policy page display in different languages.
 */
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
class PrivacyPolicyControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeAll
    public static void beforeAll(@Autowired BranchService branchService) {
        DefaultBranchUtil.createDefaultBranch(branchService);
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void getPrivacyPolicy_withoutParams_shouldReturnEnglishVersion() throws Exception {
        mockMvc.perform(get("/privacy-policy"))
            .andExpect(status().isOk())
            .andExpect(view().name("privacy-policy"));
    }

    @Test
    void getPrivacyPolicy_withBgParam_shouldReturnBulgarianVersion() throws Exception {
        mockMvc.perform(get("/privacy-policy").param("bg", ""))
            .andExpect(status().isOk())
            .andExpect(view().name("privacy-policy-bg"));
    }

    @Test
    void getPrivacyPolicy_withEnParam_shouldReturnEnglishVersion() throws Exception {
        mockMvc.perform(get("/privacy-policy").param("en", ""))
            .andExpect(status().isOk())
            .andExpect(view().name("privacy-policy"));
    }

    @Test
    void getPrivacyPolicy_withBothParams_shouldReturnBulgarianVersion() throws Exception {
        // When both params are present, bg takes precedence
        mockMvc.perform(get("/privacy-policy").param("bg", "").param("en", ""))
            .andExpect(status().isOk())
            .andExpect(view().name("privacy-policy-bg"));
    }
}
