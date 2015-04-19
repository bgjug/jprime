package site.controller.utils;

import org.junit.Test;
import site.model.Speaker;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static site.controller.util.Utils.fixTwitterHandle;

/**
 * @author Ivan St. Ivanov
 */
public class UtilsTest {


    @Test
    public void twitterHandleShouldNotStartWithAt() throws Exception {
        Speaker testSpeaker = new Speaker();
        testSpeaker.setTwitter("@speaker");
        fixTwitterHandle(testSpeaker);
        assertThat(testSpeaker.getTwitter(), is("speaker"));
    }

    @Test
    public void shouldSupportSpeakerWithNullTwitterHandle() throws Exception {
        Speaker testSpeaker = new Speaker();
        fixTwitterHandle(testSpeaker);
        assertThat(testSpeaker.getTwitter(), is(nullValue()));
    }
}
