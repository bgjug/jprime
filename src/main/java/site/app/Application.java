package site.app;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
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
@ComponentScan(basePackages = "site")
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = { "site.repository" } )
@EntityScan(basePackages="site.model")
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
	}

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