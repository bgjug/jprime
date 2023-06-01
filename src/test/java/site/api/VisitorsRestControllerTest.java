package site.api;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {Application.class})
@WebAppConfiguration
@Transactional
class VisitorsRestControllerTest {

    private static final TypeReference<List<VisitorFromJSON>> VISITOR_LIST =
        new TypeReference<List<VisitorFromJSON>>() {};

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private VisitorRepository visitorRepository;

    @Autowired
    private RegistrantRepository registrantRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        Registrant r = createRegistrant();

        createVisitorForRegistrant(r);
        createVisitorForRegistrantWithoutTicket(r);
    }

    @Test
    void testFindAllVisitors() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/visitor/" + Globals.CURRENT_BRANCH.getLabel()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<VisitorFromJSON> visitorList = new ObjectMapper().readValue(jsonResponse, VISITOR_LIST);
        assertEquals(1, visitorList.size());
        VisitorFromJSON visitor = visitorList.get(0);
        assertNotNull(visitor.getRegistrantName());
        assertNotNull(visitor.getTicket());
    }

    @Test
    void testFindVisitorByTicket() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/api/visitor/" + Globals.CURRENT_BRANCH.getLabel() + "/_TICKET_REFERENCE_ID_"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn();
        VisitorFromJSON visitor =
            new ObjectMapper().readValue(result.getResponse().getContentAsString(), VisitorFromJSON.class);
        assertNotNull(visitor.getRegistrantName());
        assertNotNull(visitor.getTicket());
    }

    @Test
    void testFindVisitorByTicketBadTicket() throws Exception {
        mockMvc.perform(get("/api/visitor/" + Globals.CURRENT_BRANCH.getLabel() + "/_INVALID_TICKET_ID_"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testSearchForVisitorByFirstName() throws Exception {
        VisitorSearch search = new VisitorSearch(null, "fu", null, null);
        MvcResult result = mockMvc.perform(
                post("/api/visitor/search/" + Globals.CURRENT_BRANCH.getLabel()).contentType(
                    MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(search)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        List<VisitorFromJSON> visitorList =
            new ObjectMapper().readValue(result.getResponse().getContentAsString(), VISITOR_LIST);
        assertEquals(1, visitorList.size());
        VisitorFromJSON visitor = visitorList.get(0);
        assertNotNull(visitor.getRegistrantName());
        assertNotNull(visitor.getTicket());
    }

    @Test
    void testSearchForVisitorByLastName() throws Exception {
        VisitorSearch search = new VisitorSearch(null, null, "na", null);
        MvcResult result = mockMvc.perform(
                post("/api/visitor/search/" + Globals.CURRENT_BRANCH.getLabel()).contentType(
                    MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(search)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        List<VisitorFromJSON> visitorList =
            new ObjectMapper().readValue(result.getResponse().getContentAsString(), VISITOR_LIST);
        assertEquals(1, visitorList.size());
        VisitorFromJSON visitor = visitorList.get(0);
        assertNotNull(visitor.getRegistrantName());
        assertNotNull(visitor.getTicket());
    }

    @Test
    void testSearchForVisitorByFirstAndLastName() throws Exception {
        VisitorSearch search = new VisitorSearch(null, "fu", "na", null);
        MvcResult result = mockMvc.perform(
                post("/api/visitor/search/" + Globals.CURRENT_BRANCH.getLabel()).contentType(
                    MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(search)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        List<VisitorFromJSON> visitorList =
            new ObjectMapper().readValue(result.getResponse().getContentAsString(), VISITOR_LIST);
        assertEquals(1, visitorList.size());
        VisitorFromJSON visitor = visitorList.get(0);
        assertNotNull(visitor.getRegistrantName());
        assertNotNull(visitor.getTicket());
    }

    @Test
    void testSearchForVisitorByCompany() throws Exception {
        VisitorSearch search = new VisitorSearch(null, null, null, "fun");
        MvcResult result = mockMvc.perform(
                post("/api/visitor/search/" + Globals.CURRENT_BRANCH.getLabel()).contentType(
                    MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(search)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        List<VisitorFromJSON> visitorList =
            new ObjectMapper().readValue(result.getResponse().getContentAsString(), VISITOR_LIST);
        assertEquals(1, visitorList.size());
        VisitorFromJSON visitor = visitorList.get(0);
        assertNotNull(visitor.getRegistrantName());
        assertNotNull(visitor.getTicket());
    }

    @Test
    void testSearchForVisitorByEmail() throws Exception {
        VisitorSearch search = new VisitorSearch("funky.com", null, null, null);
        MvcResult result = mockMvc.perform(
                post("/api/visitor/search/" + Globals.CURRENT_BRANCH.getLabel()).contentType(
                    MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(search)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        List<VisitorFromJSON> visitorList =
            new ObjectMapper().readValue(result.getResponse().getContentAsString(), VISITOR_LIST);
        assertEquals(1, visitorList.size());
        VisitorFromJSON visitor = visitorList.get(0);
        assertNotNull(visitor.getRegistrantName());
        assertNotNull(visitor.getTicket());
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