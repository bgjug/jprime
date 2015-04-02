package site.facade;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import site.model.Article;
import site.model.Speaker;
import site.model.Sponsor;
import site.model.Submission;
import site.model.SubmissionStatus;
import site.model.Tag;
import site.model.User;
import site.repository.ArticleRepository;
import site.repository.SpeakerRepository;
import site.repository.SponsorRepository;
import site.repository.SubmissionRepository;
import site.repository.TagRepository;
import site.repository.UserRepository;

@Service(AdminFacade.NAME)
@Transactional
public class AdminFacade {
	
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
	@Qualifier(UserRepository.NAME)
	private UserRepository userRepository;

	@Autowired
	@Qualifier(TagRepository.NAME)
	private TagRepository tagRepository;

    @Autowired
    @Qualifier(SubmissionRepository.NAME)
    private SubmissionRepository submissionRepository;
	
	/* article repo */
	
	public Page<Article> findAllArticles(Pageable pageable){
		return articleRepository.findAll(pageable);
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

    //currently used for admin TODO:to be fixed with normal authentication with spring
    public User findUserByEmail(String email){
        if(userRepository.findByEmail(email).size()>0) {
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

    public Iterable<Submission> findAllSubmissions() {
        return submissionRepository.findAll();
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
}
