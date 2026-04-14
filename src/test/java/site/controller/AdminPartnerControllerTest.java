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
import site.model.Partner;
import site.model.PartnerPackage;
import site.repository.PartnerRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for AdminPartnerController.
 * Tests partner CRUD operations in admin panel.
 */
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Transactional
class AdminPartnerControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private PartnerRepository partnerRepository;

    private MockMvc mockMvc;
    private Partner testPartner;

    @BeforeAll
    public static void beforeAll(@Autowired BranchService branchService) {
        DefaultBranchUtil.createDefaultBranch(branchService);
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        partnerRepository.deleteAll();

        testPartner = new Partner();
        testPartner.setCompanyName("Test Partner");
        testPartner.setPartnerPackage(PartnerPackage.MEDIA);
        testPartner = partnerRepository.save(testPartner);
    }

    @Test
    void getView_shouldReturnPartnerViewWithAllPartners() throws Exception {
        mockMvc.perform(get("/admin/partner/view"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/partner/view"))
            .andExpect(model().attributeExists("partners"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attributeExists("number"));
    }

    @Test
    void getAdd_shouldReturnFormWithEmptyPartner() throws Exception {
        mockMvc.perform(get("/admin/partner/add"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/partner/edit"))
            .andExpect(model().attribute("partner", is(new Partner())));
    }

    @Test
    void getEdit_shouldReturnFormWithExistingPartner() throws Exception {
        mockMvc.perform(get("/admin/partner/edit/" + testPartner.getId()))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/partner/edit"))
            .andExpect(model().attribute("partner", testPartner));
    }

    @Test
    void postAdd_shouldCreateNewPartner() throws Exception {
        MockMultipartFile emptyFile = new MockMultipartFile("file", new byte[0]);

        mockMvc.perform(multipart("/admin/partner/add")
                .file(emptyFile)
                .param("companyName", "New Partner")
                .param("partnerPackage", "MEDIA"))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/partner/view"));

        assertThat(partnerRepository.findAll()).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void getRemove_shouldDeletePartner() throws Exception {
        long partnerId = testPartner.getId();

        mockMvc.perform(get("/admin/partner/remove/" + partnerId))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/partner/view"));

        assertThat(partnerRepository.findById(partnerId)).isEmpty();
    }
}
