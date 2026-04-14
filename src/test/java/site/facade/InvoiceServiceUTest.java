package site.facade;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import site.app.Application;
import site.controller.invoice.InvoiceData;
import site.model.Branch;
import site.model.Registrant;
import site.model.TicketPrice;
import site.model.TicketType;
import site.model.Visitor;
import site.repository.RegistrantRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Transactional
class InvoiceServiceUTest {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private BranchService branchService;

    @Autowired
    private RegistrantRepository registrantRepository;

    @BeforeAll
    static void setupBranches(@Autowired BranchService branchService) {
        DefaultBranchUtil.createDefaultBranch(branchService);
    }

    @Test
    void fromRegistrant_usesStudentPrice_forStudentRegistrant() {
        Branch branch = branchService.getCurrentBranch();
        BigDecimal studentPrice = branchService.getTicketPrices(branch).get(TicketType.STUDENT).getPrice();

        Registrant registrant = savedRegistrant(branch, true);

        InvoiceData invoiceData = invoiceService.fromRegistrant(registrant);

        assertThat(invoiceData.getInvoiceDetails()).hasSize(1);
        assertThat(invoiceData.getInvoiceDetails().get(0).getSinglePriceWithVAT())
            .isEqualByComparingTo(studentPrice);
    }

    @Test
    void fromRegistrant_usesRegularPrice_forNonEarlyBirdRegistrant() {
        // The current 2025 branch has cfpCloseDate in the past (Feb 2025), so any newly saved
        // registrant (createdDate = NOW) will not qualify for early bird.
        Branch branch = branchService.getCurrentBranch();
        BigDecimal regularPrice = branchService.getTicketPrices(branch).get(TicketType.REGULAR).getPrice();

        Registrant registrant = savedRegistrant(branch, false);

        InvoiceData invoiceData = invoiceService.fromRegistrant(registrant);

        assertThat(invoiceData.getInvoiceDetails()).hasSize(1);
        assertThat(invoiceData.getInvoiceDetails().get(0).getSinglePriceWithVAT())
            .isEqualByComparingTo(regularPrice);
    }

    @Test
    void fromRegistrant_usesEarlyBirdPrice_forEarlyBirdRegistrant() {
        // Create a branch whose cfpCloseDate is in the future so a newly persisted
        // registrant (createdDate = NOW) is within the early bird window.
        LocalDateTime futureClose = LocalDateTime.now().plusYears(1);
        Branch futureBranch = branchService.createBranch(
            new Branch(2099, LocalDateTime.now(), Duration.ofDays(2), LocalDateTime.now(), futureClose,
                Set.of(), false),
            List.of(new TicketPrice(BigDecimal.valueOf(500), TicketType.REGULAR),
                new TicketPrice(BigDecimal.valueOf(350), TicketType.EARLY_BIRD),
                new TicketPrice(BigDecimal.valueOf(150), TicketType.STUDENT)));

        BigDecimal earlyBirdPrice =
            branchService.getTicketPrices(futureBranch).get(TicketType.EARLY_BIRD).getPrice();

        Registrant registrant = savedRegistrant(futureBranch, false);

        InvoiceData invoiceData = invoiceService.fromRegistrant(registrant);

        assertThat(invoiceData.getInvoiceDetails()).hasSize(1);
        assertThat(invoiceData.getInvoiceDetails().get(0).getSinglePriceWithVAT())
            .isEqualByComparingTo(earlyBirdPrice);
    }

    private Registrant savedRegistrant(Branch branch, boolean student) {
        Registrant r = new Registrant();
        r.setName("Test Registrant");
        r.setEmail("test@test.com");
        r.setBranch(branch);
        r.setStudent(student);
        Visitor v = new Visitor();
        v.setName("Test Visitor");
        v.setEmail("visitor@test.com");
        v.setRegistrant(r);
        r.getVisitors().add(v);
        return registrantRepository.save(r);
    }
}
