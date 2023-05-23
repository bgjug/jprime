package site.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import site.model.Registrant;

/**
 * @author Mihail Stoynov
 */
@Repository(value = RegistrantProformaInvoiceNumberGeneratorRepository.NAME)
public interface RegistrantProformaInvoiceNumberGeneratorRepository extends RegistrantNumberGeneratorRepository<Registrant.ProformaInvoiceNumberGenerator> {

	String NAME = "registrantProformaInvoiceNumberGeneratorRepository";
}
