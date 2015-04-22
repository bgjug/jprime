package site.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import site.model.Registrant;

/**
 * @author Mihail Stoynov
 */
@Repository(value = RegistrantRealInvoiceNumberGeneratorRepository.NAME)
public interface RegistrantRealInvoiceNumberGeneratorRepository extends PagingAndSortingRepository<Registrant.RealInvoiceNumberGenerator, Long> {

	String NAME = "registrantRealInvoiceNumberGeneratorRepository";
    Registrant.RealInvoiceNumberGenerator findFirstByOrderByIdAsc();
}
