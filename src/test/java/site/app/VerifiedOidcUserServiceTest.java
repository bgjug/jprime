package site.app;

import java.time.Instant;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.core.authority.AuthorityUtils;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VerifiedOidcUserServiceTest {

    private static OidcUser user(Map<String, Object> extra) {
        var claims = new java.util.HashMap<String, Object>(Map.of("sub", "1", "email", "a@x.io"));
        claims.putAll(extra);
        var token = new OidcIdToken("t", Instant.now(), Instant.now().plusSeconds(60), claims);
        return new DefaultOidcUser(AuthorityUtils.NO_AUTHORITIES, token);
    }

    @Test
    void passesWhenVerified() {
        assertThatCode(() -> VerifiedOidcUserService.requireVerified(user(Map.of("email_verified", true))))
            .doesNotThrowAnyException();
    }

    @Test
    void rejectsFalseAndMissing() {
        assertThatThrownBy(() -> VerifiedOidcUserService.requireVerified(user(Map.of("email_verified", false))))
            .isInstanceOf(OAuth2AuthenticationException.class);
        assertThatThrownBy(() -> VerifiedOidcUserService.requireVerified(user(Map.of())))
            .isInstanceOf(OAuth2AuthenticationException.class);
    }
}
