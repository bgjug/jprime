package site.facade;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang3.StringUtils;
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

    @Value("${test.email.address:}")
    private String testEmailAddress;

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String messageText) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        String emailTo = StringUtils.isEmpty(testEmailAddress) ? to: testEmailAddress;

        helper.setFrom(emailAddress);
        helper.setTo(emailTo);
        helper.setSubject(subject);
        helper.setText(messageText, true);

        mailSender.send(mimeMessage);
    }

    public void sendEmail(String to, String subject, String messageText, byte[] pdf, String pdfFilename) throws MessagingException {
        System.setProperty("mail.mime.splitlongparameters", "false");
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        String emailTo = StringUtils.isEmpty(testEmailAddress) ? to: testEmailAddress;

        helper.setFrom(emailAddress);
        helper.setTo(emailTo);
        helper.setSubject(subject);
        helper.setText(messageText, true);

        ByteArrayResource bais = new ByteArrayResource(pdf);
        try {
            helper.addAttachment(MimeUtility.encodeWord(pdfFilename, "UTF-8", "Q"), bais);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }

        mailSender.send(mimeMessage);
    }

}
