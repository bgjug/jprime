package site.app;

import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SecurityRulesTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void anonymousMyRedirectsToLogin() throws Exception {
        mockMvc.perform(get("/my")).andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void anonymousCfpRedirectsToLogin() throws Exception {
        mockMvc.perform(get("/cfp")).andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void cfpPostWithoutCsrfForbidden() throws Exception {
        mockMvc.perform(multipart("/cfp").with(user("ivan@jprime.io"))).andExpect(status().isForbidden());
    }

    @Test
    void adminPostStillNeedsNoCsrf() throws Exception {
        mockMvc.perform(multipart("/admin/speaker/add").file(new MockMultipartFile("file", new byte[0]))
                .with(user("admin").authorities(() -> "ADMIN"))
                .param("firstName", "Jane").param("lastName", "Smith").param("email", "jane@example.com")
                .param("headline", "Spring Expert").param("twitter", "@janesmith").param("bio", "Bio"))
            .andExpect(status().isFound());
    }

    @Test
    void getLogoutEndsAdminSession() throws Exception {
        var session = new MockHttpSession();
        session.setAttribute("SPRING_SECURITY_CONTEXT", new SecurityContextImpl(
            UsernamePasswordAuthenticationToken.authenticated("admin", "x", AuthorityUtils.createAuthorityList("ADMIN"))));
        mockMvc.perform(get("/logout").session(session));
        mockMvc.perform(get("/admin").session(session)).andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void loginPageShowsProviders() throws Exception {
        mockMvc.perform(get("/login")).andExpect(status().isOk()).andExpect(forwardedUrl("/login.jsp"));
        // MockMvc does not render JSPs, so check the template itself
        assertThat(Files.readString(Path.of("src/main/webapp/login.jsp")))
            .contains("/oauth2/authorization/google", "/oauth2/authorization/github");
    }
}
