package site.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import site.app.Application;
import site.facade.BranchService;
import site.facade.DefaultBranchUtil;
import site.model.User;
import site.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for AdminUserController.
 * Tests user management operations in admin panel.
 */
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Transactional
class AdminUserControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private UserRepository userRepository;

    private MockMvc mockMvc;
    private User testUser;

    @BeforeAll
    public static void beforeAll(@Autowired BranchService branchService) {
        DefaultBranchUtil.createDefaultBranch(branchService);
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser = userRepository.save(testUser);
    }

    @Test
    void getView_shouldReturnUserViewWithAllUsers() throws Exception {
        mockMvc.perform(get("/admin/user/view"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/user/view"))
            .andExpect(model().attributeExists("users"));
    }

    @Test
    void getAdd_shouldReturnFormWithEmptyUser() throws Exception {
        mockMvc.perform(get("/admin/user/add"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/user/edit"))
            .andExpect(model().attribute("user", is(new User())));
    }

    @Test
    void postAdd_shouldCreateNewUser() throws Exception {
        mockMvc.perform(post("/admin/user/add")
                .param("email", "newuser@example.com")
                .param("firstName", "New")
                .param("lastName", "User"))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/user/view"));

        List<User> savedUsers = userRepository.findByEmail("newuser@example.com");
        assertThat(savedUsers).isNotEmpty();
        User saved = savedUsers.get(0);
        assertThat(saved).isNotNull();
        assertThat(saved.getFirstName()).isEqualTo("New");
    }

    @Test
    void postAdd_withValidationErrors_shouldReturnEditForm() throws Exception {
        mockMvc.perform(post("/admin/user/add")
                .param("email", "")  // Empty email should fail validation
                .param("firstName", "")
                .param("lastName", ""))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/user/edit"));
    }
}
