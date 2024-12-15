package site.repository;

import org.springframework.stereotype.Repository;

import site.model.Registrant;

@Repository(value="epayInvoiceNumberGenerator")
public interface RegistrantEpayInvoiceNumberGeneratorRepository extends RegistrantNumberGeneratorRepository<Registrant.EpayInvoiceNumberGenerator> {

    @Override
    default Registrant.EpayInvoiceNumberGenerator newInstance() {
        return new Registrant.EpayInvoiceNumberGenerator();
    }
}
