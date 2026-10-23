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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import site.app.Application;
import site.facade.BranchService;
import site.facade.DefaultBranchUtil;
import site.facade.MailService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Autowired
    private MailService mailer;

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

    private MockHttpServletRequestBuilder editPost(Submission s,
                                                                                               String email, String title) {
        return post("/my/submissions/" + s.getId()).with(user(email).roles("USER")).with(csrf())
            .param("title", title).param("description", "D2").param("level", "ADVANCED")
            .param("type", SessionType.WORKSHOP.name());
    }

    @Test
    void ownerEditsSubmittedSubmission() throws Exception {
        mockMvc.perform(editPost(s1, "ivan@jprime.io", "T2"))
            .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/my"));
        Submission saved = submissionRepository.findById(s1.getId()).orElseThrow();
        assertThat(saved.getTitle()).isEqualTo("T2");
        assertThat(saved.getDescription()).isEqualTo("D2");
        assertThat(saved.getLevel()).isEqualTo(SessionLevel.ADVANCED);
        assertThat(saved.getType()).isEqualTo(SessionType.WORKSHOP);
    }

    @Test
    void coSpeakerCanEdit() throws Exception {
        Speaker co = speakerRepository.save(new Speaker("Co", "S", "co@jprime.io", "h", "co"));
        s1.setCoSpeaker(co);
        submissionRepository.save(s1);
        mockMvc.perform(editPost(s1, "co@jprime.io", "T2")).andExpect(redirectedUrl("/my"));
        assertThat(submissionRepository.findById(s1.getId()).orElseThrow().getTitle()).isEqualTo("T2");
    }

    @Test
    void strangerGets404() throws Exception {
        speakerRepository.save(new Speaker("Str", "A", "str@jprime.io", "h", "str"));
        mockMvc.perform(get("/my/submissions/" + s1.getId()).with(user("str@jprime.io").roles("USER")))
            .andExpect(status().isNotFound());
        mockMvc.perform(editPost(s1, "str@jprime.io", "")).andExpect(status().isNotFound());
        mockMvc.perform(editPost(s1, "str@jprime.io", "T2")).andExpect(status().isNotFound());
        assertThat(submissionRepository.findById(s1.getId()).orElseThrow().getTitle()).isEqualTo("S1");
    }

    @Test
    void acceptedSubmissionIsReadOnly() throws Exception {
        mockMvc.perform(get("/my/submissions/" + s2.getId()).with(user("ivan@jprime.io").roles("USER")))
            .andExpect(status().isOk()).andExpect(view().name("my-submission"))
            .andExpect(model().attribute("editable", false));
        mockMvc.perform(editPost(s2, "ivan@jprime.io", "T2")).andExpect(status().isForbidden());
        assertThat(submissionRepository.findById(s2.getId()).orElseThrow().getTitle()).isEqualTo("S2");
    }

    @Test
    void editIgnoresOtherFields() throws Exception {
        Speaker stranger = speakerRepository.save(new Speaker("Str", "A", "str@jprime.io", "h", "str"));
        mockMvc.perform(editPost(s1, "ivan@jprime.io", "T2").param("status", "ACCEPTED")
                .param("featured", "true").param("speaker.id", String.valueOf(stranger.getId())))
            .andExpect(redirectedUrl("/my"));
        Submission saved = submissionRepository.findById(s1.getId()).orElseThrow();
        assertThat(saved.getStatus()).isEqualTo(SubmissionStatus.SUBMITTED);
        assertThat(saved.getFeatured()).isNotEqualTo(true);
        assertThat(saved.getSpeaker().getId()).isEqualTo(ivan.getId());
    }

    @Test
    void blankTitleRejected() throws Exception {
        mockMvc.perform(editPost(s1, "ivan@jprime.io", ""))
            .andExpect(status().isOk()).andExpect(view().name("my-submission"))
            .andExpect(model().attributeHasFieldErrors("form", "title"));
    }

    @Test
    void missingLevelRejected() throws Exception {
        mockMvc.perform(post("/my/submissions/" + s1.getId()).with(user("ivan@jprime.io").roles("USER")).with(csrf())
                .param("title", "T2").param("description", "D2").param("type", SessionType.WORKSHOP.name()))
            .andExpect(status().isOk()).andExpect(view().name("my-submission"))
            .andExpect(model().attributeHasFieldErrors("form", "level"));
        assertThat(submissionRepository.findById(s1.getId()).orElseThrow().getLevel())
            .isEqualTo(SessionLevel.BEGINNER);
    }

    @Test
    void editSendsNoMail() throws Exception {
        assertThat(mailer).isInstanceOf(MailServiceMock.class);
        ((MailServiceMock) mailer).clear();
        mockMvc.perform(editPost(s1, "ivan@jprime.io", "T2")).andExpect(redirectedUrl("/my"));
        assertThat(((MailServiceMock) mailer).getRecipientAddresses()).isEmpty();
    }

    private MockHttpServletRequestBuilder profilePost(String bio) {
        return multipart("/my/profile").with(user("ivan@jprime.io").roles("USER")).with(csrf())
            .param("firstName", "Ivan").param("lastName", "Ivanov").param("bio", bio);
    }

    @Test
    void nonImageUploadGivesGlobalErrorAndKeepsPicture() throws Exception {
        mockMvc.perform(multipart("/my/profile")
                .file(new org.springframework.mock.web.MockMultipartFile("picture", "a.txt", "text/plain",
                    "not an image".getBytes()))
                .with(user("ivan@jprime.io").roles("USER")).with(csrf())
                .param("firstName", "Ivan").param("lastName", "Ivanov").param("bio", "B"))
            .andExpect(status().isOk())
            .andExpect(view().name("my-profile"))
            .andExpect(model().attributeHasErrors("profile"))
            .andExpect(result -> {
                var br = (org.springframework.validation.BindingResult) result.getModelAndView().getModel()
                    .get(org.springframework.validation.BindingResult.MODEL_KEY_PREFIX + "profile");
                assertThat(br.getFieldErrors("picture")).isEmpty();
                assertThat(br.getGlobalError().getCode()).isEqualTo("picture.invalid");
            });
        assertThat(speakerRepository.findByEmail("ivan@jprime.io").getPicture()).isNull();
    }

    @Test
    void overlongHeadlineRejected() throws Exception {
        mockMvc.perform(profilePost("B").param("headline", "x".repeat(256)))
            .andExpect(view().name("my-profile"))
            .andExpect(model().attributeHasFieldErrors("profile", "headline"));
    }
}
