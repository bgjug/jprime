package site.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import site.model.SessionLevel;
import site.model.SessionType;
import site.model.Submission;

public record SubmissionForm(@NotBlank @Size(max = 255) String title, @NotBlank @Size(max = 10000) String description,
                             SessionLevel level, SessionType type) {

    static SubmissionForm of(Submission s) {
        return new SubmissionForm(s.getTitle(), s.getDescription(), s.getLevel(), s.getType());
    }

    void applyTo(Submission s) {
        s.setTitle(title);
        s.setDescription(description);
        s.setLevel(level);
        s.setType(type);
    }
}
