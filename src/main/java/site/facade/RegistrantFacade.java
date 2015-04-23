package site.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import site.model.JprimeException;
import site.model.Registrant;
import site.model.Visitor;
import site.repository.RegistrantEpayInvoiceNumberGeneratorRepository;
import site.repository.RegistrantRealInvoiceNumberGeneratorRepository;
import site.repository.RegistrantRepository;

import javax.transaction.Transactional;

@Service(RegistrantFacade.NAME)
@Transactional
public class RegistrantFacade {

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

    public Registrant findByEpayInvoiceNumber(long invoiceNumber){
        return registrantRepository.findByEpayInvoiceNumber(invoiceNumber);
    }


    public synchronized Registrant save(Registrant registrant) {
        if(registrant.getEpayInvoiceNumber()==0) {//only if new registrant
            long counter = getEpayInvoiceNumber();
            registrant.setEpayInvoiceNumber(counter);
        }

        //only if registrant has the epay info. now a real invoice number must be produced
        if(registrant.getEpayResponse() != null && registrant.getRealInvoiceNumber() == 0) {
            long counter = getRealInvoiceNumber();
            registrant.setRealInvoiceNumber(counter);
        }

        //todo: mihail this is not optimal, but for now it works
        for(Visitor visitor:registrant.getVisitors()) {
            visitor.setRegistrant(registrant);
        }

        return registrantRepository.save(registrant);
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
            epayInvoiceNumberGenerator.setCounter(1000001);
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
            realInvoiceNumberGenerator.setCounter(81000001);
            realInvoiceNumberGenerator = registrantRealInvoiceNumberGeneratorRepository.save(realInvoiceNumberGenerator);
        } else {
            realInvoiceNumberGenerator = registrantRealInvoiceNumberGeneratorRepository.findFirstByOrderByIdAsc();
        }

        long counter = realInvoiceNumberGenerator.getCounter();
        realInvoiceNumberGenerator.setCounter(counter + 1);
        registrantRealInvoiceNumberGeneratorRepository.save(realInvoiceNumberGenerator);
        return counter;
    }
}
