package com.example.ProjectManagementSystem.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.util.Properties;
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendEmailWithToken(String userEmail, String link) throws MessagingException {
        // Force the STARTTLS settings programmatically
        JavaMailSenderImpl mailSenderImpl = (JavaMailSenderImpl) javaMailSender;
        Properties props = mailSenderImpl.getJavaMailProperties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        helper.setSubject("Project Team Invitation");
        helper.setText("Click the link to join the team: " + link, true);
        helper.setTo(userEmail);
        helper.setFrom("ssarfrazsahir@gmail.com"); // Must match your username

        javaMailSender.send(mimeMessage);
        System.out.println("Real-time email sent successfully to: " + userEmail);
    }
}