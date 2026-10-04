package site.facade;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.transaction.annotation.Transactional;

import site.app.Application;
import site.model.Speaker;
import site.model.User;
import site.repository.SpeakerRepository;
import site.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = Application.class)
@Transactional
class SpeakerAccountServiceTest {

    @Autowired
    private SpeakerAccountService svc;

    @Autowired
    private SpeakerRepository speakerRepository;

    @Autowired
    private UserRepository userRepository;

    private static Authentication form(String email) {
        return new UsernamePasswordAuthenticationToken(email, "x", List.of());
    }

    private static Authentication github(String email, String name) {
        return new OAuth2AuthenticationToken(new DefaultOAuth2User(List.of(),
            Map.of("id", 1, "email", email, "name", name, "login", "ada"), "id"), List.of(), "github");
    }

    @Test
    void existingSpeakerReturned() {
        Speaker s = new Speaker();
        s.setEmail("ivan@jprime.io");
        s.setFirstName("Ivan");
        s.setLastName("I");
        s = speakerRepository.save(s);
        assertEquals(s.getId(), svc.currentSpeaker(form("ivan@jprime.io")).getId());
    }

    @Test
    void plainUserPromoted() {
        User u = new User();
        u.setEmail("bob@x.io");
        u.setFirstName("Bob");
        u.setLastName("B");
        u = userRepository.saveAndFlush(u);
        Speaker s = svc.currentSpeaker(form("bob@x.io"));
        assertInstanceOf(Speaker.class, s);
        assertEquals(u.getId(), s.getId());
    }

    @Test
    void unknownOAuthEmailCreatesSpeaker() {
        Speaker s = svc.currentSpeaker(github("ada@x.io", "Ada Lovelace"));
        assertEquals("Ada", s.getFirstName());
        assertEquals("Lovelace", s.getLastName());
        assertNull(s.getPassword());
        assertNotNull(speakerRepository.findByEmail("ada@x.io"));
    }

    @Test
    void githubWithoutNameUsesLogin() {
        Speaker s = svc.currentSpeaker(github("ada@x.io", ""));
        assertEquals("ada", s.getFirstName());
        assertEquals("", s.getLastName());
    }

    @Test
    void adminFormLoginWithoutRowDenied() {
        assertThrows(AccessDeniedException.class, () -> svc.currentSpeaker(form("admin")));
    }

    @Test
    void findOrPromoteUnknownIsEmpty() {
        assertTrue(svc.findOrPromote("nobody@x.io").isEmpty());
    }
}
