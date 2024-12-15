package site.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import site.model.Branch;
import site.model.Visitor;

/**
 * Created by mitia on 28.04.15.
 */
@Repository(value = VisitorRepository.NAME)
public interface VisitorRepository extends JpaRepository<Visitor, Long> {


    String NAME = "visitorRepository";

    @Query("SELECT v FROM Registrant r join r.visitors v where r.branch = :branch ORDER BY v.createdDate DESC")
    List<Visitor> findAllNewestUsers(Branch branch);

    List<Visitor> findByNameIgnoreCase(String name);

    List<Visitor> findByNameIgnoreCaseAndCompanyIgnoreCase(String name, String company);

    @Query("SELECT v FROM Registrant r join r.visitors v where r.branch = :branch and v.ticket is not null")
    List<Visitor> findAllWithTicket(Branch branch);

    Optional<Visitor> findByTicket(String ticket);
}
