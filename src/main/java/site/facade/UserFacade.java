package site.facade;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import site.model.Speaker;
import site.repository.ArticleRepository;
import site.repository.SpeakerRepository;
import site.repository.SponsorRepository;
import site.repository.TagRepository;
import site.repository.UserRepository;

@Service(UserFacade.NAME)
@Transactional
public class UserFacade {

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
	@Qualifier(UserRepository.NAME)
	private UserRepository userRepository;

	@Autowired
	@Qualifier(TagRepository.NAME)
	private TagRepository tagRepository;
	
	public Speaker findSpeaker(String name){
		//TODO
		return null;
	}
	
}
