package site.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import site.config.Globals;
import site.model.Article;
import site.model.Partner;
import site.model.PartnerPackage;
import site.model.Session;
import site.model.Speaker;
import site.model.Sponsor;
import site.model.SponsorPackage;
import site.model.Submission;
import site.model.Tag;
import site.model.User;
import site.model.Visitor;
import site.repository.ArticleRepository;
import site.repository.PartnerRepository;
import site.repository.SessionRepository;
import site.repository.SpeakerRepository;
import site.repository.SponsorRepository;
import site.repository.SubmissionRepository;
import site.repository.TagRepository;
import site.repository.UserRepository;
import site.repository.VisitorRepository;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;

@Service(UserService.NAME)
@Transactional
public class UserService {

	public static final String NAME = "userFacade";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
	@Qualifier(ArticleRepository.NAME)
	private ArticleRepository articleRepository;

	@Autowired
	@Qualifier(SpeakerRepository.NAME)
	private SpeakerRepository speakerRepository;

    @Autowired
    @Qualifier(UserRepository.NAME)
    private UserRepository userRepository;

	@Autowired
	@Qualifier(SponsorRepository.NAME)
	private SponsorRepository sponsorRepository;

	@Autowired
	@Qualifier(PartnerRepository.NAME)
	private PartnerRepository partnerRepository;

	@Autowired
	@Qualifier(TagRepository.NAME)
	private TagRepository tagRepository;

    @Autowired
    @Qualifier(SubmissionRepository.NAME)
    private SubmissionRepository submissionRepository;

    @Autowired
    @Qualifier(SessionRepository.NAME)
	private SessionRepository sessionRepository;

    @Autowired
    @Qualifier(VisitorRepository.NAME)
    private VisitorRepository visitorRepository;

    /**
     * Speaker
     *
     * @param id
     * @return
     */
    public Speaker findSpeaker(Long id){
		return speakerRepository.findById(id).orElse(null);
	}

    /**
     * Article
     *
     * @param id
     * @return
     */
	public Article findArticle(Long id){
		return articleRepository.findById(id).orElse(null);
	}

    public Page<Article> allArticles(Pageable pageable){
        return articleRepository.findAll(pageable);
    }

    public Page<Article> allPublishedArticles(Pageable pageable){
        return articleRepository.findByPublishedTrueOrderByCreatedDateDesc(pageable);
    }

    public Article getArticleById(long id){
        return articleRepository.findById(id).orElse(null);
    }

    public Article getArticleByTitle(String title){
        return articleRepository.findByTitle(title);
    }

	public List<Article> findArticlesByTag(String tagName){
		return articleRepository.findByTag(tagName);
	}

    public Page<Article> findArticlesByTag(String tagName, Pageable pageable) {
        return articleRepository.findByTag(tagName, pageable);
    }

    public List<Tag> findAllTags() {
        return tagRepository.findAll();
    }

	public Page<Speaker> findAllSpeakers(Pageable pageable){
		return speakerRepository.findAll(pageable);
	}

    public List<Speaker> findFeaturedSpeakers() {
        return speakerRepository.findFeaturedSpeakers(Globals.CURRENT_BRANCH);
    }

    public List<Speaker> findAcceptedSpeakers() {
        return speakerRepository.findAcceptedSpeakers(Globals.CURRENT_BRANCH);
    }

    public Speaker findSpeaker(Speaker speakerToFind) {
        String email = speakerToFind.getEmail();
        Speaker speaker = speakerRepository.findByEmail(email);
        if (speaker != null) {
            return speaker;
        }

        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            return null;
        }

        if (!user.getFirstName().equalsIgnoreCase(speakerToFind.getFirstName())) {
            throw new ServiceException("Invalid user information!!! Can't store the data.");
        }

        if (!user.getLastName().equalsIgnoreCase(speakerToFind.getLastName())) {
            throw new ServiceException("Invalid user information!!! Can't store the data.");
        }

        userRepository.convertToSpeaker(user.getId());
        entityManager.unwrap(org.hibernate.Session.class).evict(user);
        return speakerRepository.findByEmail(email);
    }

	public Map<SponsorPackage, List<Sponsor>> findAllActiveSponsors(){
        return sponsorRepository.findByActive(true).stream().collect(groupingBy(Sponsor::getSponsorPackage));
    }

    public List<Partner> findAllActiveOfficalSupportingPartners(){
        return partnerRepository.findByActiveAndPartnerPackage(true, PartnerPackage.SUPPORTERS);
    }

    public List<Partner> findAllActiveMediaPartners(){
        List<Partner> partners = partnerRepository.findByActiveAndPartnerPackage(true, PartnerPackage.MEDIA);
        partners.addAll(partnerRepository.findByActiveAndPartnerPackage(true, null));
        return partners;
    }

    public List<Partner> findAllActiveEventPartners(){
        return partnerRepository.findByActiveAndPartnerPackage(true, PartnerPackage.OTHER);
    }

    public void	submitTalk(Submission submission) {
        submissionRepository.save(submission);
    }

    public Session findSessionTalk(long id){
    	return sessionRepository.findById(id).orElse(null);
    }

    public List<Session> findSessionTalksAndBreaksByHallName(String hallName) {
        return sessionRepository.findSessionsForBranchAndHallOrHallIsNull(hallName, Globals.CURRENT_BRANCH.name());
    }

    public boolean setPresentById(Visitor visitorExample){
	    Optional<Visitor> persistedVisitor = visitorRepository.findById(visitorExample.getId());
	    if(persistedVisitor.isPresent()){
	        persistedVisitor.get().setPresent(true);
	        visitorRepository.save(persistedVisitor.get());
            return true;
        }
        return false;
    }

    public boolean setPresentByNameIgnoreCase(Visitor visitorExample) {
        List<Visitor> matchedVisitors = visitorRepository.findByNameIgnoreCase(visitorExample.getName());

        if (!matchedVisitors.isEmpty()) {
            matchedVisitors.forEach(v -> v.setPresent(true));
            visitorRepository.saveAll(matchedVisitors);
            return true;
        }
        return false;
    }

    public boolean setPresentByNameIgnoreCaseAndCompanyIgnoreCase(Visitor visitorExample){
        List<Visitor> matchedVisitors =  visitorRepository.findByNameIgnoreCaseAndCompanyIgnoreCase(visitorExample.getName(), visitorExample.getCompany());
        if (!matchedVisitors.isEmpty()) {
            matchedVisitors.forEach(v -> v.setPresent(true));
            visitorRepository.saveAll(matchedVisitors);
            return true;
        }
        return false;
    }

}
