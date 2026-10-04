package site.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;

import site.model.Speaker;

public record ProfileForm(@NotBlank @Size(max = 255) String firstName, @NotBlank @Size(max = 255) String lastName,
                          @Size(max = 255) String headline,
                          @NotBlank @Size(max = 3000) String bio, @Size(max = 255) String twitter, @Size(max = 255) String bsky) {

    static ProfileForm of(Speaker s) {
        return new ProfileForm(s.getFirstName(), s.getLastName(), s.getHeadline(), s.getBio(), s.getTwitter(),
            s.getBsky());
    }

    void applyTo(Speaker s) {
        s.setFirstName(firstName);
        s.setLastName(lastName);
        s.setHeadline(headline);
        s.setBio(bio);
        s.setTwitter(StringUtils.trimToNull(twitter));
        AbstractCfpController.fixTwitterHandle(s);
        s.setBsky(bsky);
    }
}
