package site.controller.util;

import site.model.Speaker;

/**
 * @author Ivan St. Ivanov
 */
public class Utils {

    public static Speaker fixTwitterHandle(Speaker speaker) {
        String twitterHandle = speaker.getTwitter();
        if (twitterHandle != null && twitterHandle.startsWith("@")) {
            speaker.setTwitter(twitterHandle.substring(1));
        }

        return speaker;
    }

}
