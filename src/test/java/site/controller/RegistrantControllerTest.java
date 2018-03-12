package site.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import site.app.Application;
import site.model.Registrant;
import site.model.Visitor;
import site.repository.RegistrantRepository;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
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
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Transactional
public class RegistrantControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    @Qualifier(RegistrantRepository.NAME)
    private RegistrantRepository registrantRepository;

    private MockMvc mockMvc;
    private Registrant adamsFamily;
    private Registrant ivan;

    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        insertTestRegistrants();
    }

    private void insertTestRegistrants() {
        adamsFamily = new Registrant(true, "Adams Family", "0001 Cemetery Lane", "666", "Gomez Adamz", "gomez@adams.com");
        List<Visitor> adamsVisitors = Arrays.asList(
                new Visitor(adamsFamily, "Lurch Adams", "lurch@adams.com", "Adams Family"),
                new Visitor(adamsFamily, "Morticia Adams", "morticia@adams.com", "Adams Family")
        );
        adamsFamily.setVisitors(adamsVisitors);
        adamsFamily = registrantRepository.save(adamsFamily);

        ivan = new Registrant("Ivan St. Ivanov", "ivan.st.ivanov@gmail.com");
        ivan.setVisitors(Arrays.asList(
                new Visitor(ivan, "Ivan St. Ivanov", "ivan.st.ivanov@gmail.com", "JUG")));
        ivan = registrantRepository.save(ivan);
    }

    @Test
    public void getViewShouldReturnRegistrantViewWithAllRegistrants() throws Exception {
        mockMvc.perform(get("/admin/registrant/view"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("registrants", hasSize(2)))
                .andExpect(model().attribute("registrants", containsInAnyOrder(adamsFamily, ivan)))
                .andExpect(view().name(REGISTRANT_VIEW_JSP));
    }

    @Test
    public void getAddShouldReturnRegistrantFormWithEmptyRegistrant() throws Exception {
        mockMvc.perform(get("/admin/registrant/add"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("registrant", is(new Registrant())))
                .andExpect(view().name(REGISTRANT_EDIT_JSP));
    }

    @Test
    public void shouldAddNewRegistrant() throws Exception {
        mockMvc.perform(post("/admin/registrant/add")
                .param("company", "true")
                .param("name", "SAP Labs Bulgaria")
                .param("address", "136A Tzar Boris III blvd.")
                .param("vatNumber", "BG123456")
                .param("eik", "123456")
                .param("mol", "Radoslav Nikolov")
                .param("email", "rado@sap.bg")
                .param("paymentType", Registrant.PaymentType.BANK_TRANSFER.name()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/admin/registrant/view"));
        List<Registrant> registrants = (List<Registrant>) registrantRepository.findAll();
        assertThat(registrants.size(), is(3));
        assertThat(registrants.stream().filter(registrant -> registrant.getName().equals("SAP Labs Bulgaria")).count(), is(
                1L));
    }

    @Test
    public void getEditShouldReturnRegisterFormWithRequestedRegistrant() throws Exception {
        mockMvc.perform(get("/admin/registrant/edit/" + ivan.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("registrant", is(ivan)))
                .andExpect(view().name(REGISTRANT_EDIT_JSP));
    }

    @Test
    public void getDeleteShouldRemoveRegistrant() throws Exception {
        List<Registrant> registrants = (List<Registrant>) registrantRepository.findAll();
        assertThat(registrants.stream().filter(registrant -> registrant.getName().equals("Ivan St. Ivanov")).count(), is(
                1L));
        mockMvc.perform(get("/admin/registrant/remove/" + ivan.getId()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/admin/registrant/view"));
        registrants = (List<Registrant>) registrantRepository.findAll();
        assertThat(registrants.size(), is(1));
        assertThat(registrants.stream().filter(registrant -> registrant.getName().equals("Ivan St. Ivanov")).count(), is(
                0L));
    }

    @Test
    public void getAddVisitorFormShouldReturnVisitorFormWithCurrentRegistrant() throws Exception {
        mockMvc.perform(get("/admin/registrant/" + ivan.getId() + "/addVisitor"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("visitor", hasProperty("registrant", is(ivan))))
                .andExpect(view().name(AdminVisitorController.VISITOR_EDIT_JSP));
    }

    @Test
    public void shouldBeAbleToAddExistingRegistrantOnceAgain() throws Exception {
        mockMvc.perform(post("/admin/registrant/add")
                .param("company", "true")
                .param("name", "Adams Family")
                .param("address", "0001 Cemetery Lane")
                .param("vatNumber", "666")
                .param("eik", "666")
                .param("mol", "Gomez Adamz")
                .param("email", "gomez@adams.com")
                .param("paymentType", Registrant.PaymentType.BANK_TRANSFER.name()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/admin/registrant/view"));
        List<Registrant> registrants = (List<Registrant>) registrantRepository.findAll();
        assertThat(registrants.size(), is(3));
        assertThat(registrants.stream().filter(registrant -> registrant.getName().equals("Adams Family")).count(), is(
                2L));
    }
}
