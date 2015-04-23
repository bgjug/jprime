package site.facade;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author Ivan St. Ivanov
 */
@Service(MailFacade.NAME)
public class MailFacade {
    public static final String NAME = "mailFacade";

    @Value("${spring.mail.username}")
    private String emailAddress;

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String messageText) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setFrom(emailAddress);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(messageText, true);

        mailSender.send(mimeMessage);
    }

    public void sendInvoice(String to, String subject, String messageText, byte[] pdf) throws MessagingException {
        ByteArrayResource bais = null;
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setFrom(emailAddress);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(messageText, true);

        bais = new ByteArrayResource(pdf);
        helper.addAttachment("invoice.pdf", bais);

        mailSender.send(mimeMessage);
    }
}
