package site.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import site.app.Application;
import site.model.Registrant;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static site.controller.TicketsController.TICKETS_EPAY_REGISTER_JSP;
import static site.controller.TicketsController.TICKETS_JSP;

/**
 * @author Ivan St. Ivanov
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class TicketsControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void getShouldReturnTicketsJsp() throws Exception {
        mockMvc.perform(get("/tickets"))
                .andExpect(status().isOk())
                .andExpect(view().name(TICKETS_JSP));
    }

    @Test
    public void getTicketsEpayShouldReturnEmptyRegistrantAndAllPaymentTypes() throws Exception {
        mockMvc.perform(get("/tickets/epay"))
                .andExpect(status().isOk())
                .andExpect(view().name(TICKETS_EPAY_REGISTER_JSP))
                .andExpect(model().attribute("registrant", new Registrant()))
                .andExpect(model().attribute("paymentTypes", containsInAnyOrder(
                        Registrant.PaymentType.BANK_TRANSFER.toString(),
                        Registrant.PaymentType.EPAY_ACCOUNT.toString(),
                        Registrant.PaymentType.EPAY_CREDIT_CARD.toString())));
    }
}
