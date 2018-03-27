package site.controller;

import org.hamcrest.core.IsInstanceOf;
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
import site.model.*;
import site.repository.ArticleRepository;
import site.repository.PartnerRepository;
import site.repository.SpeakerRepository;
import site.repository.SponsorRepository;
import site.repository.SubmissionRepository;
import site.repository.TagRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Transactional
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

    @Autowired
    @Qualifier(SpeakerRepository.NAME)
    private SpeakerRepository speakerRepository;
    
    @Autowired
    @Qualifier(PartnerRepository.NAME)
    private PartnerRepository partnerRepository;
    
    
    @Autowired
    @Qualifier(SubmissionRepository.NAME)
    private SubmissionRepository submissionRepository;

    private Sponsor google;
    private Sponsor apple;
    private Sponsor sap;
    private Sponsor hater;
    private Tag tag1;
    private Tag tag2;

    private Speaker brianGoetz;

    private Partner softUni;

    private Partner baristo;

    @Before
    public void setup() throws IOException {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        google = new Sponsor(SponsorPackage.GOLD, "Google", "http://www.google.com", "sponsor@google.com");
        apple = new Sponsor(SponsorPackage.GOLD, "Apple", "http://www.apple.com", "sponsor@apple.com");
        sap = new Sponsor(SponsorPackage.PLATINUM, "SAP", "http://www.sap.com", "sponsor@sap.com");
        sap.setLogo(Files.readAllBytes(Paths.get("src/main/webapp/images/sap.png")));
        hater = new Sponsor(SponsorPackage.SILVER, "Now I hate Java", "http://hatejava.com", "hater@hatejava.com", false);
        sponsorRepository.save(google); sponsorRepository.save(apple); sponsorRepository.save(sap); sponsorRepository.save(hater);

        tag1 = tagRepository.save(new Tag("tag1")); tag2 = tagRepository.save(new Tag("tag2"));

        brianGoetz = new Speaker("Brian", "Goetz", "brian@oracle.com", "The Java Language Architect", "@briangoetz", true, true);
        brianGoetz = speakerRepository.save(brianGoetz);

        Speaker ivanIvanov = new Speaker("Ivan St.", "Ivanov", "ivan@jprime.io", "JBoss Forge", "@ivan_stefanov", false, false);
        speakerRepository.save(ivanIvanov);
        
        softUni = new Partner();
        softUni.setCompanyName("SoftUni");
        partnerRepository.save(softUni);

        baristo = new Partner();
        baristo.setCompanyName("Baristo");
        baristo.setPartnerPackage(PartnerPackage.OTHER);
        partnerRepository.save(baristo);
    }

    @Test
    public void controllerShouldContainRequiredData() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name(IndexController.PAGE_INDEX))
                .andExpect(model().attribute("platinumSponsors", containsInAnyOrder(sap)))
                .andExpect(model().attribute("goldSponsors", containsInAnyOrder(google, apple)))
                .andExpect(model().attribute("silverSponsors", hasSize(0)))
                .andExpect(model().attribute("tags", containsInAnyOrder(tag1, tag2)))
                .andExpect(model().attribute("acceptedSpeakers", contains(brianGoetz)))
                .andExpect(model().attribute("partnerChunks", IsInstanceOf.instanceOf(List.class)))
                .andExpect(model().attribute("eventPartnerChunks", IsInstanceOf.instanceOf(List.class)));
    }
}
