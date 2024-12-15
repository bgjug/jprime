package site.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import site.model.Article;

@Repository(value = ArticleRepository.NAME)
public interface ArticleRepository extends JpaRepository<Article, Long> {

	String NAME = "articleRepository";
    String SELECT_ARTICLES_BY_TAG_STMT = "SELECT a FROM Article a JOIN a.tags t WHERE t.name = :tagName and a.published=true ORDER BY a.createdDate DESC ";
    String SELECT_PUBLISHED_ARTICLES = "SELECT a FROM Article a WHERE a.published=true";
    String SELECT_NEWEST_ARTICLES = "SELECT a FROM Article a ORDER BY a.createdDate DESC";

    @Query(SELECT_ARTICLES_BY_TAG_STMT)
    List<Article> findByTag(@Param("tagName") String tagName);

    @Query(SELECT_ARTICLES_BY_TAG_STMT)
    Page<Article> findByTag(@Param("tagName") String tagName, Pageable pageable);

    @Query(SELECT_PUBLISHED_ARTICLES)
    List<Article> findAllPublished();

    @Query(SELECT_NEWEST_ARTICLES)
    List<Article> findAllLatestArticles();

    @Query(SELECT_NEWEST_ARTICLES)
    Page<Article> findAllLatestArticles(Pageable pageable);

    Page<Article> findByPublishedTrueOrderByCreatedDateDesc(Pageable pageable);
    
    Article findByTitle(String title);
}
