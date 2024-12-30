package site.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import site.facade.MailService;

@Service
@Primary
public class MailServiceMock extends MailService {

    List<String> recipientAddresses = new ArrayList<>();

    String lastMessageText;

    @Override
    public void sendEmail(String to, String subject, String messageText) {
        recipientAddresses.add(to);
        lastMessageText = messageText;
    }
}

