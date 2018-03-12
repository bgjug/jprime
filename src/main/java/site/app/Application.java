package site.app;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * to make this deployable as war, this is necessary:
 * http://docs.spring.io/spring-boot/docs/current/reference/html/howto-traditional-deployment.html
 */
@Configuration
@ConfigurationProperties//mihail: so that the conf properties are supplied here
@EnableAutoConfiguration
@ComponentScan(basePackages = "site")
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = { "site.repository" } )
@EntityScan(basePackages="site.model")
@EnableJpaAuditing
@EnableSpringDataWebSupport
@SpringBootApplication//mihail: so that it can be run as war file
public class Application  extends SpringBootServletInitializer {

    /** mihail: so that it can be run as war file */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		
//		VenueHall hall = new VenueHall();
//		hall.setName("VITOSHA");
//		hall.setDescription("ala bala na dqsno");
//		hall = context.getBean(VenueHallRepository.class).save(hall);
//		
//		Session session = new Session();
//		session.setStartTime(DateTime.now());
//		session.setEndTime(DateTime.now().minus(Duration.standardHours(1)));
//		session.setSubmission(context.getBean(SubmissionRepository.class).findAll().iterator().next());
//		session.setHall(hall);
//		
//		context.getBean(SessionRepository.class).save(session);
		
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
//       user.setEmail("nov34@test.com");
//       user.setFirstName("test");
//       user.setLastName("test");
//
//		 repository.save(user);
		 
		 
//		 Iterable<User> allUsers = repository.findAll();
//		 for(User theUser : allUsers){
//			 System.out.println(theUser.getEmail());
//		 }
		 
//		 SponsorRepository sponsorRepository = context.getBean(SponsorRepository.class);
//		 
//		 Sponsor sponsor = new Sponsor();
//		 sponsor.setEmail("test@test.com");
//		 sponsor.setSponsorPackage(SponsorPackage.DIAMOND);
//		 
//		 sponsorRepository.save(sponsor);
//		
//		Page page = new Page();
//		page.setName("home");
//		PageRepository pageRepository = context.getBean(PageRepository.class);
//		pageRepository.save(page);
//		 
//		Link link = new Link();
//		link.setName("Registration");
//		link.setUrl("/");
//		LinkRepository linkRepository = context.getBean(LinkRepository.class);
//		linkRepository.save(link);
//		 context.close();
	}


//    private DataSource dataSource() {
//        final HikariDataSource ds = new HikariDataSource();
//        ds.setMaximumPoolSize(100);
//        ds.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
//        ds.addDataSourceProperty("url", url);
//        ds.addDataSourceProperty("user", username);
//        ds.addDataSourceProperty("password", password);
//        ds.addDataSourceProperty("cachePrepStmts", true);
//        ds.addDataSourceProperty("prepStmtCacheSize", 250);
//        ds.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
//        ds.addDataSourceProperty("useServerPrepStmts", true);
//        return ds;
//    }


    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.url}")
    private String dataSourceUrl;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Bean
    public DataSource primaryDataSource() {

        Properties dsProps = new Properties();
        dsProps.setProperty("url", dataSourceUrl);
        dsProps.setProperty("user", user);
        dsProps.setProperty("password", password);

        Properties configProps = new Properties();
        configProps.setProperty("driverClassName", driverClassName);
        configProps.setProperty("jdbcUrl", dataSourceUrl);

        HikariConfig hc = new HikariConfig(configProps);
        hc.setDataSourceProperties(dsProps);
        return new HikariDataSource(hc);
    }

}