package site.controller;

import com.jayway.restassured.RestAssured;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import site.app.Application;
import site.repository.SpeakerRepository;
import site.repository.SubmissionRepository;

import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * @author Ivan St. Ivanov
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class CfpControllerTest {

    @Autowired
    @Qualifier(SpeakerRepository.NAME)
    private SpeakerRepository speakerRepository;


    @Autowired
    @Qualifier(SubmissionRepository.NAME)
    private SubmissionRepository submissionRepository;

    @Value("${local.server.port}")
    private int port;

    @Before
    public void setup() {
        RestAssured.port = port;
    }

    @Test
    public void shouldGetEmptySubmission() throws Exception {
        when().get("/cfp").then().statusCode(200).body("customer", notNullValue());
    }
}
