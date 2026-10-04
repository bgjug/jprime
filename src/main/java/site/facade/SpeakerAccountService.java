package site.facade;

import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import site.model.Speaker;
import site.model.User;
import site.repository.SpeakerRepository;
import site.repository.UserRepository;

@Service
public class SpeakerAccountService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SpeakerRepository speakerRepository;

    public String email(Authentication auth) {
        String email;
        if (auth instanceof UsernamePasswordAuthenticationToken) {
            email = auth.getName();
        } else if (auth instanceof OAuth2AuthenticationToken oauth) {
            email = oauth.getPrincipal().getAttribute("email");
        } else {
            throw new IllegalStateException("Unsupported authentication: " + auth);
        }
        return email.trim();
    }

    @Transactional
    public Optional<Speaker> findOrPromote(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            return Optional.empty();
        }
        if (user instanceof Speaker speaker) {
            return Optional.of(speaker);
        }
        userRepository.convertToSpeaker(user.getId());
        entityManager.unwrap(org.hibernate.Session.class).evict(user);
        return Optional.ofNullable(speakerRepository.findByEmail(email));
    }

    @Transactional
    public Speaker currentSpeaker(Authentication auth) {
        String email = email(auth);
        return findOrPromote(email).orElseGet(() -> {
            if (!(auth instanceof OAuth2AuthenticationToken oauth)) {
                throw new AccessDeniedException("No speaker account");
            }
            Speaker speaker = new Speaker();
            speaker.setEmail(email);
            names(oauth.getPrincipal(), speaker);
            return speakerRepository.save(speaker);
        });
    }

    private static void names(OAuth2User principal, Speaker speaker) {
        if (principal.getAttribute("given_name") != null) {
            speaker.setFirstName(principal.getAttribute("given_name"));
            speaker.setLastName(principal.getAttribute("family_name"));
            return;
        }
        String name = StringUtils.trimToEmpty(principal.getAttribute("name"));
        if (name.isEmpty()) {
            speaker.setFirstName(principal.getAttribute("login"));
            speaker.setLastName("");
            return;
        }
        int i = name.lastIndexOf(' ');
        speaker.setFirstName(i < 0 ? name : name.substring(0, i));
        speaker.setLastName(i < 0 ? "" : name.substring(i + 1));
    }
}
