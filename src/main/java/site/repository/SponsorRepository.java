package site.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import site.model.Sponsor;

@Repository(value = SponsorRepository.NAME)
public interface SponsorRepository extends JpaRepository<Sponsor, Long> {

	String NAME = "sponsorRepository";

    List<Sponsor> findByActive(Boolean active);
}
