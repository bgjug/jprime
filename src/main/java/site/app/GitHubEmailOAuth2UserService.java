package site.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class GitHubEmailOAuth2UserService extends DefaultOAuth2UserService {

    private final RestClient client;

    public GitHubEmailOAuth2UserService(RestClient.Builder builder) {
        this.client = builder.build();
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(request);
        if (!"github".equals(request.getClientRegistration().getRegistrationId())) {
            return user;
        }
        Map<String, Object> attributes = new HashMap<>(user.getAttributes());
        attributes.put("email", primaryVerifiedEmail(request.getAccessToken().getTokenValue()));
        return new DefaultOAuth2User(user.getAuthorities(), attributes, "id");
    }

    String primaryVerifiedEmail(String accessToken) {
        List<Map<String, Object>> emails;
        try {
            emails = client.get().uri("https://api.github.com/user/emails")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve().body(new ParameterizedTypeReference<>() { });
        } catch (RestClientException e) {
            throw new OAuth2AuthenticationException(new OAuth2Error("email_lookup_failed"),
                "Could not read your GitHub email addresses", e);
        }
        return (emails == null ? List.<Map<String, Object>>of() : emails).stream()
            .filter(e -> Boolean.TRUE.equals(e.get("primary")) && Boolean.TRUE.equals(e.get("verified")))
            .map(e -> (String) e.get("email"))
            .findFirst()
            .orElseThrow(() -> new OAuth2AuthenticationException(new OAuth2Error("unverified_email"),
                "Your GitHub account has no verified email"));
    }
}
