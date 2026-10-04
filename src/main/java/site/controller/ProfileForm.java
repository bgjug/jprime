package site.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;

import site.model.Speaker;

public record ProfileForm(@NotBlank String firstName, @NotBlank String lastName, String headline,
                          @NotBlank @Size(max = 3000) String bio, String twitter, String bsky) {

    static ProfileForm of(Speaker s) {
        return new ProfileForm(s.getFirstName(), s.getLastName(), s.getHeadline(), s.getBio(), s.getTwitter(),
            s.getBsky());
    }

    void applyTo(Speaker s) {
        s.setFirstName(firstName);
        s.setLastName(lastName);
        s.setHeadline(headline);
        s.setBio(bio);
        s.setTwitter(StringUtils.removeStart(StringUtils.trimToNull(twitter), "@"));
        s.setBsky(bsky);
    }
}
