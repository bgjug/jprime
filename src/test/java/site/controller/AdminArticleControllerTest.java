package site.controller;

import java.util.List;

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
import site.model.Article;
import site.model.Tag;
import site.model.User;
import site.repository.ArticleRepository;
import site.repository.TagRepository;
import site.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for AdminArticleController.
 * Tests article CRUD operations in admin panel.
 */
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Transactional
class AdminArticleControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BranchService branchService;

    private MockMvc mockMvc;
    private Article testArticle;
    private Tag testTag;
    private User admin;

    @BeforeAll
    public static void beforeAll(@Autowired BranchService branchService) {
        DefaultBranchUtil.createDefaultBranch(branchService);
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        articleRepository.deleteAll();
        tagRepository.deleteAll();

        // Ensure admin user exists
        List<User> adminUsers = userRepository.findByEmail("admin@jsprime.io");
        if (adminUsers.isEmpty()) {
            admin = new User();
            admin.setEmail("admin@jsprime.io");
            admin.setFirstName("Admin");
            admin.setLastName("");
            admin = userRepository.save(admin);
        } else {
            admin = adminUsers.get(0);
        }

        testTag = new Tag("Java");
        testTag = tagRepository.save(testTag);

        testArticle = new Article("Test Article", "Test content");
        testArticle.setPublished(true);
        testArticle.setAuthor(admin);
        testArticle.addTag(testTag);
        testArticle = articleRepository.save(testArticle);
    }

    @Test
    void getView_shouldReturnArticleViewWithAllArticles() throws Exception {
        mockMvc.perform(get("/admin/article/view"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/article/view"))
            .andExpect(model().attributeExists("articles"))
            .andExpect(model().attribute("articles", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    void getViewById_shouldReturnSingleArticle() throws Exception {
        mockMvc.perform(get("/admin/article/view/" + testArticle.getId()))
            .andExpect(status().isOk())
            .andExpect(view().name("single-post"))
            .andExpect(model().attribute("article", testArticle))
            .andExpect(model().attributeExists("jprime_year"));
    }

    @Test
    void getAdd_shouldReturnFormWithEmptyArticle() throws Exception {
        mockMvc.perform(get("/admin/article/add"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/article/edit"))
            .andExpect(model().attribute("article", is(new Article())))
            .andExpect(model().attributeExists("tags"));
    }

    @Test
    void getEdit_shouldReturnFormWithExistingArticle() throws Exception {
        mockMvc.perform(get("/admin/article/edit/" + testArticle.getId()))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/article/edit"))
            .andExpect(model().attribute("article", testArticle))
            .andExpect(model().attributeExists("tags"));
    }

    @Test
    void postAdd_shouldCreateNewArticle() throws Exception {
        mockMvc.perform(post("/admin/article/add")
                .param("title", "New Article")
                .param("text", "New article content")
                .param("published", "true"))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/article/view"));

        Article saved = articleRepository.findByTitle("New Article");
        assertThat(saved).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("New Article");
        assertThat(saved.getText()).isEqualTo("New article content");
        assertThat(saved.getAuthor()).isNotNull();
    }

    @Test
    void postAdd_withValidationErrors_shouldReturnEditForm() throws Exception {
        mockMvc.perform(post("/admin/article/add")
                .param("title", "")  // Empty title should fail validation
                .param("text", ""))   // Empty text should fail validation
            .andExpect(status().isOk())
            .andExpect(view().name("admin/article/edit"));
    }

    @Test
    void getRemove_shouldDeleteArticle() throws Exception {
        long articleId = testArticle.getId();

        mockMvc.perform(get("/admin/article/remove/" + articleId))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/article/view"));

        assertThat(articleRepository.findById(articleId)).isEmpty();
    }

    @Test
    void postAdd_shouldSetAdminAsAuthor() throws Exception {
        mockMvc.perform(post("/admin/article/add")
                .param("title", "Article with Auto Author")
                .param("text", "Content")
                .param("published", "false"))
            .andExpect(status().isFound());

        Article saved = articleRepository.findByTitle("Article with Auto Author");
        assertThat(saved).isNotNull();
        assertThat(saved.getAuthor()).isNotNull();
        assertThat(saved.getAuthor().getEmail()).isEqualTo("admin@jsprime.io");
    }
}
