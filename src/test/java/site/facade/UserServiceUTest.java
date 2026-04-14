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
import site.model.SessionLevel;
import site.model.SessionType;
import site.model.Speaker;
import site.model.Submission;
import site.model.SubmissionStatus;
import site.model.Visitor;
import site.repository.RegistrantRepository;
import site.repository.SpeakerRepository;
import site.repository.SubmissionRepository;
import site.repository.VisitorRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Transactional
class UserServiceUTest {

    @Autowired
    private UserService userService;

    @Autowired
    private BranchService branchService;

    @Autowired
    private VisitorRepository visitorRepository;

    @Autowired
    private RegistrantRepository registrantRepository;

    @Autowired
    private SpeakerRepository speakerRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    private Visitor savedVisitor;

    @BeforeAll
    static void setupBranches(@Autowired BranchService branchService) {
        DefaultBranchUtil.createDefaultBranch(branchService);
    }

    @BeforeEach
    void setupVisitor() {
        Branch branch = branchService.getCurrentBranch();
        Registrant registrant = new Registrant();
        registrant.setName("Test Company");
        registrant.setEmail("company@test.com");
        registrant.setBranch(branch);
        registrantRepository.save(registrant);

        Visitor visitor = new Visitor();
        visitor.setName("John Doe");
        visitor.setEmail("john@test.com");
        visitor.setCompany("Test Co");
        visitor.setRegistrant(registrant);
        savedVisitor = visitorRepository.save(visitor);
    }

    @Test
    void setPresentById_returnsTrue_whenVisitorExists() {
        Visitor example = new Visitor();
        example.setId(savedVisitor.getId());

        boolean result = userService.setPresentById(example);

        assertThat(result).isTrue();
        assertThat(visitorRepository.findById(savedVisitor.getId()).orElseThrow().isPresent()).isTrue();
    }

    @Test
    void setPresentById_returnsFalse_whenVisitorNotFound() {
        Visitor example = new Visitor();
        example.setId(-999L);

        boolean result = userService.setPresentById(example);

        assertThat(result).isFalse();
    }

    @Test
    void setPresentByNameIgnoreCase_returnsTrue_whenMatchFound() {
        Visitor example = new Visitor();
        example.setName("JOHN DOE");

        boolean result = userService.setPresentByNameIgnoreCase(example);

        assertThat(result).isTrue();
        assertThat(visitorRepository.findByNameIgnoreCase("John Doe").get(0).isPresent()).isTrue();
    }

    @Test
    void setPresentByNameIgnoreCase_returnsFalse_whenNoMatch() {
        Visitor example = new Visitor();
        example.setName("Nobody Here");

        boolean result = userService.setPresentByNameIgnoreCase(example);

        assertThat(result).isFalse();
    }

    @Test
    void setPresentByNameIgnoreCaseAndCompanyIgnoreCase_returnsTrue_whenBothMatch() {
        Visitor example = new Visitor();
        example.setName("john doe");
        example.setCompany("TEST CO");

        boolean result = userService.setPresentByNameIgnoreCaseAndCompanyIgnoreCase(example);

        assertThat(result).isTrue();
        assertThat(
            visitorRepository.findByNameIgnoreCaseAndCompanyIgnoreCase("John Doe", "Test Co").get(0)
                .isPresent()).isTrue();
    }

    @Test
    void setPresentByNameIgnoreCaseAndCompanyIgnoreCase_returnsFalse_whenNoMatch() {
        Visitor example = new Visitor();
        example.setName("John Doe");
        example.setCompany("Wrong Company");

        boolean result = userService.setPresentByNameIgnoreCaseAndCompanyIgnoreCase(example);

        assertThat(result).isFalse();
    }

    @Test
    void isSpeakerConfirmed_returnsTrue_forConfirmedSpeaker() {
        Branch branch = branchService.getCurrentBranch();
        Speaker speaker = new Speaker("Jane", "Smith", "jane@test.com", "Headline", "twitter");
        Submission submission =
            new Submission("Talk", "Desc", SessionLevel.BEGINNER, SessionType.CONFERENCE_SESSION, speaker,
                SubmissionStatus.CONFIRMED, false).branch(branch);
        submissionRepository.save(submission);
        speakerRepository.save(speaker);

        boolean result = userService.isSpeakerConfirmed(speaker);

        assertThat(result).isTrue();
    }

    @Test
    void isSpeakerConfirmed_returnsFalse_forNonConfirmedSpeaker() {
        Branch branch = branchService.getCurrentBranch();
        Speaker speaker = new Speaker("Bob", "Jones", "bob@test.com", "Headline", "twitter");
        Submission submission =
            new Submission("Talk", "Desc", SessionLevel.BEGINNER, SessionType.CONFERENCE_SESSION, speaker,
                SubmissionStatus.SUBMITTED, false).branch(branch);
        submissionRepository.save(submission);
        speakerRepository.save(speaker);

        boolean result = userService.isSpeakerConfirmed(speaker);

        assertThat(result).isFalse();
    }
}
