package site.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import site.model.JprimeException;
import site.model.Registrant;
import site.model.Visitor;
import site.model.VisitorStatus;
import site.repository.RegistrantEpayInvoiceNumberGeneratorRepository;
import site.repository.RegistrantNumberGeneratorRepository;
import site.repository.RegistrantProformaInvoiceNumberGeneratorRepository;
import site.repository.RegistrantRealInvoiceNumberGeneratorRepository;
import site.repository.RegistrantRepository;

import javax.transaction.Transactional;
import java.util.function.Supplier;

@Service(RegistrantService.NAME)
@Transactional
public class RegistrantService {

	public static final String NAME = "registrantFacade";

    @Autowired
    @Qualifier(RegistrantRepository.NAME)
    private RegistrantRepository registrantRepository;
    @Autowired
    @Qualifier(RegistrantEpayInvoiceNumberGeneratorRepository.NAME)
    private RegistrantEpayInvoiceNumberGeneratorRepository registrantEpayInvoiceNumberGeneratorRepository;
    @Autowired
    @Qualifier(RegistrantRealInvoiceNumberGeneratorRepository.NAME)
    private RegistrantRealInvoiceNumberGeneratorRepository registrantRealInvoiceNumberGeneratorRepository;
    @Autowired
    @Qualifier(RegistrantProformaInvoiceNumberGeneratorRepository.NAME)
    private RegistrantProformaInvoiceNumberGeneratorRepository registrantProformaInvoiceNumberGeneratorRepository;

    public Registrant findByEpayInvoiceNumber(long invoiceNumber){
        return registrantRepository.findByEpayInvoiceNumber(invoiceNumber);
    }

    public Registrant findById(long id) {
        return registrantRepository.findById(id).get();
    }

    /** Complicated */
    public synchronized Registrant save(Registrant registrant) {

        if(registrant.getPaymentType() == Registrant.PaymentType.BANK_TRANSFER) {
            long counter = getProformaInvoiceNumber();
            registrant.setProformaInvoiceNumber(counter);
        } else {
            if(registrant.getEpayInvoiceNumber()==0) {//only if new registrant
                long counter = getEpayInvoiceNumber();
                registrant.setEpayInvoiceNumber(counter);
            }

            //only if registrant has the epay info. now a real invoice number must be produced
            if(registrant.getEpayResponse() != null && registrant.getRealInvoiceNumber() == 0) {
                generateInvoiceNumber(registrant);
            }
        }
        //todo: mihail this is not optimal, but for now it works
        for(Visitor visitor:registrant.getVisitors()) {
            visitor.setRegistrant(registrant);
        }

        return registrantRepository.save(registrant);
    }

    public void generateInvoiceNumber(Registrant registrant) {
        long counter = getRealInvoiceNumber();
        registrant.setRealInvoiceNumber(counter);
    }

    private <T extends Registrant.NumberGenerator> long getInvoiceNumber(RegistrantNumberGeneratorRepository<T> repository, long initialValue, Supplier<T> newInstance) {
        synchronized (repository.lockObject()) {
            //mihail: get the invoice number from the other table
            long count = repository.count();
            if(count > 1) {
                throw new JprimeException("Mihail: RealInvoiceNumberGenerator table has more than one row. Fix that");
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

    private long getEpayInvoiceNumber() {
        //mihail: get the invoice number from the other table
        return getInvoiceNumber(registrantEpayInvoiceNumberGeneratorRepository, 8100000001L,
            Registrant.EpayInvoiceNumberGenerator::new);
    }

    private long getRealInvoiceNumber() {
        return getInvoiceNumber(registrantRealInvoiceNumberGeneratorRepository, 8000000001L,
            Registrant.RealInvoiceNumberGenerator::new);
    }
    private long getProformaInvoiceNumber() {
        //mihail: get the invoice number from the other table
        return getInvoiceNumber(registrantProformaInvoiceNumberGeneratorRepository, 7000000001L,
            Registrant.ProformaInvoiceNumberGenerator::new);
    }

    public void setRegistrantPaid(Registrant registrant) {
        registrant.getVisitors().forEach(visitor -> visitor.setStatus(VisitorStatus.PAYED));
        registrantRepository.save(registrant);
    }
}