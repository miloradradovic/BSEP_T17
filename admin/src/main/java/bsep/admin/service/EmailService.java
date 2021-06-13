package bsep.admin.service;

import bsep.admin.utils.CryptingUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

@Service
public class EmailService {

    @Autowired
    CryptingUtil cryptingUtil;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Environment env;

    private static Logger logger = LogManager.getLogger(EmailService.class);

    @Async
    public void sendEmail(String email, String crypted){
        logger.info("Attempting to send verification message via email.");
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setFrom(env.getProperty("spring.mail.username"));
        mail.setSubject("Please verify your email address");
        mail.setText("An attempt to create a certificate request was made by an account with your email.\n" +
                "If that is you, verify by clicking the link below. Otherwise, ignore this email.\n" +
                "https://localhost:8084/authentication/verify-certificate-request/" + crypted);
        javaMailSender.send(mail);
        logger.info("Successfully sent verification message via email.");
    }

    @Async
    public void sendCertificate(String email, String fullName, byte[] cer) throws MessagingException {
        logger.info("Attempting to send certificate via email.");
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "utf-8");
        helper.setTo(email);
        helper.setFrom(env.getProperty("spring.mail.username"));
        helper.setSubject("Requested certificate");
        helper.setText("Certificate you requested is in attachment.");

        ByteArrayDataSource byteArrayDataSourceHtml = new ByteArrayDataSource(cer, "application/x-x509-ca-cert");
        helper.addAttachment(fullName + ".cer", byteArrayDataSourceHtml);
        javaMailSender.send(mimeMessage);
        logger.info("Successfully sent certificate via email.");
    }
}
