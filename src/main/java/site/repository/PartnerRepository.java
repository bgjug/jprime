package site.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import site.model.Partner;
import site.model.PartnerPackage;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {

    List<Partner> findByActiveAndPartnerPackage(Boolean active, PartnerPackage partnerPackage);
}
