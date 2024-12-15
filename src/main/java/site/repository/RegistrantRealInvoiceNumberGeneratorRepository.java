package site.repository;

import org.springframework.stereotype.Repository;

import site.model.Registrant;

/**
 * @author Mihail Stoynov
 */
@Repository(value = RegistrantRealInvoiceNumberGeneratorRepository.NAME)
public interface RegistrantRealInvoiceNumberGeneratorRepository extends RegistrantNumberGeneratorRepository<Registrant.RealInvoiceNumberGenerator> {

	String NAME = "registrantRealInvoiceNumberGeneratorRepository";
}
