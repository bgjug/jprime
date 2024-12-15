package site.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import site.model.Sponsor;

@Repository
public interface SponsorRepository extends JpaRepository<Sponsor, Long> {

    List<Sponsor> findByActive(Boolean active);
}
