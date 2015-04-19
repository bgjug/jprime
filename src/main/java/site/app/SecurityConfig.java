package site.app;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	public ApplicationContext context;

	@Autowired
    private Environment environment;
	
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
                .withUser("admin").password(environment.getProperty("admin.password")).roles("ADMIN","USER");
//	        auth.authenticationProvider(new AuthenticationProvider() {
//	            @Override
//	            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//	                String email = (String) authentication.getPrincipal();
//	                String providedPassword = (String) authentication.getCredentials();
//	                User user = userService.findAndAuthenticateUser(email, providedPassword);
//	                if (user == null) {
//	                    throw new BadCredentialsException("Username/Password does not match for " + authentication.getPrincipal());
//	                }
//
//	                return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
//	            }
//
//	            @Override
//	            public boolean supports(Class<?> authentication) {
//	                return true;
//	            }
//	        });
    }

	 @Override
     protected void configure(final HttpSecurity http) throws Exception {
		 http
         .authorizeRequests()
            //TODO Mihail: "/" only works if tomcat/conf/web.xml has index.jsp commented as a welcome page
            //TODO if not, the controller will not be called and the jsp is not going to have any model object filled up.
           .antMatchers("/","/login","/about", "/nav/**", "/cfp", "/tickets", "/image/**").permitAll() // #4
           .antMatchers("/admin/**").hasRole("ADMIN") // #6
           .anyRequest().authenticated() // 7
           .and()
       .formLogin()  // #8
           .loginPage("/login") // #9
           .permitAll(); // #5
	 }
	 
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
