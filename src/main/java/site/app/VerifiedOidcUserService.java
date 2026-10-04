package site.app;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

@Component
public class VerifiedOidcUserService extends OidcUserService {

    @Override
    public OidcUser loadUser(OidcUserRequest request) throws OAuth2AuthenticationException {
        OidcUser user = super.loadUser(request);
        requireVerified(user);
        return user;
    }

    static void requireVerified(OidcUser user) {
        if (!Boolean.TRUE.equals(user.getEmailVerified())) {
            throw new OAuth2AuthenticationException(new OAuth2Error("unverified_email"),
                "Your Google account has no verified email");
        }
    }
}
