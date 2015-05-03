package site.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import site.model.Registrant;

/**
 * @author Mihail Stoynov
 */
@Repository(value = RegistrantProformaInvoiceNumberGeneratorRepository.NAME)
public interface RegistrantProformaInvoiceNumberGeneratorRepository extends PagingAndSortingRepository<Registrant.ProformaInvoiceNumberGenerator, Long> {

	String NAME = "registrantProformaInvoiceNumberGeneratorRepository";
    Registrant.ProformaInvoiceNumberGenerator findFirstByOrderByIdAsc();
}
