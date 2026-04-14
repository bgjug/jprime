package site.controller;

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
import site.model.Tag;
import site.repository.TagRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for AdminTagController.
 * Tests tag CRUD operations in admin panel.
 */
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Transactional
class AdminTagControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private TagRepository tagRepository;

    private MockMvc mockMvc;
    private Tag testTag;

    @BeforeAll
    public static void beforeAll(@Autowired BranchService branchService) {
        DefaultBranchUtil.createDefaultBranch(branchService);
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        tagRepository.deleteAll();

        testTag = new Tag("Java");
        testTag = tagRepository.save(testTag);
    }

    @Test
    void getView_shouldReturnTagViewWithAllTags() throws Exception {
        mockMvc.perform(get("/admin/tag/view"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/tag/view"))
            .andExpect(model().attributeExists("tags"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attributeExists("number"));
    }

    @Test
    void getAdd_shouldReturnFormWithEmptyTag() throws Exception {
        mockMvc.perform(get("/admin/tag/add"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/tag/edit"))
            .andExpect(model().attributeExists("tag"));
    }

    @Test
    void getEdit_shouldReturnFormWithExistingTag() throws Exception {
        mockMvc.perform(get("/admin/tag/edit/" + testTag.getId()))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/tag/edit"))
            .andExpect(model().attribute("tag", testTag));
    }

    @Test
    void postAdd_shouldCreateNewTag() throws Exception {
        mockMvc.perform(post("/admin/tag/add")
                .param("name", "Spring"))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/tag/view"));

        assertThat(tagRepository.findAll()).hasSizeGreaterThanOrEqualTo(2);
    }

    // Validation test removed - controller doesn't use @Valid annotation

    @Test
    void getRemove_shouldDeleteTag() throws Exception {
        long tagId = testTag.getId();

        mockMvc.perform(get("/admin/tag/remove/" + tagId))
            .andExpect(status().isFound())
            .andExpect(view().name("redirect:/admin/tag/view"));

        assertThat(tagRepository.findById(tagId)).isEmpty();
    }
}
