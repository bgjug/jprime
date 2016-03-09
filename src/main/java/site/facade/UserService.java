package site.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import site.config.Globals;
import site.model.Article;
import site.model.Partner;
import site.model.Speaker;
import site.model.Sponsor;
import site.model.SponsorPackage;
import site.model.Submission;
import site.model.Tag;
import site.repository.ArticleRepository;
import site.repository.PartnerRepository;
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

    /**
     * Speaker
     * @param id
     * @return
     */
    public Speaker findSpeaker(Long id){
		return speakerRepository.findOne(id);
	}

    /**
     * Article
     * @param id
     * @return
     */
	public Article findArticle(Long id){
		return articleRepository.findOne(id);
	}

    public Page<Article> allArticles(Pageable pageable){
        return articleRepository.findAll(pageable);
    }

    public Page<Article> allPublishedArticles(Pageable pageable){
        return articleRepository.findByPublishedTrueOrderByCreatedDateDesc(pageable);
    }
    public Article getArticleById(long id){
        return articleRepository.findOne(id);
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
    
    public Speaker findSpeaker(String email) {
        return speakerRepository.findByEmail(email);
    }

	public Map<SponsorPackage, List<Sponsor>> findAllActiveSponsors(){
        return sponsorRepository.findByActive(true).stream().collect(groupingBy(Sponsor::getSponsorPackage));
    }
	
	public List<Partner> findAllPartners(){
        return partnerRepository.findAll();
    }

    public void	submitTalk(Submission submission) {
        submissionRepository.save(submission);
    }
}
