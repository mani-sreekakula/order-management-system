package io.pivotal.microservices.mailing;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * Client to send email from the Spring Java Mail
 * @author msreekak
 *
 */
@Component
public class EmailSender {
 
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSender.class);
 
    @Autowired
    private JavaMailSender javaMailSender;
 
   public boolean sendMail(String to, String subject, String text) {
        try {
            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);
            javaMailSender.send(mail);
            LOGGER.info("Send email '{}' to: {}", subject, to);
            return true;
        } catch (Exception e) {
        	e.printStackTrace();
            LOGGER.error(String.format("Problem with sending email to: {}, error message: {}", to, e.getMessage()));
            return false;
        }
    }
}