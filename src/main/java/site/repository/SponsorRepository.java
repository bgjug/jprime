package site.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import site.model.Sponsor;
import site.model.User;

@Repository(value = SponsorRepository.NAME)
public interface SponsorRepository extends PagingAndSortingRepository<Sponsor, Long> {

	String NAME = "sponsorRepository";

}
