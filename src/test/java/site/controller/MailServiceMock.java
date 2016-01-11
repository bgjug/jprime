package site.controller;

import site.facade.MailService;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan St. Ivanov
 */
class MailServiceMock extends MailService {

    List<String> recipientAddresses = new ArrayList<>();

    @Override
    public void sendEmail(String to, String subject, String messageText)
            throws MessagingException {
        recipientAddresses.add(to);
    }
}
