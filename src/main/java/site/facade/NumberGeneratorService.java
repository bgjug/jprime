package site.facade;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import site.model.JprimeException;
import site.model.Registrant;
import site.repository.RegistrantEpayInvoiceNumberGeneratorRepository;
import site.repository.RegistrantNumberGeneratorRepository;
import site.repository.RegistrantProformaInvoiceNumberGeneratorRepository;
import site.repository.RegistrantRealInvoiceNumberGeneratorRepository;

@Service
public class NumberGeneratorService {

    private final RegistrantEpayInvoiceNumberGeneratorRepository
        registrantEpayInvoiceNumberGeneratorRepository;

    private final RegistrantRealInvoiceNumberGeneratorRepository
        registrantRealInvoiceNumberGeneratorRepository;

    private final RegistrantProformaInvoiceNumberGeneratorRepository
        registrantProformaInvoiceNumberGeneratorRepository;

    public NumberGeneratorService(@Qualifier(
        RegistrantEpayInvoiceNumberGeneratorRepository.NAME) RegistrantEpayInvoiceNumberGeneratorRepository registrantEpayInvoiceNumberGeneratorRepository,
        @Qualifier(
            RegistrantRealInvoiceNumberGeneratorRepository.NAME) RegistrantRealInvoiceNumberGeneratorRepository registrantRealInvoiceNumberGeneratorRepository,
        @Qualifier(
            RegistrantProformaInvoiceNumberGeneratorRepository.NAME) RegistrantProformaInvoiceNumberGeneratorRepository registrantProformaInvoiceNumberGeneratorRepository) {
        this.registrantEpayInvoiceNumberGeneratorRepository = registrantEpayInvoiceNumberGeneratorRepository;
        this.registrantRealInvoiceNumberGeneratorRepository = registrantRealInvoiceNumberGeneratorRepository;
        this.registrantProformaInvoiceNumberGeneratorRepository =
            registrantProformaInvoiceNumberGeneratorRepository;
    }

    private <T extends Registrant.NumberGenerator> long getInvoiceNumber(
        RegistrantNumberGeneratorRepository<T> repository, long initialValue, Supplier<T> newInstance) {
        //mihail: get the invoice number from the other table
        synchronized (repository.repositoryClass()) {
            long count = repository.count();
            if (count > 1) {
                throw new JprimeException(
                    "Mihail: RealInvoiceNumberGenerator table has more than one row. Fix that");
            }

            T realInvoiceNumberGenerator;
            if (count == 0) {
                realInvoiceNumberGenerator = newInstance.get();
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
        return getInvoiceNumber(registrantEpayInvoiceNumberGeneratorRepository, 8100000001L,
            Registrant.EpayInvoiceNumberGenerator::new);
    }

    public long getRealInvoiceNumber() {
        return getInvoiceNumber(registrantRealInvoiceNumberGeneratorRepository, 8000000001L,
            Registrant.RealInvoiceNumberGenerator::new);
    }

    public long getProformaInvoiceNumber() {
        //mihail: get the invoice number from the other table
        return getInvoiceNumber(registrantProformaInvoiceNumberGeneratorRepository, 7000000001L,
            Registrant.ProformaInvoiceNumberGenerator::new);
    }

}
