package site.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import site.app.Application;
import site.facade.BranchService;
import site.facade.DefaultBranchUtil;
import site.model.Branch;
import site.model.SessionLevel;
import site.model.SessionType;
import site.model.Speaker;
import site.model.Submission;
import site.model.SubmissionStatus;
import site.repository.BranchRepository;
import site.repository.SpeakerRepository;
import site.repository.SubmissionRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@Transactional
class SpeakerPortalControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BranchService branchService;
    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private SpeakerRepository speakerRepository;
    @Autowired
    private SubmissionRepository submissionRepository;

    private Branch current;
    private Speaker ivan;
    private Submission s1;
    private Submission s2;
    private Submission s3;

    @BeforeAll
    static void beforeAll(@Autowired BranchService branchService) {
        DefaultBranchUtil.createDefaultBranch(branchService);
    }

    @BeforeEach
    void setUp() {
        current = branchService.getCurrentBranch();
        current.setCfpOpenDate(LocalDateTime.now().minusDays(1));
        current.setCfpCloseDate(LocalDateTime.now().plusDays(1));
        branchRepository.save(current);
        Branch other = branchService.findBranchByYear(2024);

        ivan = speakerRepository.save(new Speaker("Ivan", "Ivanov", "ivan@jprime.io", "h", "ivan0"));
        ivan.setBio("bio");
        Speaker other1 = speakerRepository.save(new Speaker("Petar", "Petrov", "petar@jprime.io", "h", "p"));
        s1 = submissionRepository.save(sub("S1", ivan, null, SubmissionStatus.SUBMITTED, current));
        s2 = submissionRepository.save(sub("S2", other1, ivan, SubmissionStatus.ACCEPTED, current));
        s3 = submissionRepository.save(sub("S3", ivan, null, SubmissionStatus.SUBMITTED, other));
    }

    private static Submission sub(String title, Speaker sp, Speaker co, SubmissionStatus st, Branch b) {
        Submission s = new Submission(title, "d", SessionLevel.BEGINNER, SessionType.CONFERENCE_SESSION, sp, co, st,
            false);
        s.setBranch(b);
        return s;
    }

    @Test
    void dashboardListsOwnAndCoSpeakerCurrentSubmissions() throws Exception {
        mockMvc.perform(get("/my").with(user("ivan@jprime.io").roles("USER")))
            .andExpect(status().isOk())
            .andExpect(view().name("my"))
            .andExpect(model().attribute("submissions",
                containsInAnyOrder(hasProperty("title", is("S1")), hasProperty("title", is("S2")))));
    }

    @Test
    void dashboardMarksEditable() throws Exception {
        mockMvc.perform(get("/my").with(user("ivan@jprime.io").roles("USER")))
            .andExpect(model().attribute("editable",
                Map.of(s1.getId(), true, s2.getId(), false)));
    }

    @Test
    void profileEditPersists() throws Exception {
        mockMvc.perform(profilePost("B").param("twitter", "@ivan"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/my"));
        Speaker saved = speakerRepository.findByEmail("ivan@jprime.io");
        assertThat(saved.getBio()).isEqualTo("B");
        assertThat(saved.getTwitter()).isEqualTo("ivan");
    }

    @Test
    void profileIgnoresEmailParam() throws Exception {
        mockMvc.perform(profilePost("bio").param("email", "evil@x.io").param("password", "pw"))
            .andExpect(redirectedUrl("/my"));
        Speaker saved = speakerRepository.findByEmail("ivan@jprime.io");
        assertThat(saved).isNotNull();
        assertThat(saved.getPassword()).isNotEqualTo("pw");
        assertThat(speakerRepository.findByEmail("evil@x.io")).isNull();
    }

    @Test
    void profileRequiresBio() throws Exception {
        mockMvc.perform(profilePost(""))
            .andExpect(status().isOk())
            .andExpect(view().name("my-profile"))
            .andExpect(model().attributeHasFieldErrors("profile", "bio"));
    }

    @Test
    void profilePostWithoutCsrfForbidden() throws Exception {
        mockMvc.perform(multipart("/my/profile").with(user("ivan@jprime.io").roles("USER"))
                .param("firstName", "I").param("lastName", "I").param("bio", "B"))
            .andExpect(status().isForbidden());
    }

    @Test
    void oauthSpeakerSeesDashboard() throws Exception {
        mockMvc.perform(get("/my").with(oauth2Login().attributes(a -> a.put("email", "ivan@jprime.io"))))
            .andExpect(status().isOk());
    }

    @Test
    void adminOnMyIsForbidden() throws Exception {
        mockMvc.perform(get("/my").with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN"))))
            .andExpect(status().isForbidden());
    }

    private org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder profilePost(String bio) {
        return multipart("/my/profile").with(user("ivan@jprime.io").roles("USER")).with(csrf())
            .param("firstName", "Ivan").param("lastName", "Ivanov").param("bio", bio);
    }
}
