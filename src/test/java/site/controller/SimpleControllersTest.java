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
import site.model.Speaker;
import site.model.Sponsor;
import site.model.Partner;
import site.model.PartnerPackage;
import site.model.SponsorPackage;
import site.repository.SpeakerRepository;
import site.repository.SponsorRepository;
import site.repository.PartnerRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for simple passthrough controllers.
 * Tests AppController, QRController, and ImageController.
 */
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Transactional
class SimpleControllersTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private SpeakerRepository speakerRepository;

    @Autowired
    private SponsorRepository sponsorRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    private MockMvc mockMvc;
    private Speaker testSpeaker;
    private Sponsor testSponsor;
    private Partner testPartner;

    @BeforeAll
    public static void beforeAll(@Autowired BranchService branchService) {
        DefaultBranchUtil.createDefaultBranch(branchService);
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        speakerRepository.deleteAll();
        sponsorRepository.deleteAll();
        partnerRepository.deleteAll();

        // Create test speaker with image
        testSpeaker = new Speaker("John", "Doe", "john@example.com", "Expert", "@johndoe");
        testSpeaker.setPicture(new byte[]{1, 2, 3, 4, 5});
        testSpeaker = speakerRepository.save(testSpeaker);

        // Create test sponsor with logo
        testSponsor = new Sponsor();
        testSponsor.setCompanyName("Test Sponsor");
        testSponsor.setSponsorPackage(SponsorPackage.GOLD);
        testSponsor.setLogo(new byte[]{10, 20, 30, 40, 50});
        testSponsor = sponsorRepository.save(testSponsor);

        // Create test partner with logo
        testPartner = new Partner();
        testPartner.setCompanyName("Test Partner");
        testPartner.setPartnerPackage(PartnerPackage.MEDIA);
        testPartner.setLogo(new byte[]{100, 110, 120});
        testPartner = partnerRepository.save(testPartner);
    }

    // AppController Tests
    @Test
    void appController_shouldRedirectToAppIndexHtml() throws Exception {
        mockMvc.perform(get("/app"))
            .andExpect(status().isFound())
            .andExpect(redirectedUrl("/app/index.html"));
    }

    // QRController Tests
    @Test
    void qrController_getQr_shouldReturnQrPage() throws Exception {
        mockMvc.perform(get("/qr"))
            .andExpect(status().isOk())
            .andExpect(view().name("qr"));
    }

    @Test
    void qrController_getTuk_shouldReturnRegPresencePage() throws Exception {
        mockMvc.perform(get("/qr/tuk"))
            .andExpect(status().isOk())
            .andExpect(view().name("reg-presence"))
            .andExpect(model().attributeExists("visitor"));
    }

    @Test
    void qrController_getTukJpro_shouldReturnRegPresenceJproPage() throws Exception {
        mockMvc.perform(get("/qr/tukjpro"))
            .andExpect(status().isOk())
            .andExpect(view().name("reg-presence-jpro"))
            .andExpect(model().attributeExists("visitor"));
    }

    // ImageController Tests
    @Test
    void imageController_getSpeakerPhoto_shouldReturnImage() throws Exception {
        mockMvc.perform(get("/image/speaker/" + testSpeaker.getId()))
            .andExpect(status().isOk())
            .andExpect(content().bytes(testSpeaker.getPicture()));
    }

    @Test
    void imageController_getSponsorLogo_shouldReturnImage() throws Exception {
        mockMvc.perform(get("/image/sponsor/" + testSponsor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().bytes(testSponsor.getLogo()));
    }

    @Test
    void imageController_getPartnerLogo_shouldReturnImage() throws Exception {
        mockMvc.perform(get("/image/partner/" + testPartner.getId()))
            .andExpect(status().isOk())
            .andExpect(content().bytes(testPartner.getLogo()));
    }

    @Test
    void imageController_getSpeakerPhoto_withNonExistentId_shouldReturnEmptyArray() throws Exception {
        mockMvc.perform(get("/image/speaker/999999"))
            .andExpect(status().isOk())
            .andExpect(content().bytes(new byte[0]));
    }
}
