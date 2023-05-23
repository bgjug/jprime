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
    private NumberGeneratorService numberGeneratorService;

    public Registrant findByEpayInvoiceNumber(long invoiceNumber){
        return registrantRepository.findByEpayInvoiceNumber(invoiceNumber);
    }

    public Registrant findById(long id) {
        return registrantRepository.findById(id).get();
    }

    /** Complicated */
    public synchronized Registrant save(Registrant registrant) {

        if(registrant.getPaymentType() == Registrant.PaymentType.BANK_TRANSFER) {
            long counter = numberGeneratorService.getProformaInvoiceNumber();
            registrant.setProformaInvoiceNumber(counter);
        } else {
            if(registrant.getEpayInvoiceNumber()==0) {//only if new registrant
                long counter = numberGeneratorService.getEpayInvoiceNumber();
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
        long counter = numberGeneratorService.getRealInvoiceNumber();
        registrant.setRealInvoiceNumber(counter);
    }

    public void setRegistrantPaid(Registrant registrant) {
        registrant.getVisitors().forEach(visitor -> visitor.setStatus(VisitorStatus.PAYED));
        registrantRepository.save(registrant);
    }
}