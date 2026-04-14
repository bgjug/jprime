package site.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import site.app.Application;
import site.facade.BranchService;
import site.facade.DefaultBranchUtil;
import site.model.Sponsor;
import site.model.SponsorPackage;
import site.repository.SponsorRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for AdminSponsorController.
 * Tests sponsor CRUD operations in admin panel.
 */
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Transactional
class AdminSponsorControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private SponsorRepository sponsorRepository;

    private MockMvc mockMvc;
    private Sponsor testSponsor;

    @BeforeAll
    public static void beforeAll(@Autowired BranchService branchService) {
        DefaultBranchUtil.createDefaultBranch(branchService);
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        sponsorRepository.deleteAll();

        testSponsor = new Sponsor();
        testSponsor.setCompanyName("Test Sponsor");
        testSponsor.setEmail("test@sponsor.com");
        testSponsor.setSponsorPackage(SponsorPackage.GOLD);
        testSponsor = sponsorRepository.save(testSponsor);
    }

    @Test
    void getView_shouldReturnSponsorViewWithAllSponsors() throws Exception {
        mockMvc.perform(get("/admin/sponsor/view"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/sponsor/view"))
            .andExpect(model().attributeExists("sponsors"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attributeExists("number"));
    }

    @Test
    void getAdd_shouldReturnFormWithEmptySponsor() throws Exception {
        mockMvc.perform(get("/admin/sponsor/add"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/sponsor/edit"))
            .andExpect(model().attributeExists("sponsor"));
    }

    @Test
    void getEdit_shouldReturnFormWithExistingSponsor() throws Exception {
        mockMvc.perform(get("/admin/sponsor/edit/" + testSponsor.getId()))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/sponsor/edit"))
            .andExpect(model().attribute("sponsor", testSponsor));
    }

    @Test
    void postAdd_shouldCreateNewSponsor() throws Exception {
        MockMultipartFile emptyFile = new MockMultipartFile("file", new byte[0]);

        mockMvc.perform(multipart("/admin/sponsor/add")
                .file(emptyFile)
                .param("companyName", "New Sponsor")
                .param("email", "sponsor@example.com")
                .param("sponsorPackage", "PLATINUM"))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/sponsor/view"));

        assertThat(sponsorRepository.findAll()).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void getRemove_shouldDeleteSponsor() throws Exception {
        long sponsorId = testSponsor.getId();

        mockMvc.perform(get("/admin/sponsor/remove/" + sponsorId))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/sponsor/view"));

        assertThat(sponsorRepository.findById(sponsorId)).isEmpty();
    }
}
