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
import site.model.Speaker;
import site.repository.SpeakerRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {Application.class})
@WebAppConfiguration
@Transactional
class SpeakerRestControllerTest {

    private static final TypeReference<? extends List<Speaker>> SPEAKER_LIST = new TypeReference<List<Speaker>>() {};

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private SpeakerRepository speakerRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        createSpeakers();
    }

    private void createSpeakers() {
        Speaker speaker = new Speaker("FirstSpeaker", "FirstLastName", "first@jprime.io", "", "", false, true);
        speakerRepository.save(speaker);

        speaker = new Speaker("SecondSpeaker", "SecondLastName", "second@jprime.io", "", "", false, true);
        speakerRepository.save(speaker);

        speaker = new Speaker("ThirdSpeaker", "ThirdLastName", "third@jprime.io", "", "", true, false);
        speakerRepository.save(speaker);

        speaker = new Speaker("ForthSpeaker", "ForthLastName", "fourth@jprime.io", "", "", false, false);
        speakerRepository.save(speaker);
    }

    @Test
    void searchAllSpeakers() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/api/speaker/" + Globals.CURRENT_BRANCH.getLabel()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        List<Speaker> speakerList =
            new ObjectMapper().readValue(result.getResponse().getContentAsString(), SPEAKER_LIST);
        assertEquals(3, speakerList.size());
        speakerList.forEach(sp -> {
            assertNotNull(sp.getName());
            assertNotNull(sp.getEmail());
        });

        assertTrue(speakerList.stream().map(sp -> sp.getFeatured() || sp.getAccepted()).reduce(false, Boolean::logicalOr));
    }

    @Test
    void searchForSpeaker() throws Exception {
        SpeakerSearch search = new SpeakerSearch("first", "", null);
        MvcResult result = mockMvc.perform(
                post("/api/speaker/search/" + Globals.CURRENT_BRANCH.getLabel()).contentType(
                    MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(search)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        List<Speaker> speakerList =
            new ObjectMapper().readValue(result.getResponse().getContentAsString(), SPEAKER_LIST);
        assertEquals(1, speakerList.size());
        Speaker spe = speakerList.get(0);
        assertTrue(spe.getAccepted());
        assertFalse(spe.getFeatured());
    }
}