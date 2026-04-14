package site.repository;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import site.app.Application;
import site.facade.BranchService;
import site.facade.DefaultBranchUtil;
import site.model.Branch;
import site.model.Registrant;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for RegistrantRepository custom queries.
 * Tests branch query functionality.
 */
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Transactional
class RegistrantRepositoryITest {

    @Autowired
    private RegistrantRepository registrantRepository;

    @Autowired
    private BranchService branchService;

    private Branch currentBranch;
    private Branch otherBranch;
    private Registrant registrant1Current;
    private Registrant registrant2Current;
    private Registrant registrantOther;

    @BeforeAll
    public static void beforeAll(@Autowired BranchService branchService) {
        DefaultBranchUtil.createDefaultBranch(branchService);
    }

    @BeforeEach
    void setUp() {
        registrantRepository.deleteAll();

        currentBranch = branchService.getCurrentBranch();
        try {
            otherBranch = branchService.findBranchByYear(2016);
        } catch (IllegalArgumentException e) {
            otherBranch = branchService.createBranch(2016, LocalDateTime.of(2016, 5, 24, 9, 0));
        }

        // Registrant 1 for current branch
        registrant1Current = new Registrant("John Doe", "john@example.com", currentBranch);
        registrant1Current.setEpayInvoiceNumber(12345L);
        registrant1Current = registrantRepository.save(registrant1Current);

        // Registrant 2 for current branch
        registrant2Current = new Registrant("Jane Smith", "jane@example.com", currentBranch);
        registrant2Current.setEpayInvoiceNumber(67890L);
        registrant2Current = registrantRepository.save(registrant2Current);

        // Registrant for other branch
        registrantOther = new Registrant("Bob Johnson", "bob@example.com", otherBranch);
        registrantOther.setEpayInvoiceNumber(11111L);
        registrantOther = registrantRepository.save(registrantOther);
    }

    @Test
    void findAllByBranchPageable_shouldReturnRegistrantsForGivenBranch() {
        Page<Registrant> currentBranchRegistrants = 
            registrantRepository.findAllByBranch(PageRequest.of(0, 10), currentBranch);

        assertThat(currentBranchRegistrants.getContent()).hasSize(2);
        assertThat(currentBranchRegistrants.getContent())
            .containsExactlyInAnyOrder(registrant1Current, registrant2Current);
        assertThat(currentBranchRegistrants.getContent()).doesNotContain(registrantOther);
    }

    @Test
    void findAllByBranchPageable_shouldSupportPagination() {
        Page<Registrant> firstPage = 
            registrantRepository.findAllByBranch(PageRequest.of(0, 1), currentBranch);

        assertThat(firstPage.getContent()).hasSize(1);
        assertThat(firstPage.getTotalElements()).isEqualTo(2);
        assertThat(firstPage.getTotalPages()).isEqualTo(2);
    }

    @Test
    void findAllByBranchIterable_shouldReturnAllRegistrantsForBranch() {
        Iterable<Registrant> currentBranchRegistrants = 
            registrantRepository.findAllByBranch(currentBranch);

        assertThat(currentBranchRegistrants).hasSize(2);
        assertThat(currentBranchRegistrants)
            .containsExactlyInAnyOrder(registrant1Current, registrant2Current);
    }

    @Test
    void findByEpayInvoiceNumber_shouldReturnRegistrantWithGivenInvoiceNumber() {
        Registrant found = registrantRepository.findByEpayInvoiceNumber(12345L);

        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("John Doe");
        assertThat(found.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void findByEpayInvoiceNumber_shouldReturnNullForNonExistentInvoiceNumber() {
        Registrant found = registrantRepository.findByEpayInvoiceNumber(99999L);

        assertThat(found).isNull();
    }

    @Test
    void findAllByBranchPageable_shouldReturnEmptyForBranchWithNoRegistrants() {
        Branch emptyBranch = branchService.createBranch(2015, LocalDateTime.of(2015, 5, 26, 9, 0));

        Page<Registrant> emptyPage =
            registrantRepository.findAllByBranch(PageRequest.of(0, 10), emptyBranch);

        assertThat(emptyPage.getContent()).isEmpty();
    }

    @Test
    void findAllByBranchIterable_shouldReturnEmptyForBranchWithNoRegistrants() {
        Branch emptyBranch = branchService.createBranch(2014, LocalDateTime.of(2014, 5, 27, 9, 0));

        Iterable<Registrant> empty = registrantRepository.findAllByBranch(emptyBranch);

        assertThat(empty).isEmpty();
    }
}
