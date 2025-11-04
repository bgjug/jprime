package site.app;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public final ApplicationContext context;

    public SecurityConfig(ApplicationContext context) {
        this.context = context;
    }

    @Bean
    SecurityFilterChain filterChain(final HttpSecurity http, final AuthenticationProvider provider)
        throws Exception {
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
            .csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .authenticationProvider(provider)
            .authorizeHttpRequests(requests -> {
                requests.requestMatchers("/admin/**", "/raffle/**", "/api/**", "/user/**")
                    .hasAuthority("ADMIN");

                requests.requestMatchers(HttpMethod.GET, "/halls/**", "/sessions/**", "/submissions/**")
                    .permitAll();

                requests.requestMatchers("/login", "/perform-login", "/signup", "/resetPassword", "/createNewPassword",
                    "/successfulPasswordChange", "/assets/**", "/css/**", "/fonts/**", "/images/**", "/js/**",
                    "/nav/**", "/image/**", "/tickets/**", "/speaker/**", "/agenda/**", "/pwa/**", "/qr/**",
                    "/*").permitAll();

            });

        http.formLogin(loginForm -> loginForm.successHandler(SecurityConfig::redirectToAdmin)
            .loginPage("/login")
            .permitAll());
        return http.build(); // #5
    }

    private static void redirectToAdmin(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {
        if (response.isCommitted()) {
            return;
        }

        boolean isAdmin =
            authentication.getAuthorities().stream().anyMatch(p -> "ADMIN".equals(p.getAuthority()));
        if (isAdmin) {
            new DefaultRedirectStrategy().sendRedirect(request, response, "/admin");
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
