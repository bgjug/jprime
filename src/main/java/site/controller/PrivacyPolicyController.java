package site.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PrivacyPolicyController {

    static final String PAGE_PRIVACY_POLICY_BG = "privacy-policy-bg";

    static final String PAGE_PRIVACY_POLICY_EN = "privacy-policy";

    private static final Logger logger = LogManager.getLogger(NavController.class);

    @GetMapping("/privacy-policy")
    public String getPrivacyPolicy(@RequestParam(value = "bg", required = false) String bgLang,
                                   @RequestParam(value = "en", required = false) String enLang) {
        if (bgLang != null) {
            return PAGE_PRIVACY_POLICY_BG;
        }
        return PAGE_PRIVACY_POLICY_EN;
    }
}
