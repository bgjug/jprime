package site.facade;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import site.model.*;
import site.repository.*;

@Service
@Transactional
public class AdminService {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private SpeakerRepository speakerRepository;

    @Autowired
    private SponsorRepository sponsorRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private VisitorRepository visitorRepository;

    @Autowired
    private RegistrantRepository registrantRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private VenueHallRepository venueHallRepository;

    @Autowired
    private BackgroundJobRepository jobRepository;

    @Autowired
    private BranchService branchService;

    /* article repo */
    public Page<Article> findAllArticles(Pageable pageable) {
        return articleRepository.findAllLatestArticles(pageable);
    }

    public List<Article> findAllArticles() {
        return articleRepository.findAllLatestArticles();
    }

    public Article findOneArticle(Long id) {
        return articleRepository.findById(id).get();
    }

    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }

    public Article saveArticle(Article article) {
        return articleRepository.save(article);
    }



    /* sponsor repo */

    public Page<Sponsor> findAllSponsors(Pageable pageable) {
        return sponsorRepository.findAll(pageable);
    }

    public Sponsor findOneSponsor(Long id) {
        return sponsorRepository.findById(id).get();
    }

    public void deleteSponsor(Long id) {
        sponsorRepository.deleteById(id);
    }

    public Sponsor saveSponsor(Sponsor sponsor) {
        return sponsorRepository.save(sponsor);
    }

    /* partner repo */

    public Page<Partner> findAllPartners(Pageable pageable) {
        return partnerRepository.findAll(pageable);
    }

    public Partner findOnePartner(Long id) {
        return partnerRepository.findById(id).get();
    }

    public void deletePartner(Long id) {
        partnerRepository.deleteById(id);
    }

    public Partner savePartner(Partner partner) {
        return partnerRepository.save(partner);
    }




    /* speaker repo */

    public Page<Speaker> findAllSpeakers(Pageable pageable) {
        Page<Speaker> all = speakerRepository.findAll(pageable);
        all.getContent().forEach(s -> s.updateFlags(branchService.getCurrentBranch()));
        return all;
    }

    public Speaker findOneSpeaker(Long id) {
        return speakerRepository.findById(id).orElse(null);
    }

    public void deleteSpeaker(Long id) {
        speakerRepository.deleteById(id);
    }

    public Speaker saveSpeaker(Speaker speaker) {
        Speaker existing = speakerRepository.findByEmail(speaker.getEmail());
        if (existing != null) {
            speaker.setId(existing.getId());
        }
        return speakerRepository.save(speaker);
    }




    /* user repo */

    public Page<User> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User findOneUser(Long id) {
        return userRepository.findById(id).get();
    }

    //currently used for admin TODO:to be fixed with normal authentication with spring
    public User findUserByEmail(String email) {
        if (!userRepository.findByEmail(email).isEmpty()) {
            return userRepository.findByEmail(email).get(0);
        }
        return null;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }



    /* tag repo */

    public Page<Tag> findAllTags(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    public Iterable<Tag> findAllTags() {
        return tagRepository.findAll();
    }

    public Tag findOneTag(Long id) {
        return tagRepository.findById(id).get();
    }

    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    /* submissions repo */
    public Page<Submission> findAllSubmissions(Pageable pageable) {
        return submissionRepository.findAll(pageable);
    }

    public Page<Submission> findAllSubmissionsForBranch(Branch branch, Pageable pageable) {
        return submissionRepository.findAllByBranch(branch, pageable);
    }

    public Page<Submission> findAllSubmissionsForBranchAndStatus(Branch branch, SubmissionStatus status, Pageable pageable) {
        return submissionRepository.findByBranchAndStatus(branch,status, pageable);
    }

    public Submission findOneSubmission(Long submissionId) {
        return submissionRepository.findById(submissionId).get();
    }

    public void acceptSubmission(Submission submission) {
        changeStatusTo(submission, SubmissionStatus.ACCEPTED);
    }

    public void confirmSubmission(Submission submission) {
        changeStatusTo(submission, SubmissionStatus.CONFIRMED);
    }

    public void cancelSubmission(Submission submission) {
        changeStatusTo(submission, SubmissionStatus.CANCELED);
    }

    public void rejectSubmission(Submission submission) {
        changeStatusTo(submission, SubmissionStatus.REJECTED);
    }

    public void deleteSubmission(Submission submission) {
        submissionRepository.delete(submission);
    }

    private void changeStatusTo(Submission submission, SubmissionStatus status) {
        submission.setStatus(status);
        submissionRepository.save(submission);
    }

    public List<Submission> findAllConfirmedSubmissionsForBranch(Branch branch) {
        return submissionRepository.findByBranchAndStatus(branch, SubmissionStatus.CONFIRMED);
    }

    public List<Submission> findAllSubmittedSubmissionsForCurrentBranch() {
        return submissionRepository.findByBranchAndStatus(branchService.getCurrentBranch(),
            SubmissionStatus.SUBMITTED);
    }

    /* visitors repo */
    public Page<Visitor> findAllVisitors(Pageable pageable) {
        return visitorRepository.findAll(pageable);
    }

    public Iterable<Visitor> findAllVisitors() {
        return visitorRepository.findAll();
    }

    public Iterable<Visitor> findAllWithTicket(Branch branch) {
        return visitorRepository.findAllWithTicket(branch);
    }

    public List<Visitor> findAllNewestVisitors(Branch branch) {
        return visitorRepository.findAllNewestUsers(branch);
    }

    public Visitor findOneVisitor(Long id) {
        return visitorRepository.findById(id).get();
    }

    public void deleteVisitor(Long id) {
        Visitor visitor = visitorRepository.findById(id).get();
        Registrant registrant = visitor.getRegistrant();
        registrant.getVisitors().remove(visitor);
        visitorRepository.delete(visitor);
        if (registrant.getEmail().equals(visitor.getEmail()) && registrant.getName()
            .equals(visitor.getName()) && registrant.getVisitors().isEmpty()) {
            registrantRepository.delete(registrant);
        } else {
            registrantRepository.save(registrant);
        }

    }

    public Visitor saveVisitor(Visitor visitor) {
        return visitorRepository.save(visitor);
    }

    /* registrants repo*/
    public Page<Registrant> findAllRegistrants(Pageable pageable) {
        return registrantRepository.findAll(pageable);
    }

    public Page<Registrant> findRegistrantsByBranch(Pageable pageable, Branch branch) {
        return registrantRepository.findAllByBranch(pageable, branch);
    }

    public Iterable<Registrant> findRegistrantsByBranch(Branch branch) {
        return registrantRepository.findAllByBranch(branch);
    }

    public Registrant saveRegistrant(Registrant registrant) {
        return registrantRepository.save(registrant);
    }

    public Iterable<Registrant> findAllRegistrants() {
        return registrantRepository.findAll();
    }

    public Registrant findOneRegistrant(Long itemId) {
        return registrantRepository.findById(itemId).get();
    }

    public void deleteRegistrant(Long itemId) {
        registrantRepository.deleteById(itemId);
    }

    /* sessions repo */
    public List<Session> findAllSessions() {
        return sessionRepository.findBySubmissionBranchOrSubmissionIsNullOrderByStartTimeAsc(
            branchService.getCurrentBranch());
    }

    public Session saveSession(Session session) {
        return sessionRepository.save(session);
    }

    public Session findOneSession(Long itemId) {
        return sessionRepository.findById(itemId).get();
    }

    public void deleteSession(Long itemId) {
        sessionRepository.deleteById(itemId);
    }

    /* venue halls repo */
    public Iterable<VenueHall> findAllVenueHalls() {
        return venueHallRepository.findAll();
    }

    public VenueHall saveVenueHall(VenueHall venueHall) {
        return venueHallRepository.save(venueHall);
    }

    public VenueHall findOneVenueHall(Long itemId) {
        return venueHallRepository.findById(itemId).get();
    }

    public void deleteVenueHall(Long itemId) {
        venueHallRepository.deleteById(itemId);
    }

    public Page<Speaker> findSpeakersByBranch(Pageable pageable, Branch branch) {
        Page<Speaker> allByBranch = speakerRepository.findAllByBranch(pageable, branch);
        allByBranch.stream().forEach(s->s.updateFlags(branch));
        return allByBranch;
    }

    public Optional<Visitor> findVisitorByTicket(String ticket) {
        return visitorRepository.findByTicket(ticket);
    }

    public List<Visitor> searchVisitor(Branch branch, String email, String firstName, String lastName,
        String company) {
        String query =
            "select v from Visitor as v where v.ticket is not null and v.registrant.branch = :branch and ";
        Map<String, Object> paramsMap = new HashMap<>();
        List<String> whereClauses = new ArrayList<>();

        paramsMap.put("branch", branch);

        if (StringUtils.isNotBlank(email)) {
            whereClauses.add("lower(v.email) like :email");
            paramsMap.put("email", '%' + email.toLowerCase() + '%');
        }

        String name = "";
        if (StringUtils.isNotBlank(firstName)) {
            name += firstName + '%';
        }

        if (StringUtils.isNotBlank(lastName)) {
            if (StringUtils.isEmpty(name)) {
                name += "% " + lastName + '%';
            } else {
                name += ' ' + lastName + '%';
            }
        }

        if (StringUtils.isNotBlank(name)) {
            whereClauses.add("lower(v.name) like :name");
            paramsMap.put("name", name.toLowerCase());
        }

        if (StringUtils.isNotBlank(company)) {
            whereClauses.add("(lower(v.company) like :company or lower(v.registrant.name) like :company)");
            paramsMap.put("company", '%' + company.toLowerCase() + '%');
        }

        query += String.join(" and ", whereClauses);

        TypedQuery<Visitor> searchQuery = entityManager.createQuery(query, Visitor.class);
        paramsMap.forEach(searchQuery::setParameter);

        return searchQuery.getResultList();
    }

    public List<Speaker> searchSpeaker(String firstName, String lastName, String email) {
        String query = "select s from Speaker as s where ";
        Map<String, String> paramsMap = new HashMap<>();
        List<String> whereClauses = new ArrayList<>();

        if (StringUtils.isNotBlank(email)) {
            whereClauses.add("lower(s.email) like :email");
            paramsMap.put("email", '%' + email.toLowerCase() + '%');
        }

        if (StringUtils.isNotBlank(firstName)) {
            whereClauses.add("lower(s.firstName) like :firstName");
            paramsMap.put("firstName", '%' + firstName.toLowerCase() + '%');
        }

        if (StringUtils.isNotBlank(lastName)) {
            whereClauses.add("lower(s.lastName) like :lastName");
            paramsMap.put("lastName", '%' + lastName.toLowerCase() + '%');
        }

        query += String.join(" and ", whereClauses);

        TypedQuery<Speaker> searchQuery = entityManager.createQuery(query, Speaker.class);
        paramsMap.forEach(searchQuery::setParameter);

        return searchQuery.getResultList()
            .stream()
            .map(s -> s.updateFlags(branchService.getCurrentBranch()))
            .toList();
    }

    public List<BackgroundJob> findBackgroundJobs() {
        List<BackgroundJob> backgroundJobs = new ArrayList<>();
        backgroundJobs.addAll(jobRepository.findPendingJobs());
        backgroundJobs.addAll(jobRepository.findCompletedJobs(LocalDateTime.now().minusHours(8)));
        return backgroundJobs;
    }

    public List<SubmissionByStatus> countSubmissionsByStatusForBranch(Branch branch) {
        return submissionRepository.countSubmissionsByStatusForBranch(branch);
    }

    public Session detachSession(Session session) {
        entityManager.detach(session);
        return session;
    }
}
