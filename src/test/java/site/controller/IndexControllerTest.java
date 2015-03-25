package site.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import site.app.Application;
import site.model.Sponsor;
import site.model.SponsorPackage;
import site.model.Tag;
import site.repository.ArticleRepository;
import site.repository.SponsorRepository;
import site.repository.TagRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * @author Ivan St. Ivanov
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class IndexControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    @Qualifier(TagRepository.NAME)
    private TagRepository tagRepository;

    @Autowired
    @Qualifier(SponsorRepository.NAME)
    private SponsorRepository sponsorRepository;

    @Autowired
    @Qualifier(ArticleRepository.NAME)
    private ArticleRepository articleRepository;

    private Sponsor google;
    private Sponsor apple;
    private Sponsor sap;
    private Tag tag1;
    private Tag tag2;

    @Before
    @Transactional
    public void setup() throws IOException {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        articleRepository.deleteAll();
        tagRepository.deleteAll();
        sponsorRepository.deleteAll();

        google = new Sponsor(SponsorPackage.GOLD, "Google", "http://www.google.com", "sponsor@google.com");
        apple = new Sponsor(SponsorPackage.GOLD, "Apple", "http://www.apple.com", "sponsor@apple.com");
        sap = new Sponsor(SponsorPackage.PLATINUM, "SAP", "http://www.sap.com", "sponsor@sap.com");
        sap.setLogo(Files.readAllBytes(Paths.get("src/main/webapp/images/sap.png")));
        sponsorRepository.save(google); sponsorRepository.save(apple); sponsorRepository.save(sap);

        tag1 = tagRepository.save(new Tag("tag1")); tag2 = tagRepository.save(new Tag("tag2"));
    }

    @Test
    public void shouldContainSponsors() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name(IndexController.PAGE_INDEX))
                .andExpect(model().attribute("platinumSponsors", contains(sap)))
                .andExpect(model().attribute("goldSponsors", contains(google, apple)))
                .andExpect(model().attribute("silverSponsors", hasSize(0)))
                .andExpect(model().attribute("tags", containsInAnyOrder(tag1, tag2)));
    }
}
