package site.controller;

import org.junit.jupiter.api.Test;
import site.model.Speaker;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author Ivan St. Ivanov
 */
class CommonCfpControllerTest {

    private static class TestCfpController extends AbstractCfpController {}

    private TestCfpController testCfpController = new TestCfpController();

    @Test
    void twitterHandleShouldNotStartWithAt() throws Exception {
        Speaker testSpeaker = new Speaker();
        testSpeaker.setTwitter("@speaker");
        testCfpController.fixTwitterHandle(testSpeaker);
        assertThat(testSpeaker.getTwitter(), is("speaker"));
    }

    @Test
    void shouldSupportSpeakerWithNullTwitterHandle() throws Exception {
        Speaker testSpeaker = new Speaker();
        testCfpController.fixTwitterHandle(testSpeaker);
        assertThat(testSpeaker.getTwitter(), is(nullValue()));
    }

}
