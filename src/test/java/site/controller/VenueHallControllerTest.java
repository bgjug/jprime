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
import site.model.VenueHall;
import site.repository.VenueHallRepository;

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static site.controller.AdminVenueHallController.HALLS_EDIT_JSP;
import static site.controller.AdminVenueHallController.HALLS_VIEW_JSP;

/**
 * @author Ivan St. Ivanov
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Transactional
public class VenueHallControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    @Qualifier(VenueHallRepository.NAME)
    private VenueHallRepository venueHallRepository;

    private MockMvc mockMvc;
    private VenueHall betaHall;

    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        this.betaHall = venueHallRepository.save(new VenueHall("Beta", "600 seats"));
    }

    @Test
    public void getViewVenueHallsShouldReturnAllVenueHalls() throws Exception {
        mockMvc.perform(get("/admin/hall/view"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("halls", hasSize(1)))
                .andExpect(model().attribute("halls", containsInAnyOrder(betaHall)))
                .andExpect(view().name(HALLS_VIEW_JSP));
    }

    @Test
    public void getNewVenueHallShouldReturnFormWithEmptyHall() throws Exception {
        mockMvc.perform(get("/admin/hall/add"))
                .andExpect(model().attribute("hall", is(new VenueHall())))
                .andExpect(status().isOk())
                .andExpect(view().name(HALLS_EDIT_JSP));
    }

    @Test
    public void getEditVenueHallShouldReturnFormWithHall() throws Exception {
        mockMvc.perform(get("/admin/hall/edit/" + betaHall.getId()))
                .andExpect(model().attribute("hall", is(betaHall)))
                .andExpect(status().isOk())
                .andExpect(view().name(HALLS_EDIT_JSP));
    }

    @Test
    public void shouldAddNewVenueHall() throws Exception {
        mockMvc.perform(post("/admin/hall/add")
                .param("name", "Alpha")
                .param("description", "400 seats"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/admin/hall/view"));
        List<VenueHall> venueHalls = venueHallRepository.findAll();
        assertThat(venueHalls.size(), is(2));
        assertThat(venueHalls.stream().filter(hall -> hall.getName().equals("Alpha")).count(), is(
                1L));
    }

    @Test
    public void getDeleteShouldRemoveHall() throws Exception {
        mockMvc.perform(get("/admin/hall/remove/" + betaHall.getId()));
        List<VenueHall> venueHalls = venueHallRepository.findAll();
        assertThat(venueHalls.size(), is(0));
    }
}
