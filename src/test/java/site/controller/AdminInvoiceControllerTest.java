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
import site.repository.RegistrantRepository;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for AdminInvoiceController.
 * Tests invoice generation and management operations.
 */
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Transactional
class AdminInvoiceControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private RegistrantRepository registrantRepository;

    @Autowired
    private BranchService branchService;

    private MockMvc mockMvc;
    private Registrant testRegistrant;
    private Branch currentBranch;

    @BeforeAll
    public static void beforeAll(@Autowired BranchService branchService) {
        DefaultBranchUtil.createDefaultBranch(branchService);
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        registrantRepository.deleteAll();

        currentBranch = branchService.getCurrentBranch();

        testRegistrant = new Registrant("Test Company", "company@example.com", currentBranch);
        testRegistrant.setAddress("123 Main St");
        testRegistrant.setVatNumber("BG123456789");
        testRegistrant.setEik("123456789");
        testRegistrant.setMol("John Doe");
        testRegistrant.setProformaInvoiceNumber(1001L);
        testRegistrant = registrantRepository.save(testRegistrant);
    }

    @Test
    void getProforma_shouldReturnInvoiceDataForm() throws Exception {
        mockMvc.perform(get("/admin/invoice/proforma/" + testRegistrant.getId()))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/invoice/invoiceData"))
            .andExpect(model().attributeExists("invoiceData"))
            .andExpect(model().attributeExists("registrantId"));
    }

    @Test
    void getFinal_shouldReturnInvoiceDataForm() throws Exception {
        mockMvc.perform(get("/admin/invoice/final/" + testRegistrant.getId()))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/invoice/invoiceData"))
            .andExpect(model().attributeExists("invoiceData"))
            .andExpect(model().attributeExists("registrantId"));
    }
}
