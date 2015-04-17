package site.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import site.model.*;
import site.repository.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Service(RegistrantFacade.NAME)
@Transactional
public class RegistrantFacade {

	public static final String NAME = "registrantFacade";
	
	@Autowired
	@Qualifier(RegistrantRepository.NAME)
	private RegistrantRepository registrantRepository;
//
//	@Autowired
//	@Qualifier(SpeakerRepository.NAME)
//	private SpeakerRepository speakerRepository;
//
//	@Autowired
//	@Qualifier(SponsorRepository.NAME)
//	private SponsorRepository sponsorRepository;
//
//	@Autowired
//	@Qualifier(TagRepository.NAME)
//	private TagRepository tagRepository;
//
//    @Autowired
//    @Qualifier(SubmissionRepository.NAME)
//    private SubmissionRepository submissionRepository;

//    /**
//     * Speaker
//     * @param id
//     * @return
//     */
//    public Speaker findSpeaker(Long id){
//		return speakerRepository.findOne(id);
//	}
//
//    /**
//     * Article
//     * @param id
//     * @return
//     */
//	public Article findArticle(Long id){
//		return articleRepository.findOne(id);
//	}
//
//    public Page<Article> allArticles(Pageable pageable){
//        return articleRepository.findAll(pageable);
//    }
//
//    public Page<Article> allPublishedArticles(Pageable pageable){
//        return articleRepository.findAllPublishedArticles(pageable);
//    }
//    public Article getArticleById(long id){
//        return articleRepository.findOne(id);
//    }
//
//	public List<Article> findArticlesByTag(String tagName){
//		return articleRepository.findByTag(tagName);
//	}
//
//    public Page<Article> findArticlesByTag(String tagName, Pageable pageable) {
//        return articleRepository.findByTag(tagName, pageable);
//    }
//
//    public List<Tag> findAllTags() {
//        return tagRepository.findAll();
//    }
//
//	public Page<Speaker> findAllSpeakers(Pageable pageable){
//		return speakerRepository.findAll(pageable);
//	}
//
//    public List<Speaker> findFeaturedSpeakers() {
//        return speakerRepository.findFeaturedSpeakers();
//    }
//
//	public Map<SponsorPackage, List<Sponsor>> findAllSponsors(){
//        return sponsorRepository.findAll().stream().collect(groupingBy(Sponsor::getSponsorPackage));
//    }

//    public void	save(Submission submission) {
//        submissionRepository.save(submission);
//    }

    public Registrant save(Registrant registrant) {

        //todo: mihail this is fucking ugly
        for(Visitor visitor:registrant.getVisitors()) {
            visitor.setRegistrant(registrant);
        }

        return registrantRepository.save(registrant);
    }
}
