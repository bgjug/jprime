package site.controller;

import org.junit.Before;
import org.junit.Ignore;
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
import site.model.VisitorStatus;
import site.repository.RegistrantRepository;

import java.util.List;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static site.controller.TicketsController.TICKETS_REGISTER_JSP;
import static site.controller.TicketsController.TICKETS_RESULT_JSP;

/**
 * @author Ivan St. Ivanov
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Transactional
public class TicketsEpayRegisterControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    @Qualifier(RegistrantRepository.NAME)
    private RegistrantRepository registrantRepository;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void getShouldReturnTicketsEpayRegisterJsp() throws Exception {
        mockMvc.perform(get("/tickets/epay"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("registrant", is(new Registrant())))
                .andExpect(view().name(TICKETS_REGISTER_JSP));
    }

    @Test
    @Ignore("ignored since adding captcha, has to be updated")
    public void postNonCompanyRegistrantShouldSaveVisitorDataAsRegistrant() throws Exception {
        mockMvc.perform(post("/tickets/epay")
                .param("visitors[0].name", "John Doe")
                .param("visitors[0].email", "john@example.com")
                .param("visitors[0].company", "Example")
                .param("company", "false"))
                .andExpect(status().isOk())
                .andExpect(view().name(TICKETS_RESULT_JSP));
        List<Registrant> allRegistrants = (List<Registrant>) registrantRepository.findAll();
        assertThat(allRegistrants.size(), is(1));
        Registrant registrant = allRegistrants.get(0);
        assertThat("John Doe", is(registrant.getName()));
        assertThat("john@example.com", is(registrant.getEmail()));
        assertThat(false, is(registrant.isCompany()));
        assertThat(1, is(registrant.getVisitors().size()));
        assertThat("John Doe", is(registrant.getVisitors().get(0).getName()));
        assertThat("john@example.com", is(registrant.getVisitors().get(0).getEmail()));
        assertThat("Example", is(registrant.getVisitors().get(0).getCompany()));
    }

    @Test
    @Ignore("ignored since adding captcha, has to be updated")
    public void postCompanyRegistrantShouldSaveInvoiceData() throws Exception {
        mockMvc.perform(post("/tickets/epay")
                .param("visitors[0].name", "John Doe")
                .param("visitors[0].email", "john@example.com")
                .param("visitors[0].company", "Example")
                .param("company", "true")
                .param("name", "Adams Family")
                .param("address", "0001 Cemetery Lane")
                .param("eik", "666")
                .param("vatNumber", "666")
                .param("mol", "Gomez Adams")
                .param("email", "gomez@adams.com"))
                .andExpect(status().isOk())
                .andExpect(view().name(TICKETS_RESULT_JSP));
        List<Registrant> allRegistrants = (List<Registrant>) registrantRepository.findAll();
        assertThat(allRegistrants.size(), is(1));
        Registrant registrant = allRegistrants.get(0);
        assertThat("Adams Family", is(registrant.getName()));
        assertThat("gomez@adams.com", is(registrant.getEmail()));
        assertThat("Adams Family", is(registrant.getName()));
        assertThat("0001 Cemetery Lane", is(registrant.getAddress()));
        assertThat("BG666", is(registrant.getVatNumber()));
        assertThat("666", is(registrant.getEik()));
        assertThat("Gomez Adams", is(registrant.getMol()));
        assertThat(true, is(registrant.isCompany()));
        assertThat(1, is(registrant.getVisitors().size()));
        assertThat("John Doe", is(registrant.getVisitors().get(0).getName()));
        assertThat("john@example.com", is(registrant.getVisitors().get(0).getEmail()));
        assertThat("Example", is(registrant.getVisitors().get(0).getCompany()));
        assertThat(VisitorStatus.REQUESTING, is(registrant.getVisitors().get(0).getStatus()));
    }

    @Test
    @Ignore("ignored since adding captcha, has to be updated")
    public void shouldBeAbleToSaveMoreVisitorsForOneRegistrant() throws Exception {
        mockMvc.perform(post("/tickets/epay")
                .param("visitors[0].name", "Lurch")
                .param("visitors[0].email", "lurch@example.com")
                .param("visitors[1].name", "Morticia Adams")
                .param("visitors[1].email", "morticia@example.com")
                .param("company", "true")
                .param("name", "Adams Family")
                .param("address", "0001 Cemetery Lane")
                .param("vatNumber", "666")
                .param("mol", "Gomez Adams")
                .param("email", "gomez@adams.com"))
                .andExpect(status().isOk())
                .andExpect(view().name(TICKETS_RESULT_JSP));
        List<Registrant> allRegistrants = (List<Registrant>) registrantRepository.findAll();
        assertThat(allRegistrants.size(), is(1));
        Registrant registrant = allRegistrants.get(0);
        assertThat(2, is(registrant.getVisitors().size()));
        assertThat(registrant.getVisitors().get(0).getName(), anyOf(is("Morticia Adams"),
                is("Lurch")));
        assertThat(VisitorStatus.REQUESTING, is(registrant.getVisitors().get(0).getStatus()));
    }

    @Test
    public void getTicketsEpayShouldReturnEmptyRegistrant() throws Exception {
        mockMvc.perform(get("/tickets/epay"))
                .andExpect(status().isOk())
                .andExpect(view().name(TICKETS_REGISTER_JSP))
                .andExpect(model().attribute("registrant", new Registrant()));
    }
}