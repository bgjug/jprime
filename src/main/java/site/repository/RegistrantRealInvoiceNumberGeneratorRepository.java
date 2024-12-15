package site.repository;

import org.springframework.stereotype.Repository;

import site.model.Registrant;

/**
 * @author Mihail Stoynov
 */
@Repository("realInvoiceNumberGenerator")
public interface RegistrantRealInvoiceNumberGeneratorRepository extends RegistrantNumberGeneratorRepository<Registrant.RealInvoiceNumberGenerator> {

    @Override
    default Registrant.RealInvoiceNumberGenerator newInstance() {
        return new Registrant.RealInvoiceNumberGenerator();
    }
}
