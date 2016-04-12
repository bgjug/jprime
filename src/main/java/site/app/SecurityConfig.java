package site.app;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import site.model.Role;
import site.model.User;
import site.repository.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    public ApplicationContext context;

    @Autowired
    private Environment environment;

    @Autowired
    private UserRepository userRepository;
    
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
//        auth
//                .inMemoryAuthentication()
//                .withUser("user").password("password").roles("USER")
//                .and()
//                .withUser("admin").password(environment.getProperty("admin.password")).roles("ADMIN", "USER");
	        auth.authenticationProvider(new AuthenticationProvider() {
	            @Override
	            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
	                String email = (String) authentication.getPrincipal();
	                String providedPassword = (String) authentication.getCredentials();
	                
	                //admin hack
	                if(email.equals("admin") && providedPassword.equals(environment.getProperty("admin.password"))) {
	                	return new UsernamePasswordAuthenticationToken(email, providedPassword, Collections.singleton(new SimpleGrantedAuthority(Role.ADMIN_NAME)));
	                }
	                
	                User user = userRepository.findUserByEmail(email);
	                
	                if (user == null || !passwordEncoder().matches(providedPassword, user.getPassword())) {
	                    throw new BadCredentialsException("Username/Password does not match for " + authentication.getPrincipal());
	                }
	                // @Trifon - Read User roles from DB
	                // return new UsernamePasswordAuthenticationToken(email, providedPassword, Collections.singleton(new SimpleGrantedAuthority("USER")));
	                return new UsernamePasswordAuthenticationToken(email, providedPassword, user.getRoles());
	            }

	            @Override
	            public boolean supports(Class<?> authentication) {
	                return true;
	            }
	        });
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                        //TODO Mihail: "/" only works if tomcat/conf/web.xml has index.jsp commented as a welcome page
                        //TODO if not, the controller will not be called and the jsp is not going to have any model object filled up.
                .antMatchers("/", "/login", "/about", "/nav/**", "/cfp", "/signup", "/resetPassword","/createNewPassword", "/successfulPasswordChange", "/image/**", "/tickets/**", "/team","/venue", "/404").permitAll() // #4
                .antMatchers(HttpMethod.GET, "/halls", "/halls/**", "/sessions", "/sessions/**", "/submissions", "/submissions/**").permitAll()
                .antMatchers("/admin/**").hasAuthority("ADMIN") // #6
                .antMatchers("/user/**").hasAuthority("USER") //will contain schedule and etc
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
