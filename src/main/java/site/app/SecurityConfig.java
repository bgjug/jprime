package site.app;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public final ApplicationContext context;

    public SecurityConfig(ApplicationContext context) {
        this.context = context;
    }

    @Bean
    SecurityFilterChain filterChain(final HttpSecurity http, final AuthenticationProvider provider,
        LoginSuccessHandler loginSuccessHandler, GitHubEmailOAuth2UserService gitHubService,
        VerifiedOidcUserService oidcService)
        throws Exception {
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
            .csrf(c -> c.requireCsrfProtectionMatcher(new OrRequestMatcher(
                PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/my/**"),
                PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/cfp"))))
            .cors(AbstractHttpConfigurer::disable)
            .authenticationProvider(provider)
            .authorizeHttpRequests(requests -> {
                requests.requestMatchers("/app/**").permitAll();

                requests.requestMatchers("/my/**", "/cfp").authenticated();

                requests.requestMatchers("/admin/**", "/raffle/**", "/api/**", "/user/**")
                    .hasAuthority("ADMIN");

                requests.requestMatchers(HttpMethod.GET, "/halls/**", "/sessions/**", "/submissions/**")
                    .permitAll();

                requests.requestMatchers("/login", "/perform-login", "/signup", "/resetPassword", "/createNewPassword",
                    "/successfulPasswordChange", "/assets/**", "/css/**", "/fonts/**", "/images/**", "/js/**",
                    "/nav/**", "/image/**", "/tickets/**", "/speaker/**", "/agenda/**", "/pwa/**", "/qr/**",
                    "/*").permitAll();

            });

        http.formLogin(loginForm -> loginForm.successHandler(loginSuccessHandler)
            .loginPage("/login")
            .permitAll());
        http.logout(l -> l.logoutRequestMatcher(PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.GET, "/logout")));
        http.oauth2Login(o -> o.loginPage("/login")
            .successHandler(loginSuccessHandler)
            .userInfoEndpoint(u -> u.userService(gitHubService).oidcUserService(oidcService)));
        return http.build(); // #5
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
