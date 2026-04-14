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
import site.model.Article;
import site.model.Tag;
import site.model.User;
import site.repository.ArticleRepository;
import site.repository.TagRepository;
import site.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for NavController.
 * Tests navigation and blog article display functionality.
 */
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Transactional
class NavControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private UserRepository userRepository;

    private MockMvc mockMvc;
    private Article publishedArticle;
    private Article unpublishedArticle;
    private Tag testTag;

    @BeforeAll
    public static void beforeAll(@Autowired BranchService branchService) {
        DefaultBranchUtil.createDefaultBranch(branchService);
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        articleRepository.deleteAll();
        tagRepository.deleteAll();

        User author = new User();
        author.setEmail("author@example.com");
        author.setFirstName("Test");
        author.setLastName("Author");
        author = userRepository.save(author);

        testTag = new Tag("TestTag");
        testTag = tagRepository.save(testTag);

        publishedArticle = new Article("Published Article", "Published content");
        publishedArticle.setPublished(true);
        publishedArticle.setAuthor(author);
        publishedArticle.addTag(testTag);
        publishedArticle = articleRepository.save(publishedArticle);

        unpublishedArticle = new Article("Unpublished Article", "Unpublished content");
        unpublishedArticle.setPublished(false);
        unpublishedArticle.setAuthor(author);
        unpublishedArticle = articleRepository.save(unpublishedArticle);
    }

    @Test
    void getNav_shouldReturnBlogPage() throws Exception {
        mockMvc.perform(get("/nav"))
            .andExpect(status().isOk())
            .andExpect(view().name("blog"))
            .andExpect(model().attributeExists("articles"))
            .andExpect(model().attributeExists("tags"))
            .andExpect(model().attributeExists("jprime_year"));
    }

    @Test
    void getNavByTag_withMultipleArticles_shouldReturnBlogPage() throws Exception {
        // Add another published article with the same tag
        Article article2 = new Article("Another Article", "Content");
        article2.setPublished(true);
        article2.setAuthor(publishedArticle.getAuthor());
        article2.addTag(testTag);
        articleRepository.save(article2);

        mockMvc.perform(get("/nav/" + testTag.getName()))
            .andExpect(status().isOk())
            .andExpect(view().name("blog"))
            .andExpect(model().attributeExists("articles"))
            .andExpect(model().attributeExists("tags"));
    }

    @Test
    void getNavByTag_withInvalidTag_shouldReturn404() throws Exception {
        mockMvc.perform(get("/nav/InvalidTag"))
            .andExpect(status().isOk())
            .andExpect(view().name("404"));
    }

    @Test
    void getNavArticleById_shouldReturnSinglePost() throws Exception {
        mockMvc.perform(get("/nav/article/" + publishedArticle.getId()))
            .andExpect(status().isOk())
            .andExpect(view().name("single-post"))
            .andExpect(model().attribute("article", publishedArticle))
            .andExpect(model().attributeExists("tags"))
            .andExpect(model().attributeExists("jprime_year"));
    }

    @Test
    void getNavArticleById_withUnpublished_shouldNotShowArticle() throws Exception {
        mockMvc.perform(get("/nav/article/" + unpublishedArticle.getId()))
            .andExpect(status().isOk())
            .andExpect(view().name("single-post"))
            .andExpect(model().attributeExists("tags"));
    }

    @Test
    void getNavArticleById_withInvalidId_shouldReturn404() throws Exception {
        mockMvc.perform(get("/nav/article/999999"))
            .andExpect(status().isOk())
            .andExpect(view().name("404"));
    }

    @Test
    void getNavArticleByTitle_shouldReturnSinglePost() throws Exception {
        mockMvc.perform(get("/nav/article").param("title", "Published Article"))
            .andExpect(status().isOk())
            .andExpect(view().name("single-post"))
            .andExpect(model().attribute("article", publishedArticle));
    }

    @Test
    void getTeam_shouldReturnTeamPage() throws Exception {
        mockMvc.perform(get("/team"))
            .andExpect(status().isOk())
            .andExpect(view().name("team"))
            .andExpect(model().attributeExists("tags"));
    }

    @Test
    void getVenue_shouldReturnVenuePage() throws Exception {
        mockMvc.perform(get("/venue"))
            .andExpect(status().isOk())
            .andExpect(view().name("venue"))
            .andExpect(model().attributeExists("tags"))
            .andExpect(model().attributeExists("conference_dates"));
    }
}
