package site.app;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class GitHubEmailOAuth2UserServiceTest {

    private final RestClient.Builder builder = RestClient.builder();
    private final MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
    private final GitHubEmailOAuth2UserService service = new GitHubEmailOAuth2UserService(builder);

    private void respond(String body) {
        server.expect(requestTo("https://api.github.com/user/emails"))
            .andExpect(header("Authorization", "Bearer tok"))
            .andRespond(withSuccess(body, MediaType.APPLICATION_JSON));
    }

    @Test
    void picksPrimaryVerified() {
        respond("[{\"email\":\"a@x.io\",\"primary\":false,\"verified\":true},"
            + "{\"email\":\"b@x.io\",\"primary\":true,\"verified\":true}]");
        assertThat(service.primaryVerifiedEmail("tok")).isEqualTo("b@x.io");
    }

    @Test
    void rejectsUnverifiedPrimary() {
        respond("[{\"email\":\"b@x.io\",\"primary\":true,\"verified\":false}]");
        assertThatThrownBy(() -> service.primaryVerifiedEmail("tok"))
            .isInstanceOfSatisfying(OAuth2AuthenticationException.class,
                e -> assertThat(e.getError().getErrorCode()).isEqualTo("unverified_email"));
    }
}
