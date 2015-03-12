package site.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import site.model.Article;

@Repository(value = ArticleRepository.NAME)
public interface ArticleRepository extends PagingAndSortingRepository<Article, Long> {

	String NAME = "articleRepository";

	public Page<Article> findByTag(String tag, Pageable pagable);
}
