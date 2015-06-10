package site.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import site.model.User;
import site.model.Visitor;

import java.util.List;

/**
 * Created by mitia on 28.04.15.
 */
@Repository(value = VisitorRepository.NAME)
public interface VisitorRepository extends PagingAndSortingRepository<Visitor, Long> {

    String NEWEST_VISITORS = "SELECT v FROM Visitor v ORDER BY v.createdDate DESC";

    String NAME = "visitorRepository";

    List<User> findByEmail(String email);

    @Query(NEWEST_VISITORS)
    List<Visitor> findAllNewestUsers();
    
}
