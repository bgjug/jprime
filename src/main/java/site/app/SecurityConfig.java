package site.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.util.StringUtils;
import site.model.User;
import site.repository.SpeakerRepository;
import site.repository.UserRepository;

import java.io.IOException;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    public ApplicationContext context;

    @Autowired
    private Environment environment;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SpeakerRepository speakerRepository;
    
    @Override
    public void configure(final WebSecurity web) {
        web
                .ignoring()
                .antMatchers("/asset/**")
                .antMatchers("/assets/**")
                .antMatchers("/css/**")
                .antMatchers("/fonts/**")
                .antMatchers("/images/**")
                .antMatchers("/js/**");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
	        auth.authenticationProvider(new AuthenticationProvider() {
	            @Override
	            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
	                String email = (String) authentication.getPrincipal();
	                String providedPassword = (String) authentication.getCredentials();
	                
	                //admin hack
	                if(email.equals("admin") && providedPassword.equals(environment.getProperty("admin.password"))){
	                	return new UsernamePasswordAuthenticationToken(email, providedPassword, Collections.singleton(new SimpleGrantedAuthority("ADMIN")));
	                }
	                
	                User user = userRepository.findUserByEmail(email);
	                if (user == null) {
                        user = speakerRepository.findByEmail(email);
                        if (StringUtils.isEmpty(user.getPassword())) {
                            user = null;
                        }
                    }

	                if (user == null || !passwordEncoder().matches(providedPassword, user.getPassword())) {
	                    throw new BadCredentialsException("Username/Password does not match for " + authentication.getPrincipal());
	                }
	                
	                return new UsernamePasswordAuthenticationToken(email, providedPassword, Collections.singleton(new SimpleGrantedAuthority("USER")));
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
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .and()
                .csrf().disable()
                .authorizeRequests()
                        //TODO Mihail: "/" only works if tomcat/conf/web.xml has index.jsp commented as a welcome page
                        //TODO if not, the controller will not be called and the jsp is not going to have any model object filled up.
                .antMatchers("/", "/login", "/about", "/nav/**", "/cfp", "/cfp-thank-you",  "/privacy-policy", "/signup", "/resetPassword","/createNewPassword", "/successfulPasswordChange", "/image/**", "/tickets/**", "/team","/venue","/speaker/**","/speakers", "/agenda/**", "/404", "/captcha-image", "/pwa", "/pwa/**", "/qr/**").permitAll() // #4
                .antMatchers(HttpMethod.GET, "/halls", "/halls/**", "/sessions", "/sessions/**", "/submissions", "/submissions/**", "/sw.js", "/manifest.json").permitAll()
                .antMatchers("/admin/**").hasAuthority("ADMIN") // #6
                .antMatchers("/raffle/**").hasAuthority("ADMIN") // #7
                .antMatchers("/user/**").hasAuthority("USER") //will contain schedule and etc
                .anyRequest().authenticated() // 8
                .and()
                .formLogin().successHandler(SecurityConfig::redirectToAdmin) // #9
                .loginPage("/login") // #10
                .permitAll(); // #5
    }

    private static void redirectToAdmin(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {
        if (response.isCommitted()) {
            return;
        }

        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(p-> "ADMIN".equals(p.getAuthority()));
        if (isAdmin) {
            new DefaultRedirectStrategy().sendRedirect(request, response, "/admin");
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
