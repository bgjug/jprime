package site.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
import site.model.SessionLevel;
import site.model.SessionType;
import site.model.Speaker;
import site.model.Submission;
import site.model.SubmissionStatus;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for SpeakerRepository custom queries.
 * Tests featured/confirmed speaker joins and branch filtering.
 */
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Transactional
class SpeakerRepositoryITest {

    @Autowired
    private SpeakerRepository speakerRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private BranchService branchService;

    private Branch currentBranch;
    private Branch otherBranch;
    private Speaker featuredSpeaker;
    private Speaker confirmedSpeaker;
    private Speaker submittedSpeaker;
    private Speaker coSpeaker;

    @BeforeAll
    public static void beforeAll(@Autowired BranchService branchService) {
        DefaultBranchUtil.createDefaultBranch(branchService);
    }

    @BeforeEach
    void setUp() {
        submissionRepository.deleteAll();
        speakerRepository.deleteAll();

        currentBranch = branchService.getCurrentBranch();
        try {
            otherBranch = branchService.findBranchByYear(2016);
        } catch (IllegalArgumentException e) {
            otherBranch = branchService.createBranch(2016, LocalDateTime.of(2016, 5, 24, 9, 0));
        }

        // Speaker with featured submission
        featuredSpeaker = new Speaker("Jane", "Doe", "jane@example.com", "Featured Speaker", "@janedoe");
        featuredSpeaker = speakerRepository.save(featuredSpeaker);
        
        Submission featuredSubmission = new Submission(
            "Featured Talk", "Amazing content", SessionLevel.ADVANCED,
            SessionType.CONFERENCE_SESSION, featuredSpeaker, SubmissionStatus.SUBMITTED, true
        );
        featuredSubmission.branch(currentBranch);
        featuredSubmission = submissionRepository.save(featuredSubmission);
        featuredSpeaker.getSubmissions().add(featuredSubmission);
        speakerRepository.save(featuredSpeaker);

        // Speaker with confirmed submission
        confirmedSpeaker = new Speaker("John", "Smith", "john@example.com", "Confirmed Speaker", "@johnsmith");
        confirmedSpeaker = speakerRepository.save(confirmedSpeaker);
        
        Submission confirmedSubmission = new Submission(
            "Confirmed Talk", "Great content", SessionLevel.INTERMEDIATE,
            SessionType.CONFERENCE_SESSION, confirmedSpeaker, SubmissionStatus.CONFIRMED, false
        );
        confirmedSubmission.branch(currentBranch);
        confirmedSubmission = submissionRepository.save(confirmedSubmission);
        confirmedSpeaker.getSubmissions().add(confirmedSubmission);
        speakerRepository.save(confirmedSpeaker);

        // Speaker with only submitted (not confirmed) submission
        submittedSpeaker = new Speaker("Bob", "Johnson", "bob@example.com", "Submitted Speaker", "@bobjohnson");
        submittedSpeaker = speakerRepository.save(submittedSpeaker);
        
        Submission submittedSubmission = new Submission(
            "Submitted Talk", "Good content", SessionLevel.BEGINNER,
            SessionType.CONFERENCE_SESSION, submittedSpeaker, SubmissionStatus.SUBMITTED, false
        );
        submittedSubmission.branch(currentBranch);
        submittedSubmission = submissionRepository.save(submittedSubmission);
        submittedSpeaker.getSubmissions().add(submittedSubmission);
        speakerRepository.save(submittedSpeaker);

        // Co-speaker with confirmed submission
        coSpeaker = new Speaker("Alice", "Williams", "alice@example.com", "Co-Speaker", "@alicew");
        coSpeaker = speakerRepository.save(coSpeaker);
        
        Submission coSpeakerSubmission = new Submission(
            "Co-Speaker Talk", "Collaborative content", SessionLevel.INTERMEDIATE,
            SessionType.CONFERENCE_SESSION, confirmedSpeaker, coSpeaker, SubmissionStatus.CONFIRMED, false
        );
        coSpeakerSubmission.branch(currentBranch);
        coSpeakerSubmission = submissionRepository.save(coSpeakerSubmission);
        coSpeaker.getCoSpeakerSubmissions().add(coSpeakerSubmission);
        speakerRepository.save(coSpeaker);
    }

    @Test
    void findFeaturedSpeakers_shouldReturnOnlyFeaturedSpeakers() {
        List<Speaker> featured = speakerRepository.findFeaturedSpeakers(currentBranch);

        assertThat(featured).hasSize(1);
        assertThat(featured).contains(featuredSpeaker);
    }

    @Test
    void findConfirmedSpeakers_shouldReturnConfirmedAndFeaturedSpeakers() {
        List<Speaker> confirmed = speakerRepository.findConfirmedSpeakers(currentBranch);

        assertThat(confirmed).hasSizeGreaterThanOrEqualTo(3);
        assertThat(confirmed).contains(featuredSpeaker, confirmedSpeaker, coSpeaker);
        assertThat(confirmed).doesNotContain(submittedSpeaker);
    }

    @Test
    void findSpeakerByName_shouldReturnCorrectSpeaker() {
        Speaker found = speakerRepository.findSpeakerByName("Jane", "Doe");

        assertThat(found).isNotNull();
        assertThat(found.getEmail()).isEqualTo("jane@example.com");
    }

    @Test
    void findAllByBranch_shouldReturnSpeakersForBranch() {
        Page<Speaker> speakers = speakerRepository.findAllByBranch(PageRequest.of(0, 10), currentBranch);

        assertThat(speakers.getContent()).hasSizeGreaterThanOrEqualTo(4);
    }

    @Test
    void findConfirmedSpeaker_shouldReturnConfirmedSpeakerById() {
        Optional<Speaker> found = speakerRepository.findConfirmedSpeaker(confirmedSpeaker.getId(), currentBranch);

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(confirmedSpeaker.getId());
    }

    @Test
    void findConfirmedSpeaker_shouldReturnEmptyForNonConfirmedSpeaker() {
        Optional<Speaker> found = speakerRepository.findConfirmedSpeaker(submittedSpeaker.getId(), currentBranch);

        assertThat(found).isEmpty();
    }
}
