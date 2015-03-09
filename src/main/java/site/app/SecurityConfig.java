package site.app;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Resource
	public ApplicationContext context;

	@Override
	public void configure(final WebSecurity web) throws Exception {
		web
			.ignoring()
				.antMatchers("/asset/**")
				.antMatchers("/css/**")
				.antMatchers("/fonts/**")
				.antMatchers("/images/**")
				.antMatchers("/js/**");
	}
	
	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER")
                .and()
                .withUser("admin").password("password").roles("ADMIN","USER");
    }

	 @Override
     protected void configure(final HttpSecurity http) throws Exception {
		 http
         .authorizeRequests()
           .antMatchers("/","/login","/about").permitAll() // #4
           //.antMatchers("/admin/**").hasRole("ADMIN") // #6
           //.anyRequest().authenticated() // 7
           .and()
       .formLogin()  // #8
           .loginPage("/login") // #9
           .permitAll(); // #5
	 }
}
