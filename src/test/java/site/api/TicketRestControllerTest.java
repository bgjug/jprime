package site.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import site.app.Application;
import site.config.Globals;
import site.model.Registrant;
import site.model.Visitor;
import site.repository.RegistrantRepository;
import site.repository.VisitorRepository;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Application.class})
@WebAppConfiguration
@Transactional
public class TicketRestControllerTest {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private VisitorRepository visitorRepository;

    @Autowired
    private RegistrantRepository registrantRepository;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        Registrant r = createRegistrant();

        createVisitorForRegistrant(r);
        createVisitorForRegistrantWithoutTicket(r);
    }

    @Test
    public void registerTicketNotFound() throws Exception {
        mockMvc.perform(post("/api/ticket/_Invalid_ticket_id"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void registerTicket() throws Exception {
        String ticketReferenceId = "_TICKET_REFERENCE_ID_";
        Visitor visitor = findVisitorByTicket(ticketReferenceId);
        assertFalse(visitor.isRegistered());

        mockMvc.perform(post("/api/ticket/" + ticketReferenceId))
            .andExpect(status().isOk());

        visitor = findVisitorByTicket(ticketReferenceId);
        assertTrue(visitor.isRegistered());
    }

    private Visitor findVisitorByTicket(String ticket) throws Exception {
        MvcResult result = mockMvc.perform(get("/api/visitor/" + Globals.CURRENT_BRANCH.getLabel() + "/" + ticket))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn();
        return new ObjectMapper().readValue(result.getResponse().getContentAsString(), Visitor.class);
    }

    private Visitor createVisitorForRegistrant(Registrant r) {
        Visitor v = new Visitor();
        v.setName("Funny Name");
        v.setEmail("funny.name@funky.com");
        v.setCompany("Funky company Ltd.");
        v.setTicket("_TICKET_REFERENCE_ID_");
        v.setRegistrant(r);
        visitorRepository.save(v);
        return v;
    }

    private Visitor createVisitorForRegistrantWithoutTicket(Registrant r) {
        Visitor v = new Visitor();
        v.setName("Visitor NoTicket");
        v.setEmail("no.ticket.visitor@funky.com");
        v.setCompany("Funky company Ltd.");
        v.setRegistrant(r);
        visitorRepository.save(v);
        return v;
    }

    private Registrant createRegistrant() {
        Registrant r = new Registrant();
        r.setEmail("funky@email.com");
        r.setName("Funky company Ltd.");
        r.setBranch(Globals.CURRENT_BRANCH);
        registrantRepository.save(r);
        return r;
    }
}