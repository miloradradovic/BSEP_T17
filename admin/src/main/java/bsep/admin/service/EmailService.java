package bsep.admin.service;

import bsep.admin.utils.CryptingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    CryptingUtil cryptingUtil;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Environment env;

    @Async
    public void sendEmail(String email, String crypted){
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setFrom(env.getProperty("spring.mail.username"));
        mail.setSubject("Please verify your email address");
        mail.setText("An attempt to create a certificate request was made by an account with your email.\n" +
                "If that is you, verify by clicking the link below. Otherwise, ignore this email.\n" +
                "http://localhost:8080/auth/verify-certificate-request/" + crypted);
        javaMailSender.send(mail);
    }
}
