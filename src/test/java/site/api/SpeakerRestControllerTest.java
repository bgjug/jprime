package site.api;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
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
import site.model.Speaker;
import site.repository.SpeakerRepository;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Application.class})
@WebAppConfiguration
@Transactional
public class SpeakerRestControllerTest {

    private static final TypeReference<? extends List<Speaker>> SPEAKER_LIST = new TypeReference<List<Speaker>>() {};

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private SpeakerRepository speakerRepository;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
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
    }

    @Test
    public void searchForSpeaker() throws Exception {
        SpeakerSearch search = new SpeakerSearch("first", "", null);
        MvcResult result = mockMvc.perform(
                post("/api/speaker/search/" + Globals.CURRENT_BRANCH.getLabel()).contentType(
                    MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(search)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        List<Speaker> speakerList =
            new ObjectMapper().readValue(result.getResponse().getContentAsString(), SPEAKER_LIST);
        Assertions.assertEquals(1, speakerList.size());
        Speaker spe = speakerList.get(0);
        assertTrue(spe.getAccepted());
        assertFalse(spe.getFeatured());
    }
}