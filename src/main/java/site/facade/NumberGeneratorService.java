package site.facade;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import site.model.JPrimeException;
import site.model.Registrant;
import site.repository.RegistrantNumberGeneratorRepository;

@Service
public class NumberGeneratorService {

    private final RegistrantNumberGeneratorRepository<?> epayInvoiceNumberGeneratorRepository;

    private final RegistrantNumberGeneratorRepository<?> realInvoiceNumberGeneratorRepository;

    private final RegistrantNumberGeneratorRepository<?> proformaNumberGeneratorRepository;

    public NumberGeneratorService(@Qualifier(
            "epayInvoiceNumberGenerator") RegistrantNumberGeneratorRepository<?> epayInvoiceNumberGeneratorRepository,
        @Qualifier(
            "realInvoiceNumberGenerator") RegistrantNumberGeneratorRepository<?> realInvoiceNumberGeneratorRepository,
        @Qualifier(
            "proformaNumberGenerator") RegistrantNumberGeneratorRepository<?> proformaNumberGeneratorRepository) {
        this.epayInvoiceNumberGeneratorRepository = epayInvoiceNumberGeneratorRepository;
        this.realInvoiceNumberGeneratorRepository = realInvoiceNumberGeneratorRepository;
        this.proformaNumberGeneratorRepository = proformaNumberGeneratorRepository;
    }

    private <T extends Registrant.NumberGenerator> long getInvoiceNumber(
        RegistrantNumberGeneratorRepository<T> repository, long initialValue) {
        //mihail: get the invoice number from the other table
        synchronized (repository.repositoryClass()) {
            long count = repository.count();
            if (count > 1) {
                throw new JPrimeException(
                    "Mihail: RealInvoiceNumberGenerator table has more than one row. Fix that");
            }

            T realInvoiceNumberGenerator;
            if (count == 0) {
                realInvoiceNumberGenerator = repository.newInstance();
                realInvoiceNumberGenerator.setCounter(initialValue);
                realInvoiceNumberGenerator = repository.save(realInvoiceNumberGenerator);
            } else {
                realInvoiceNumberGenerator = repository.findFirstByOrderByIdAsc();
            }

            long counter = realInvoiceNumberGenerator.getCounter();
            realInvoiceNumberGenerator.setCounter(counter + 1);
            repository.save(realInvoiceNumberGenerator);
            return counter;
        }
    }

    public long getEpayInvoiceNumber() {
        //mihail: get the invoice number from the other table
        return getInvoiceNumber(epayInvoiceNumberGeneratorRepository, 8100000001L);
    }

    public long getRealInvoiceNumber() {
        return getInvoiceNumber(realInvoiceNumberGeneratorRepository, 8000000001L);
    }

    public long getProformaInvoiceNumber() {
        //mihail: get the invoice number from the other table
        return getInvoiceNumber(proformaNumberGeneratorRepository, 7000000001L);
    }

}
