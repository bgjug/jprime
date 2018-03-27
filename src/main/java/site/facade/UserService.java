package site.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import site.config.Globals;
import site.model.*;
import site.repository.ArticleRepository;
import site.repository.PartnerRepository;
import site.repository.SessionRepository;
import site.repository.SpeakerRepository;
import site.repository.SponsorRepository;
import site.repository.SubmissionRepository;
import site.repository.TagRepository;
import site.repository.UserRepository;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Service(UserService.NAME)
@Transactional
public class UserService {

	public static final String NAME = "userFacade";
	
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
	@Qualifier(TagRepository.NAME)
	private TagRepository tagRepository;

    @Autowired
    @Qualifier(SubmissionRepository.NAME)
    private SubmissionRepository submissionRepository;
    
    @Autowired
    @Qualifier(SessionRepository.NAME)
	private SessionRepository sessionRepository;
	

    /**
     * Speaker
     * @param id
     * @return
     */
    public Speaker findSpeaker(Long id){
		return speakerRepository.findById(id).get();
	}

    /**
     * Article
     * @param id
     * @return
     */
	public Article findArticle(Long id){
		return articleRepository.findById(id).get();
	}

    public Page<Article> allArticles(Pageable pageable){
        return articleRepository.findAll(pageable);
    }

    public Page<Article> allPublishedArticles(Pageable pageable){
        return articleRepository.findByPublishedTrueOrderByCreatedDateDesc(pageable);
    }
    public Article getArticleById(long id){
        return articleRepository.findById(id).get();
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

    public Speaker findSpeaker(String email) {
        return speakerRepository.findByEmail(email);
    }

	public Map<SponsorPackage, List<Sponsor>> findAllActiveSponsors(){
        return sponsorRepository.findByActive(true).stream().collect(groupingBy(Sponsor::getSponsorPackage));
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
    	return sessionRepository.findById(id).get();
    }
    
    public List<Session> findSessionTalksAndBreaksByHallName(String hallName){
    	return sessionRepository.findByHallNameOrHallIsNullOrderByStartTimeAsc(hallName);
    }
}
