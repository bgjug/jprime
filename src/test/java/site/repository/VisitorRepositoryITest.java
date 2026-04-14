package site.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import site.app.Application;
import site.facade.BranchService;
import site.facade.DefaultBranchUtil;
import site.model.Branch;
import site.model.Registrant;
import site.model.Visitor;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for VisitorRepository custom queries.
 * Tests ticket and branch query functionality.
 */
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Transactional
class VisitorRepositoryITest {

    @Autowired
    private VisitorRepository visitorRepository;

    @Autowired
    private RegistrantRepository registrantRepository;

    @Autowired
    private BranchService branchService;

    private Branch currentBranch;
    private Branch otherBranch;
    private Visitor visitorWithTicket;
    private Visitor visitorWithoutTicket;
    private Visitor otherBranchVisitor;
    private Registrant registrantCurrent;
    private Registrant registrantOther;

    @BeforeAll
    public static void beforeAll(@Autowired BranchService branchService) {
        DefaultBranchUtil.createDefaultBranch(branchService);
    }

    @BeforeEach
    void setUp() {
        visitorRepository.deleteAll();
        registrantRepository.deleteAll();

        currentBranch = branchService.getCurrentBranch();
        try {
            otherBranch = branchService.findBranchByYear(2016);
        } catch (IllegalArgumentException e) {
            otherBranch = branchService.createBranch(2016, LocalDateTime.of(2016, 5, 24, 9, 0));
        }

        // Registrant for current branch
        registrantCurrent = new Registrant("John Doe", "john@example.com", currentBranch);
        registrantCurrent = registrantRepository.save(registrantCurrent);

        // Visitor with ticket in current branch
        visitorWithTicket = new Visitor(registrantCurrent, "Jane Smith", "jane@example.com", "ACME Corp");
        visitorWithTicket.setTicket("TICKET-001");
        visitorWithTicket = visitorRepository.save(visitorWithTicket);

        // Visitor without ticket in current branch
        visitorWithoutTicket = new Visitor(registrantCurrent, "Bob Johnson", "bob@example.com", "Tech Inc");
        visitorWithoutTicket = visitorRepository.save(visitorWithoutTicket);

        // Registrant for other branch
        registrantOther = new Registrant("Alice Williams", "alice@example.com", otherBranch);
        registrantOther = registrantRepository.save(registrantOther);

        // Visitor in other branch
        otherBranchVisitor = new Visitor(registrantOther, "Charlie Brown", "charlie@example.com", "Dev Corp");
        otherBranchVisitor.setTicket("TICKET-002");
        otherBranchVisitor = visitorRepository.save(otherBranchVisitor);
    }

    @Test
    void findAllNewestUsers_shouldReturnVisitorsForBranchOrderedByCreatedDate() {
        List<Visitor> visitors = visitorRepository.findAllNewestUsers(currentBranch);

        assertThat(visitors).hasSize(2);
        assertThat(visitors).containsExactlyInAnyOrder(visitorWithTicket, visitorWithoutTicket);
        assertThat(visitors).doesNotContain(otherBranchVisitor);
        
        // Verify ordering by created date descending
        if (visitors.size() > 1) {
            assertThat(visitors.get(0).getCreatedDate())
                .isAfterOrEqualTo(visitors.get(1).getCreatedDate());
        }
    }

    @Test
    void findAllWithTicket_shouldReturnOnlyVisitorsWithTicketsForBranch() {
        List<Visitor> visitorsWithTickets = visitorRepository.findAllWithTicket(currentBranch);

        assertThat(visitorsWithTickets).hasSize(1);
        assertThat(visitorsWithTickets).containsExactly(visitorWithTicket);
        assertThat(visitorsWithTickets).doesNotContain(visitorWithoutTicket, otherBranchVisitor);
    }

    @Test
    void findByTicket_shouldReturnVisitorWithGivenTicket() {
        Optional<Visitor> found = visitorRepository.findByTicket("TICKET-001");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Jane Smith");
    }

    @Test
    void findByTicket_shouldReturnEmptyForNonExistentTicket() {
        Optional<Visitor> found = visitorRepository.findByTicket("NON-EXISTENT");

        assertThat(found).isEmpty();
    }

    @Test
    void findByNameIgnoreCase_shouldReturnVisitorsWithMatchingName() {
        List<Visitor> visitors = visitorRepository.findByNameIgnoreCase("jane smith");

        assertThat(visitors).hasSize(1);
        assertThat(visitors.get(0).getName()).isEqualTo("Jane Smith");
    }

    @Test
    void findByNameIgnoreCaseAndCompanyIgnoreCase_shouldReturnVisitorsWithMatchingNameAndCompany() {
        List<Visitor> visitors = visitorRepository.findByNameIgnoreCaseAndCompanyIgnoreCase(
            "jane smith", "acme corp"
        );

        assertThat(visitors).hasSize(1);
        assertThat(visitors.get(0).getName()).isEqualTo("Jane Smith");
        assertThat(visitors.get(0).getCompany()).isEqualTo("ACME Corp");
    }

    @Test
    void findByNameIgnoreCaseAndCompanyIgnoreCase_shouldReturnEmptyForNonMatchingCompany() {
        List<Visitor> visitors = visitorRepository.findByNameIgnoreCaseAndCompanyIgnoreCase(
            "jane smith", "wrong company"
        );

        assertThat(visitors).isEmpty();
    }
}
