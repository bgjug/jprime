package site.repository;

import org.springframework.stereotype.Repository;

import site.model.Registrant;

/**
 * @author Mihail Stoynov
 */
@Repository("proformaNumberGenerator")
public interface RegistrantProformaInvoiceNumberGeneratorRepository extends RegistrantNumberGeneratorRepository<Registrant.ProformaInvoiceNumberGenerator> {

    @Override
    default Registrant.ProformaInvoiceNumberGenerator newInstance() {
        return new Registrant.ProformaInvoiceNumberGenerator();
    }
}
