package site.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import site.model.VisitorJPro;

/**
 * Created by mitia on 28.04.15.
 */
@Repository(value = VisitorJProRepository.NAME)
public interface VisitorJProRepository extends JpaRepository<VisitorJPro, Long> {

    String NAME = "visitorJProRepository";

    String NEWEST_VISITORS = "SELECT v FROM VisitorJPro v ORDER BY v.createdDate DESC";

    @Query(NEWEST_VISITORS)
    List<VisitorJPro> findAllNewestUsers();

    List<VisitorJPro> findByNameIgnoreCase(String name);

    List<VisitorJPro> findByNameIgnoreCaseAndEmailIgnoreCase(String name, String email);
}
