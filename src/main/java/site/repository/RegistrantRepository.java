package site.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import site.model.Registrant;

@Repository(value = RegistrantRepository.NAME)
public interface RegistrantRepository extends PagingAndSortingRepository<Registrant, Long> {

	String NAME = "registrantRepository";
//    String SELECT_ARTICLES_BY_TAG_STMT = "SELECT a FROM Article a JOIN a.tags t WHERE t.name = :tagName and a.published=true";

//    @Query(SELECT_ARTICLES_BY_TAG_STMT)
//    List<Article> findByTag(@Param("tagName") String tagName);

//    @Query(SELECT_ARTICLES_BY_TAG_STMT)
//    Page<Article> findByTag(@Param("tagName") String tagName, Pageable pageable);

//    String SELECT_PUBLISHED_ARTICLES = "SELECT a FROM Article a WHERE a.published=true";

//    @Query(SELECT_PUBLISHED_ARTICLES)
//    List<Article> findAllPublished();

//    @Query(SELECT_PUBLISHED_ARTICLES)
//    Page<Article> findAllPublishedArticles(Pageable pageable);
	Registrant findByEpayInvoiceNumber(long epayInvoiceNumber);
}
