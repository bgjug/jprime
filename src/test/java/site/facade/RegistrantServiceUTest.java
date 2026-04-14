package site.facade;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import site.app.Application;
import site.controller.epay.EpayResponse;
import site.model.Branch;
import site.model.Registrant;
import site.model.Visitor;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Transactional
class RegistrantServiceUTest {

    @Autowired
    private RegistrantService registrantService;

    @Autowired
    private BranchService branchService;

    @BeforeAll
    static void setupBranches(@Autowired BranchService branchService) {
        DefaultBranchUtil.createDefaultBranch(branchService);
    }

    @Test
    void save_generatesProformaNumber_forBankTransferRegistrant() {
        Registrant registrant = newRegistrant(Registrant.PaymentType.BANK_TRANSFER);

        Registrant saved = registrantService.save(registrant);

        assertThat(saved.getProformaInvoiceNumber()).isGreaterThan(0L);
        assertThat(saved.getEpayInvoiceNumber()).isEqualTo(0L);
        assertThat(saved.getRealInvoiceNumber()).isEqualTo(0L);
    }

    @Test
    void save_generatesEpayNumber_forNewEpayRegistrant() {
        Registrant registrant = newRegistrant(Registrant.PaymentType.EPAY_ACCOUNT);

        Registrant saved = registrantService.save(registrant);

        assertThat(saved.getEpayInvoiceNumber()).isGreaterThan(0L);
        assertThat(saved.getProformaInvoiceNumber()).isEqualTo(0L);
        assertThat(saved.getRealInvoiceNumber()).isEqualTo(0L);
    }

    @Test
    void save_generatesRealInvoiceNumber_whenEpayResponseExistsAndNoRealInvoice() {
        Registrant registrant = newRegistrant(Registrant.PaymentType.EPAY_ACCOUNT);
        registrant.setEpayInvoiceNumber(9999L);
        registrant.setEpayResponse(new EpayResponse());

        Registrant saved = registrantService.save(registrant);

        assertThat(saved.getRealInvoiceNumber()).isGreaterThan(0L);
    }

    private Registrant newRegistrant(Registrant.PaymentType paymentType) {
        Branch branch = branchService.getCurrentBranch();
        Registrant r = new Registrant();
        r.setName("Test Company");
        r.setEmail("test@company.com");
        r.setBranch(branch);
        r.setPaymentType(paymentType);
        Visitor v = new Visitor();
        v.setName("Test Visitor");
        v.setEmail("visitor@company.com");
        v.setRegistrant(r);
        r.getVisitors().add(v);
        return r;
    }
}
