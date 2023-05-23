package site.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import site.model.Registrant;

@Repository(value = RegistrantEpayInvoiceNumberGeneratorRepository.NAME)
public interface RegistrantEpayInvoiceNumberGeneratorRepository extends RegistrantNumberGeneratorRepository<Registrant.EpayInvoiceNumberGenerator> {

	String NAME = "registrantEpayInvoiceNumberGeneratorRepository";
}
