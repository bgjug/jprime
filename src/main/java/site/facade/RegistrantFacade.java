package site.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import site.model.*;
import site.repository.*;

import javax.transaction.Transactional;

@Service(RegistrantFacade.NAME)
@Transactional
public class RegistrantFacade {

	public static final String NAME = "registrantFacade";

    @Autowired
    @Qualifier(RegistrantRepository.NAME)
    private RegistrantRepository registrantRepository;
    @Autowired
    @Qualifier(RegistrantInvoiceNumberGeneratorRepository.NAME)
    private RegistrantInvoiceNumberGeneratorRepository registrantInvoiceNumberGeneratorRepository;

    public Registrant findByInvoiceNumber(long invoiceNumber){
        return registrantRepository.findByInvoiceNumber(invoiceNumber);
    }


    public synchronized Registrant save(Registrant registrant) {
        long counter = getInvoiceNumber();
        registrant.setInvoiceNumber(counter);

        //todo: mihail this is not optimal, but for now it works
        for(Visitor visitor:registrant.getVisitors()) {
            visitor.setRegistrant(registrant);
        }

        return registrantRepository.save(registrant);
    }

    private long getInvoiceNumber() {
        //mihail: get the invoice number from the other table
        long count = registrantInvoiceNumberGeneratorRepository.count();
        if(count > 1) {
            throw new JprimeException("Mihail: InvoiceNumberGenerator table has more than one row. Fix that");
        }

        Registrant.InvoiceNumberGenerator generator;
        if(count == 0) {
            generator = new Registrant.InvoiceNumberGenerator();
            generator.setCounter(1000001);
            generator = registrantInvoiceNumberGeneratorRepository.save(generator);
        } else {
            generator = registrantInvoiceNumberGeneratorRepository.findFirstByOrderByIdAsc();
        }

        long counter = generator.getCounter();
        generator.setCounter(counter+1);
        registrantInvoiceNumberGeneratorRepository.save(generator);
        return counter;
    }
}
