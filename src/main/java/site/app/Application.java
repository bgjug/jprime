package site.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "site")
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = { "site.repository" } )
@EntityScan(basePackages="site.model")
@EnableJpaAuditing
@EnableSpringDataWebSupport
public class Application {
	
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
//		System.out.println("Let's inspect the beans provided by Spring Boot:");
//
//		String[] beanNames = ctx.getBeanDefinitionNames();
//		Arrays.sort(beanNames);
//		for (String beanName : beanNames) {
//			System.out.println(beanName);
//		}
		
//		 UserRepository repository = context.getBean(UserRepository.class);
//		 //example data
//		 User user = new User();
//		 user.setEmail("nov34@test.com");
//		 user.setFirstName("test");
//		 user.setLastName("test");
//
//		 repository.save(user);
//		 Iterable<User> allUsers = repository.findAll();
//		 for(User theUser : allUsers){
//			 System.out.println(theUser.getEmail());
//		 }
//		 
//		 SponsorRepository sponsorRepository = context.getBean(SponsorRepository.class);
//		 
//		 Sponsor sponsor = new Sponsor();
//		 sponsor.setEmail("test@test.com");
//		 sponsor.setSponsorPackage(SponsorPackage.DIAMOND);
//		 
//		 sponsorRepository.save(sponsor);
		
//		Page page = new Page();
//		page.setName("home");
//		PageRepository pageRepository = context.getBean(PageRepository.class);
//		pageRepository.save(page);
		 
//		Link link = new Link();
//		link.setName("Registration");
//		link.setUrl("/");
//		LinkRepository linkRepository = context.getBean(LinkRepository.class);
//		linkRepository.save(link);
//		 context.close();
	}

}