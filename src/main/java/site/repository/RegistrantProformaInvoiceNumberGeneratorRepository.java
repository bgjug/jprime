package site.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import site.model.Registrant;

/**
 * @author Mihail Stoynov
 */
@Repository(value = RegistrantProformaInvoiceNumberGeneratorRepository.NAME)
public interface RegistrantProformaInvoiceNumberGeneratorRepository extends RegistrantNumberGeneratorRepository<Registrant.ProformaInvoiceNumberGenerator> {

	String NAME = "registrantProformaInvoiceNumberGeneratorRepository";
}
