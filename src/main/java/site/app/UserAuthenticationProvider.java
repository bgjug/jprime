package site.app;

import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import site.model.User;
import site.repository.SpeakerRepository;
import site.repository.UserRepository;

@Service
public class UserAuthenticationProvider implements AuthenticationProvider {

    private final Environment environment;

    private final UserRepository userRepository;

    private final SpeakerRepository speakerRepository;

    private final PasswordEncoder passwordEncoder;

    public UserAuthenticationProvider(PasswordEncoder passwordEncoder, Environment environment,
        UserRepository userRepository, SpeakerRepository speakerRepository) {
        this.passwordEncoder = passwordEncoder;
        this.environment = environment;
        this.userRepository = userRepository;
        this.speakerRepository = speakerRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = (String) authentication.getPrincipal();
        String providedPassword = (String) authentication.getCredentials();

        //admin hack
        if ("admin".equals(email) && providedPassword.equals(environment.getProperty("admin.password"))) {
            return new UsernamePasswordAuthenticationToken(email, providedPassword,
                Collections.singleton(new SimpleGrantedAuthority("ADMIN")));
        }

        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            user = speakerRepository.findByEmail(email);
            if (StringUtils.isEmpty(user.getPassword())) {
                user = null;
            }
        }

        if (user == null || !passwordEncoder.matches(providedPassword, user.getPassword())) {
            throw new BadCredentialsException(
                "Username/Password does not match for " + authentication.getPrincipal());
        }

        return new UsernamePasswordAuthenticationToken(email, providedPassword,
            Collections.singleton(new SimpleGrantedAuthority("USER")));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}

