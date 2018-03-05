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
	@Qualifier(RoleRepository.NAME)
	private RoleRepository roleRepository;

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
		return articleRepository.findOne(id);
	}
	
	public void deleteArticle(Long id){
		articleRepository.delete(id);
	}
	
	public Article saveArticle(Article article){
		return articleRepository.save(article);
	}



	/* sponsor repo */
	
	public Page<Sponsor> findAllSponsors(Pageable pageable){
		return sponsorRepository.findAll(pageable);
	}
	
	public Sponsor findOneSponsor(Long id){
		return sponsorRepository.findOne(id);
	}
	
	public void deleteSponsor(Long id){
		sponsorRepository.delete(id);
	}
	
	public Sponsor saveSponsor(Sponsor sponsor){
		return sponsorRepository.save(sponsor);
	}
	
	/* partner repo */
	
	public Page<Partner> findAllPartners(Pageable pageable){
		return partnerRepository.findAll(pageable);
	}
	
	public Partner findOnePartner(Long id){
		return partnerRepository.findOne(id);
	}
	
	public void deletePartner(Long id){
		partnerRepository.delete(id);
	}
	
	public Partner savePartner(Partner partner){
		return partnerRepository.save(partner);
	}




	/* speaker repo */
	
	public Page<Speaker> findAllSpeakers(Pageable pageable){
		return speakerRepository.findAll(pageable);
	}
	
	public Speaker findOneSpeaker(Long id){
		return speakerRepository.findOne(id);
	}
	
	public void deleteSpeaker(Long id){
		speakerRepository.delete(id);
	}
	
	public Speaker saveSpeaker(Speaker speaker){
		return speakerRepository.save(speaker);
	}
	



	/* user repo */
	
	public Page<User> findAllUsers(Pageable pageable){
		return userRepository.findAll(pageable);
	}
	
	public User findOneUser(Long id){
		return userRepository.findOne(id);
	}

	//@Trifon
	public Role findRoleByName(String roleName) {
		return roleRepository.findByName( roleName );
	}
	//@Trifon
	public Role findRoleByNameOrCreate(String roleName) {
        Role role = this.findRoleByName( roleName );
		if (role == null) {
        	role = new Role( roleName );
        	role = this.saveRole( role );
        }
		return role;
	}
	//@Trifon
	public Role saveRole(Role role) {
		return roleRepository.save( role );
	}
	//@Trifon
	public User createRegularUser(String email) {
		 Role userRole = this.findRoleByNameOrCreate(Role.USER_NAME);

		 User user = new User();
		 user.setEmail( email );
		 user.addRole( userRole );
		 return user;
	}

    //currently used for admin TODO:to be fixed with normal authentication with spring
    public User findUserByEmail(String email){
        if (userRepository.findByEmail(email).size()>0) {
            return userRepository.findByEmail(email).get(0);
        }
        return null;
    }
	
	public void deleteUser(Long id){
		userRepository.delete(id);
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
		return tagRepository.findOne(id);
	}
	
	public void deleteTag(Long id){
		tagRepository.delete(id);
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
        return submissionRepository.findOne(submissionId);
    }

    public void acceptSubmission(Submission submission) {
        changeStatusTo(submission, SubmissionStatus.ACCEPTED);
    }

    public void rejectSubmission(Submission submission) {
        changeStatusTo(submission, SubmissionStatus.REJECTED);
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
		return visitorRepository.findOne(id);
	}

	public void deleteVisitor(Long id){
		Visitor visitor = visitorRepository.findOne(id);
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
        return registrantRepository.findOne(itemId);
    }

    public void deleteRegistrant(Long itemId) {
        registrantRepository.delete(itemId);
    }

	/* sessions repo */
	public Iterable<Session> findAllSessions() {
		return sessionRepository.findAll();
	}

	public Session saveSession(Session session) {
		return sessionRepository.save(session);
	}

	public Session findOneSession(Long itemId) {
		return sessionRepository.findOne(itemId);
	}

	public void deleteSession(Long itemId) {
		sessionRepository.delete(itemId);
	}

	/* venue halls repo */
	public Iterable<VenueHall> findAllVenueHalls() {
		return venueHallRepository.findAll();
	}

	public VenueHall saveVenueHall(VenueHall venueHall) {
		return venueHallRepository.save(venueHall);
	}

	public VenueHall findOneVenueHall(Long itemId) {
		return venueHallRepository.findOne(itemId);
	}

	public void deleteVenueHall(Long itemId) {
		venueHallRepository.delete(itemId);
	}

}
