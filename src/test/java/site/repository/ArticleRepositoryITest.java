package site.repository;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import site.app.Application;
import site.facade.BranchService;
import site.facade.DefaultBranchUtil;
import site.model.Article;
import site.model.Tag;
import site.model.User;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for ArticleRepository custom queries.
 * Tests tag queries and published filter functionality.
 */
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Transactional
class ArticleRepositoryITest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BranchService branchService;

    private Tag javaTag;
    private Tag springTag;
    private Article publishedArticle1;
    private Article publishedArticle2;
    private Article unpublishedArticle;
    private User author;

    @BeforeAll
    public static void beforeAll(@Autowired BranchService branchService) {
        DefaultBranchUtil.createDefaultBranch(branchService);
    }

    @BeforeEach
    void setUp() {
        articleRepository.deleteAll();
        tagRepository.deleteAll();
        userRepository.deleteAll();

        // Create author
        author = new User();
        author.setEmail("author@jprime.io");
        author.setFirstName("Test");
        author.setLastName("Author");
        author = userRepository.save(author);

        // Create tags
        javaTag = new Tag("Java");
        springTag = new Tag("Spring");
        javaTag = tagRepository.save(javaTag);
        springTag = tagRepository.save(springTag);

        // Create published article with Java tag
        publishedArticle1 = new Article("Java Article", "Content about Java");
        publishedArticle1.setPublished(true);
        publishedArticle1.setAuthor(author);
        publishedArticle1.addTag(javaTag);
        publishedArticle1 = articleRepository.save(publishedArticle1);

        // Create published article with Spring tag
        publishedArticle2 = new Article("Spring Article", "Content about Spring");
        publishedArticle2.setPublished(true);
        publishedArticle2.setAuthor(author);
        publishedArticle2.addTag(springTag);
        publishedArticle2 = articleRepository.save(publishedArticle2);

        // Create unpublished article with Java tag
        unpublishedArticle = new Article("Unpublished Java Article", "Draft content");
        unpublishedArticle.setPublished(false);
        unpublishedArticle.setAuthor(author);
        unpublishedArticle.addTag(javaTag);
        unpublishedArticle = articleRepository.save(unpublishedArticle);
    }

    @Test
    void findByTag_shouldReturnOnlyPublishedArticlesWithTag() {
        List<Article> javaArticles = articleRepository.findByTag("Java");

        assertThat(javaArticles).hasSize(1);
        assertThat(javaArticles).containsExactly(publishedArticle1);
        assertThat(javaArticles).doesNotContain(unpublishedArticle);
    }

    @Test
    void findByTag_shouldReturnEmptyListForNonExistentTag() {
        List<Article> articles = articleRepository.findByTag("NonExistent");

        assertThat(articles).isEmpty();
    }

    @Test
    void findByTagWithPageable_shouldReturnPublishedArticlesWithTag() {
        Page<Article> javaArticles = articleRepository.findByTag("Java", PageRequest.of(0, 10));

        assertThat(javaArticles.getContent()).hasSize(1);
        assertThat(javaArticles.getContent()).containsExactly(publishedArticle1);
    }

    @Test
    void findAllPublished_shouldReturnOnlyPublishedArticles() {
        List<Article> published = articleRepository.findAllPublished();

        assertThat(published).hasSize(2);
        assertThat(published).containsExactlyInAnyOrder(publishedArticle1, publishedArticle2);
        assertThat(published).doesNotContain(unpublishedArticle);
    }

    @Test
    void findAllLatestArticles_shouldReturnAllArticlesOrderedByCreatedDate() {
        List<Article> articles = articleRepository.findAllLatestArticles();

        assertThat(articles).hasSize(3);
        // Should be ordered by creation date descending
        assertThat(articles.get(0).getCreatedDate()).isAfterOrEqualTo(articles.get(1).getCreatedDate());
        assertThat(articles.get(1).getCreatedDate()).isAfterOrEqualTo(articles.get(2).getCreatedDate());
    }

    @Test
    void findAllLatestArticlesWithPageable_shouldReturnPagedArticles() {
        Page<Article> articles = articleRepository.findAllLatestArticles(PageRequest.of(0, 2));

        assertThat(articles.getContent()).hasSize(2);
        assertThat(articles.getTotalElements()).isEqualTo(3);
    }

    @Test
    void findByPublishedTrueOrderByCreatedDateDesc_shouldReturnPublishedArticlesOrdered() {
        Page<Article> articles = articleRepository.findByPublishedTrueOrderByCreatedDateDesc(PageRequest.of(0, 10));

        assertThat(articles.getContent()).hasSize(2);
        assertThat(articles.getContent()).containsExactlyInAnyOrder(publishedArticle1, publishedArticle2);
    }
}
