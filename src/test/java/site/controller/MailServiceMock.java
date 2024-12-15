package site.controller;

import java.util.ArrayList;
import java.util.List;

import jakarta.mail.MessagingException;

import site.facade.MailService;

/**
 * @author Ivan St. Ivanov
 */
class MailServiceMock extends MailService {

    List<String> recipientAddresses = new ArrayList<>();

    String lastMessageText;
    
    @Override
    public void sendEmail(String to, String subject, String messageText)
            throws MessagingException {
        recipientAddresses.add(to);
        lastMessageText = messageText;
    }
}
