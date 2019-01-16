package site.facade;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import site.config.Globals;
import site.model.*;
import site.repository.*;

import java.util.List;

@Service(AdminService.NAME)
@Transactional
public class AdminService {
	
	public static final String NAME = "adminFacade";
	
	@Autowired
	@Qualifier(ArticleRepository.NAME)
	private ArticleRepository articleRepository;
	
	@Autowired
	@Qualifier(SpeakerRepository.NAME)
	private SpeakerRepository speakerRepository;
	
	@Autowired
	@Qualifier(SponsorRepository.NAME)
	private SponsorRepository sponsorRepository;
	
	@Autowired
	@Qualifier(PartnerRepository.NAME)
	private PartnerRepository partnerRepository;
	
	@Autowired
	@Qualifier(UserRepository.NAME)
	private UserRepository userRepository;

	@Autowired
	@Qualifier(TagRepository.NAME)
	private TagRepository tagRepository;

    @Autowired
    @Qualifier(SubmissionRepository.NAME)
    private SubmissionRepository submissionRepository;

	@Autowired
	@Qualifier(VisitorRepository.NAME)
	private VisitorRepository visitorRepository;

	@Autowired
	@Qualifier(RegistrantRepository.NAME)
	private RegistrantRepository registrantRepository;

	@Autowired
	@Qualifier(SessionRepository.NAME)
	private SessionRepository sessionRepository;

	@Autowired
	@Qualifier(VenueHallRepository.NAME)
	private VenueHallRepository venueHallRepository;

	/* article repo */
	public Page<Article> findAllArticles(Pageable pageable){
		return articleRepository.findAllLatestArticles(pageable);
	}

	public List<Article> findAllArticles(){
		return articleRepository.findAllLatestArticles();
	}
	
	public Article findOneArticle(Long id){
		return articleRepository.findById(id).get();
	}
	
	public void deleteArticle(Long id){
		articleRepository.deleteById(id);
	}
	
	public Article saveArticle(Article article){
		return articleRepository.save(article);
	}



	/* sponsor repo */
	
	public Page<Sponsor> findAllSponsors(Pageable pageable){
		return sponsorRepository.findAll(pageable);
	}
	
	public Sponsor findOneSponsor(Long id){
		return sponsorRepository.findById(id).get();
	}
	
	public void deleteSponsor(Long id){
		sponsorRepository.deleteById(id);
	}
	
	public Sponsor saveSponsor(Sponsor sponsor){
		return sponsorRepository.save(sponsor);
	}
	
	/* partner repo */
	
	public Page<Partner> findAllPartners(Pageable pageable){
		return partnerRepository.findAll(pageable);
	}
	
	public Partner findOnePartner(Long id){
		return partnerRepository.findById(id).get();
	}
	
	public void deletePartner(Long id){
		partnerRepository.deleteById(id);
	}
	
	public Partner savePartner(Partner partner){
		return partnerRepository.save(partner);
	}




	/* speaker repo */
	
	public Page<Speaker> findAllSpeakers(Pageable pageable){
		return speakerRepository.findAll(pageable);
	}
	
	public Speaker findOneSpeaker(Long id){
		return speakerRepository.findById(id).get();
	}
	
	public void deleteSpeaker(Long id){
		speakerRepository.deleteById(id);
	}
	
	public Speaker saveSpeaker(Speaker speaker){
		return speakerRepository.save(speaker);
	}
	



	/* user repo */
	
	public Page<User> findAllUsers(Pageable pageable){
		return userRepository.findAll(pageable);
	}
	
	public User findOneUser(Long id){
		return userRepository.findById(id).get();
	}

    //currently used for admin TODO:to be fixed with normal authentication with spring
    public User findUserByEmail(String email){
        if(userRepository.findByEmail(email).size()>0) {
            return userRepository.findByEmail(email).get(0);
        }
        return null;
    }
	
	public void deleteUser(Long id){
		userRepository.deleteById(id);
	}

	public User saveUser(User user){
		return userRepository.save(user);
	}



	/* tag repo */
	
	public Page<Tag> findAllTags(Pageable pageable){
		return tagRepository.findAll(pageable);
	}
	
	public Iterable<Tag> findAllTags(){
		return tagRepository.findAll();
	}
	
	public Tag findOneTag(Long id){
		return tagRepository.findById(id).get();
	}
	
	public void deleteTag(Long id){
		tagRepository.deleteById(id);
	}
	
	public Tag saveTag(Tag tag){
		return tagRepository.save(tag);
	}

	/* submissions repo */
    public Page<Submission> findAllSubmissions(Pageable pageable) {
        return submissionRepository.findAll(pageable);
    }
    
    public Page<Submission> findAllSubmissionsForBranch(Branch branch, Pageable pageable) {
        return submissionRepository.findAllByBranch(branch, pageable);
    }
    

    public Submission findOneSubmission(Long submissionId) {
        return submissionRepository.findById(submissionId).get();
    }

    public void acceptSubmission(Submission submission) {
        changeStatusTo(submission, SubmissionStatus.ACCEPTED);
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

	public Iterable<Submission> findAllAcceptedSubmissionsForBranch(Branch branch) {
		return submissionRepository.findByBranchAndStatus(branch, SubmissionStatus.ACCEPTED);
	}
	
	public List<Submission> findAllSubmitedSubmissionsForCurrentBranch(){
		return submissionRepository.findByBranchAndStatus(Globals.CURRENT_BRANCH, SubmissionStatus.SUBMITTED);
	}

	/* visitors repo */
	public Page<Visitor> findAllVisitors(Pageable pageable){
		return visitorRepository.findAll(pageable);
	}

	public Iterable<Visitor> findAllVisitors(){
		return visitorRepository.findAll();
	}

	public List<Visitor> findAllNewestVisitors(){
		return visitorRepository.findAllNewestUsers();
	}

	public Visitor findOneVisitor(Long id){
		return visitorRepository.findById(id).get();
	}

	public void deleteVisitor(Long id){
		Visitor visitor = visitorRepository.findById(id).get();
		Registrant registrant = visitor.getRegistrant();
		registrant.getVisitors().remove(visitor);
		visitorRepository.delete(visitor);
		if (registrant.getEmail().equals(visitor.getEmail())&&registrant.getName().equals(visitor.getName())&&registrant.getVisitors().isEmpty()){
			registrantRepository.delete(registrant);
		}else {
			registrantRepository.save(registrant);
		}

	}

	public Visitor saveVisitor(Visitor visitor){
		return visitorRepository.save(visitor);
	}



	/* registrants repo*/
	public Page<Registrant> findAllRegistrants(Pageable pageable){
		return registrantRepository.findAll(pageable);
	}

	public Registrant saveRegistrant(Registrant registrant){
		return registrantRepository.save(registrant);
	}

	public Iterable<Registrant> findAllRegistrants(){
		return registrantRepository.findAll();
	}

    public Registrant findOneRegistrant(Long itemId) {
        return registrantRepository.findById(itemId).get();
    }

    public void deleteRegistrant(Long itemId) {
        registrantRepository.deleteById(itemId);
    }

	/* sessions repo */
	public Iterable<Session> findAllSessions() {
		return sessionRepository.findAll();
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

}
