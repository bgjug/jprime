package site.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import site.facade.Attachment;
import site.facade.MailService;

@Service
@Primary
public class MailServiceMock extends MailService {

    private final List<String> recipientAddresses = new ArrayList<>();

    private final List<String> messageTexts = new ArrayList<>();

    private final List<String> subjects = new ArrayList<>();

    private final Map<String, List<Attachment>> attachments = new HashMap<>();

    public Map<String, List<Attachment>> getAttachments() {
        return attachments;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public List<String> getRecipientAddresses() {
        return recipientAddresses;
    }

    public List<String> getMessageTexts() {
        return messageTexts;
    }

    public void clear() {
        recipientAddresses.clear();
        messageTexts.clear();
        subjects.clear();
        attachments.clear();
    }

    @Override
    public void sendEmail(String to, String subject, String messageText) {
        recipientAddresses.add(to);
        messageTexts.add(messageText);
        subjects.add(subject);
        attachments.put(to + subject, List.of());
    }

    @Override
    public void sendEmail(String to, String subject, String messageText, Attachment... attachments) {
        recipientAddresses.add(to);
        messageTexts.add(messageText);
        subjects.add(subject);
        this.attachments.put(to + subject, List.of(attachments));
    }
}

