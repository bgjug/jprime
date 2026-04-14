package site.facade;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import site.app.Application;
import site.model.Branch;
import site.model.Registrant;
import site.model.Speaker;
import site.model.Visitor;
import site.repository.RegistrantRepository;
import site.repository.SpeakerRepository;
import site.repository.VisitorRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Transactional
class AdminServiceUTest {

    @Autowired
    private AdminService adminService;

    @Autowired
    private BranchService branchService;

    @Autowired
    private SpeakerRepository speakerRepository;

    @Autowired
    private RegistrantRepository registrantRepository;

    @Autowired
    private VisitorRepository visitorRepository;

    private Branch currentBranch;

    @BeforeAll
    static void setupBranches(@Autowired BranchService branchService) {
        DefaultBranchUtil.createDefaultBranch(branchService);
    }

    @BeforeEach
    void setup() {
        currentBranch = branchService.getCurrentBranch();
    }

    @Test
    void saveSpeaker_usesExistingId_whenSpeakerWithEmailExists() {
        Speaker existing = speakerRepository.save(new Speaker("Old", "Name", "dup@test.com", "h", "t"));

        Speaker incoming = new Speaker("New", "Name", "dup@test.com", "h2", "t2");
        Speaker saved = adminService.saveSpeaker(incoming);

        assertThat(saved.getId()).isEqualTo(existing.getId());
        assertThat(saved.getFirstName()).isEqualTo("New");
    }

    @Test
    void saveSpeaker_savesNewSpeaker_whenEmailNotExists() {
        Speaker speaker = new Speaker("Brand", "New", "brand.new@test.com", "h", "t");

        Speaker saved = adminService.saveSpeaker(speaker);

        assertThat(saved.getId()).isNotNull();
        assertThat(speakerRepository.findByEmail("brand.new@test.com")).isNotNull();
    }

    @Test
    void deleteVisitor_deletesRegistrant_whenRegistrantHasOnlyThisVisitor() {
        Registrant registrant = new Registrant();
        registrant.setName("Solo Visitor");
        registrant.setEmail("solo@test.com");
        registrant.setBranch(currentBranch);
        registrantRepository.save(registrant);

        Visitor visitor = new Visitor();
        visitor.setName("Solo Visitor");
        visitor.setEmail("solo@test.com");
        visitor.setRegistrant(registrant);
        visitorRepository.save(visitor);
        registrant.getVisitors().add(visitor);

        adminService.deleteVisitor(visitor.getId());

        assertThat(visitorRepository.findById(visitor.getId())).isEmpty();
        assertThat(registrantRepository.findById(registrant.getId())).isEmpty();
    }

    @Test
    void deleteVisitor_keepsRegistrant_whenRegistrantHasMoreVisitors() {
        Registrant registrant = new Registrant();
        registrant.setName("Company Inc");
        registrant.setEmail("company@test.com");
        registrant.setBranch(currentBranch);
        registrantRepository.save(registrant);

        Visitor first = new Visitor();
        first.setName("First Person");
        first.setEmail("first@test.com");
        first.setRegistrant(registrant);
        visitorRepository.save(first);
        registrant.getVisitors().add(first);

        Visitor second = new Visitor();
        second.setName("Second Person");
        second.setEmail("second@test.com");
        second.setRegistrant(registrant);
        visitorRepository.save(second);
        registrant.getVisitors().add(second);

        adminService.deleteVisitor(first.getId());

        assertThat(visitorRepository.findById(first.getId())).isEmpty();
        assertThat(registrantRepository.findById(registrant.getId())).isPresent();
    }
}
