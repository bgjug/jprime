package site.controller;

import site.model.Speaker;

/**
 * @author Ivan St. Ivanov
 */
public class Utils {

    static Speaker fixTwitterHandle(Speaker speaker) {
        String twitterHandle = speaker.getTwitter();
        if (twitterHandle != null && twitterHandle.startsWith("@")) {
            speaker.setTwitter(twitterHandle.substring(1));
        }

        return speaker;
    }

}
