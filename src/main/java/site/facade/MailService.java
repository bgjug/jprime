package site.facade;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

/**
 * @author Ivan St. Ivanov
 */
@Service(MailService.NAME)
public class MailService {
    public static final String NAME = "mailFacade";

    private static final Logger logger = LogManager.getLogger(MailService.class);

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

    public void sendInvoice(String to, String subject, String messageText, byte[] pdf, String pdfFilename) throws MessagingException {
        System.setProperty("mail.mime.splitlongparameters", "false");
        ByteArrayResource bais = null;
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setFrom(emailAddress);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(messageText, true);

        bais = new ByteArrayResource(pdf);
        try {
            helper.addAttachment(MimeUtility.encodeWord(pdfFilename), bais);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }

        mailSender.send(mimeMessage);
    }
}
