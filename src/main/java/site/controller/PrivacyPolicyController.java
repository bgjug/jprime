package site.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class PrivacyPolicyController {

    static final String PAGE_PRIVACY_POLICY_BG = "privacy-policy-bg.jsp";

    static final String PAGE_PRIVACY_POLICY_EN = "privacy-policy.jsp";

    private static final Logger logger = LoggerFactory.getLogger(PrivacyPolicyController.class);

    @GetMapping("/privacy-policy")
    public String getPrivacyPolicy(@RequestParam(value = "bg", required = false) String bgLang,
                                   @RequestParam(value = "en", required = false) String enLang) {
        if (bgLang != null) {
            return PAGE_PRIVACY_POLICY_BG;
        }
        return PAGE_PRIVACY_POLICY_EN;
    }
}
