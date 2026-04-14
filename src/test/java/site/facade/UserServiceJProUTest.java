package site.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import site.app.Application;
import site.model.Visitor;
import site.model.VisitorJPro;
import site.repository.VisitorJProRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Transactional
class UserServiceJProUTest {

    @Autowired
    private UserServiceJPro userServiceJPro;

    @Autowired
    private VisitorJProRepository visitorJProRepository;

    @BeforeEach
    void setupVisitor() {
        visitorJProRepository.save(new VisitorJPro("Alice Smith", "alice@test.com", "TestCorp"));
    }

    @Test
    void setPresentByNameIgnoreCaseAndEmailIgnoreCase_returnsTrue_whenMatchFound() {
        Visitor example = new Visitor();
        example.setName("ALICE SMITH");
        example.setEmail("ALICE@TEST.COM");

        boolean result = userServiceJPro.setPresentByNameIgnoreCaseAndEmailIgnoreCase(example);

        assertThat(result).isTrue();
        assertThat(
            visitorJProRepository.findByNameIgnoreCaseAndEmailIgnoreCase("Alice Smith", "alice@test.com")
                .get(0)
                .isPresent()).isTrue();
    }

    @Test
    void setPresentByNameIgnoreCaseAndEmailIgnoreCase_returnsFalse_whenNoMatch() {
        Visitor example = new Visitor();
        example.setName("Unknown Person");
        example.setEmail("unknown@test.com");

        boolean result = userServiceJPro.setPresentByNameIgnoreCaseAndEmailIgnoreCase(example);

        assertThat(result).isFalse();
    }

    @Test
    void setJProPresentByNameIgnoreCase_returnsTrue_whenMatchFound() {
        Visitor example = new Visitor();
        example.setName("alice smith");

        boolean result = userServiceJPro.setJProPresentByNameIgnoreCase(example);

        assertThat(result).isTrue();
        assertThat(visitorJProRepository.findByNameIgnoreCase("Alice Smith").get(0).isPresent()).isTrue();
    }

    @Test
    void setJProPresentByNameIgnoreCase_returnsFalse_whenNoMatch() {
        Visitor example = new Visitor();
        example.setName("Ghost User");

        boolean result = userServiceJPro.setJProPresentByNameIgnoreCase(example);

        assertThat(result).isFalse();
    }
}
