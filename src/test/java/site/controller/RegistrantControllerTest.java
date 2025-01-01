package site.controller;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static site.controller.AdminRegistrantController.REGISTRANT_EDIT_JSP;
import static site.controller.AdminRegistrantController.REGISTRANT_VIEW_JSP;

/**
 * @author Ivan St. Ivanov
 */
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Transactional
class RegistrantControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private RegistrantRepository registrantRepository;

    private MockMvc mockMvc;

    private static Registrant adamsFamily;

    private static Registrant ivan;

    @Autowired
    private BranchService branchService;

    @Autowired
    private VisitorRepository visitorRepository;

    @BeforeAll
    public static void beforeAll(@Autowired BranchService branchService) {
        DefaultBranchUtil.createDefaultBranch(branchService);
    }

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        Branch currentBranch = branchService.getCurrentBranch();
        assertThat(currentBranch).isNotNull();
        visitorRepository.deleteAll();
        registrantRepository.deleteAll();

        adamsFamily = new Registrant(true, "Adams Family", "0001 Cemetery Lane", "666", "Gomez Adamz",
            "gomez@adams.com");
        List<Visitor> adamsVisitors =
            Arrays.asList(new Visitor(adamsFamily, "Lurch Adams", "lurch@adams.com", "Adams Family"),
                new Visitor(adamsFamily, "Morticia Adams", "morticia@adams.com", "Adams Family"));
        adamsFamily.setVisitors(adamsVisitors);
        adamsFamily.setBranch(currentBranch);
        adamsFamily = registrantRepository.save(adamsFamily);

        ivan = new Registrant("Ivan St. Ivanov", "ivan.st.ivanov@gmail.com");
        ivan.setVisitors(List.of(new Visitor(ivan, "Ivan St. Ivanov", "ivan.st.ivanov@gmail.com", "JUG")));
        ivan.setBranch(currentBranch);
        ivan = registrantRepository.save(ivan);
    }

    @Test
    void getViewShouldReturnRegistrantViewWithAllRegistrants() throws Exception {
        mockMvc.perform(get("/admin/registrant/view"))
            .andExpect(status().isOk())
            .andExpect(model().attribute("registrants", Matchers.hasSize(2)))
            .andExpect(model().attribute("registrants", containsInAnyOrder(adamsFamily, ivan)))
            .andExpect(view().name(REGISTRANT_VIEW_JSP));
    }

    @Test
    void getAddShouldReturnRegistrantFormWithEmptyRegistrant() throws Exception {
        mockMvc.perform(get("/admin/registrant/add"))
            .andExpect(status().isOk())
            .andExpect(model().attribute("registrant", is(new Registrant())))
            .andExpect(view().name(REGISTRANT_EDIT_JSP));
    }

    @Test
    void shouldAddNewRegistrant() throws Exception {
        mockMvc.perform(post("/admin/registrant/add").param("company", "true")
                .param("name", "SAP Labs Bulgaria")
                .param("address", "136A Tzar Boris III blvd.")
                .param("vatNumber", "BG123456")
                .param("eik", "123456")
                .param("mol", "Radoslav Nikolov")
                .param("email", "rado@sap.bg")
                .param("paymentType", Registrant.PaymentType.BANK_TRANSFER.name()))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/registrant/view"));
        List<Registrant> registrants = registrantRepository.findAll();
        assertThat(registrants).hasSize(3);
        assertThat(registrants.stream()
            .filter(registrant -> "SAP Labs Bulgaria".equals(registrant.getName()))
            .count()).isEqualTo(1L);
    }

    @Test
    void getEditShouldReturnRegisterFormWithRequestedRegistrant() throws Exception {
        mockMvc.perform(get("/admin/registrant/edit/" + ivan.getId()))
            .andExpect(status().isOk())
            .andExpect(model().attribute("registrant", is(ivan)))
            .andExpect(view().name(REGISTRANT_EDIT_JSP));
    }

    @Test
    void getDeleteShouldRemoveRegistrant() throws Exception {
        List<Registrant> registrants = registrantRepository.findAll();
        assertThat(registrants.stream()
            .filter(registrant -> "Ivan St. Ivanov".equals(registrant.getName()))
            .count()).isEqualTo(1L);
        mockMvc.perform(get("/admin/registrant/remove/" + ivan.getId()))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/registrant/view"));
        registrants = registrantRepository.findAll();
        assertThat(registrants).hasSize(1);
        assertThat(registrants.stream()
            .filter(registrant -> "Ivan St. Ivanov".equals(registrant.getName()))
            .count()).isZero();
    }

    @Test
    void getAddVisitorFormShouldReturnVisitorFormWithCurrentRegistrant() throws Exception {
        mockMvc.perform(get("/admin/registrant/" + ivan.getId() + "/addVisitor"))
            .andExpect(status().isOk())
            .andExpect(model().attribute("visitor", hasProperty("registrant", is(ivan))))
            .andExpect(view().name(AdminVisitorController.VISITOR_EDIT_JSP));
    }

    @Test
    void shouldBeAbleToAddExistingRegistrantOnceAgain() throws Exception {
        mockMvc.perform(post("/admin/registrant/add").param("company", "true")
                .param("name", "Adams Family")
                .param("address", "0001 Cemetery Lane")
                .param("vatNumber", "666")
                .param("eik", "666")
                .param("mol", "Gomez Adamz")
                .param("email", "gomez@adams.com")
                .param("paymentType", Registrant.PaymentType.BANK_TRANSFER.name()))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/registrant/view"));
        List<Registrant> registrants = registrantRepository.findAll();
        assertThat(registrants).hasSize(3);
        assertThat(registrants.stream()
            .filter(registrant -> "Adams Family".equals(registrant.getName()))
            .count()).isEqualTo(2L);
    }
}
