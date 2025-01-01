package site.facade;

import java.io.UnsupportedEncodingException;
import java.util.stream.Stream;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeUtility;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import site.model.JPrimeException;

/**
 * @author Ivan St. Ivanov
 */
@Service
public class MailService {
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

    public void sendEmail(String to, String subject, String messageText, byte[] pdf, String pdfName) throws MessagingException {
        sendEmail(to, subject, messageText, new Attachment(pdf, pdfName, "utf-8", false, "application/pdf"));
    }

    public void sendEmail(String to, String subject, String messageText, Attachment ... attachments) throws MessagingException {
        System.setProperty("mail.mime.splitlongparameters", "false");
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        String emailTo = StringUtils.isEmpty(testEmailAddress) ? to: testEmailAddress;

        helper.setFrom(emailAddress);
        helper.setTo(emailTo);
        helper.setSubject(subject);
        helper.setText(messageText, true);

        Stream.of(attachments).filter(attachment -> attachment.isInline).forEach(attachment -> {
                try {
                    helper.addInline(attachment.name, new ByteArrayResource(attachment.data), attachment.type);
                } catch (MessagingException e) {
                    throw new JPrimeException(e);
                }
            }
        );

        Stream.of(attachments).filter(attachment -> !attachment.isInline).forEach(attachment -> {
            ByteArrayResource bais = new ByteArrayResource(attachment.data);
            try {
                helper.addAttachment(MimeUtility.encodeWord(attachment.name, attachment.charset, "Q"), bais);
            } catch (UnsupportedEncodingException e) {
                logger.error(e.getMessage());
            } catch (MessagingException e) {
                throw new JPrimeException(e);
            }
        });

        mailSender.send(mimeMessage);
    }

}
