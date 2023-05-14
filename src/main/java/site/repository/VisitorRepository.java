package site.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import site.model.Visitor;

import java.util.List;
import java.util.Optional;

/**
 * Created by mitia on 28.04.15.
 */
@Repository(value = VisitorRepository.NAME)
public interface VisitorRepository extends PagingAndSortingRepository<Visitor, Long> {


    String NEWEST_VISITORS = "SELECT v FROM Visitor v ORDER BY v.createdDate DESC";

    String NAME = "visitorRepository";

    @Query(NEWEST_VISITORS)
    List<Visitor> findAllNewestUsers();

    List<Visitor> findByNameIgnoreCase(String name);

    List<Visitor> findByNameIgnoreCaseAndCompanyIgnoreCase(String name, String company);

    @Query("SELECT v from Visitor as v where v.ticket is not null")
    List<Visitor> findAllWithTicket();

    Optional<Visitor> findByTicket(String ticket);
}
