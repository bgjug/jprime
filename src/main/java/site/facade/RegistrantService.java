package site.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import site.model.JprimeException;
import site.model.Registrant;
import site.model.Visitor;
import site.model.VisitorStatus;
import site.repository.RegistrantEpayInvoiceNumberGeneratorRepository;
import site.repository.RegistrantProformaInvoiceNumberGeneratorRepository;
import site.repository.RegistrantRealInvoiceNumberGeneratorRepository;
import site.repository.RegistrantRepository;

import javax.transaction.Transactional;

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

        if(registrant.getPaymentType().equals(Registrant.PaymentType.BANK_TRANSFER)) {
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

    private long getEpayInvoiceNumber() {
        //mihail: get the invoice number from the other table
        long count = registrantEpayInvoiceNumberGeneratorRepository.count();
        if(count > 1) {
            throw new JprimeException("Mihail: EpayInvoiceNumberGenerator table has more than one row. Fix that");
        }

        Registrant.EpayInvoiceNumberGenerator epayInvoiceNumberGenerator;
        if(count == 0) {
            epayInvoiceNumberGenerator = new Registrant.EpayInvoiceNumberGenerator();
            epayInvoiceNumberGenerator.setCounter(8100000001L);
            epayInvoiceNumberGenerator = registrantEpayInvoiceNumberGeneratorRepository.save(epayInvoiceNumberGenerator);
        } else {
            epayInvoiceNumberGenerator = registrantEpayInvoiceNumberGeneratorRepository.findFirstByOrderByIdAsc();
        }

        long counter = epayInvoiceNumberGenerator.getCounter();
        epayInvoiceNumberGenerator.setCounter(counter + 1);
        registrantEpayInvoiceNumberGeneratorRepository.save(epayInvoiceNumberGenerator);
        return counter;
    }

    private long getRealInvoiceNumber() {
        //mihail: get the invoice number from the other table
        long count = registrantRealInvoiceNumberGeneratorRepository.count();
        if(count > 1) {
            throw new JprimeException("Mihail: RealInvoiceNumberGenerator table has more than one row. Fix that");
        }

        Registrant.RealInvoiceNumberGenerator realInvoiceNumberGenerator;
        if(count == 0) {
            realInvoiceNumberGenerator = new Registrant.RealInvoiceNumberGenerator();
            realInvoiceNumberGenerator.setCounter(8000000001L);
            realInvoiceNumberGenerator = registrantRealInvoiceNumberGeneratorRepository.save(realInvoiceNumberGenerator);
        } else {
            realInvoiceNumberGenerator = registrantRealInvoiceNumberGeneratorRepository.findFirstByOrderByIdAsc();
        }

        long counter = realInvoiceNumberGenerator.getCounter();
        realInvoiceNumberGenerator.setCounter(counter + 1);
        registrantRealInvoiceNumberGeneratorRepository.save(realInvoiceNumberGenerator);
        return counter;
    }
    private long getProformaInvoiceNumber() {
        //mihail: get the invoice number from the other table
        long count = registrantProformaInvoiceNumberGeneratorRepository.count();
        if(count > 1) {
            throw new JprimeException("Mihail: ProformaInvoiceNumberGenerator table has more than one row. Fix that");
        }

        Registrant.ProformaInvoiceNumberGenerator proformaInvoiceNumberGenerator;
        if(count == 0) {
            proformaInvoiceNumberGenerator = new Registrant.ProformaInvoiceNumberGenerator();
            proformaInvoiceNumberGenerator.setCounter(7000000001L);
            proformaInvoiceNumberGenerator = registrantProformaInvoiceNumberGeneratorRepository.save(proformaInvoiceNumberGenerator);
        } else {
            proformaInvoiceNumberGenerator = registrantProformaInvoiceNumberGeneratorRepository.findFirstByOrderByIdAsc();
        }

        long counter = proformaInvoiceNumberGenerator.getCounter();
        proformaInvoiceNumberGenerator.setCounter(counter + 1);
        registrantProformaInvoiceNumberGeneratorRepository.save(proformaInvoiceNumberGenerator);
        return counter;
    }

    public void setRegistrantPaid(Registrant registrant) {
        registrant.getVisitors().forEach(visitor -> visitor.setStatus(VisitorStatus.PAYED));
        registrantRepository.save(registrant);
    }
}