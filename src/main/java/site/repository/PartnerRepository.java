package site.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import site.model.Partner;
import site.model.Sponsor;

import java.util.List;

@Repository(value = PartnerRepository.NAME)
public interface PartnerRepository extends PagingAndSortingRepository<Partner, Long> {

	String NAME = "partnerRepository";

    List<Partner> findAll();

    List<Partner> findByActive(Boolean active);
}
