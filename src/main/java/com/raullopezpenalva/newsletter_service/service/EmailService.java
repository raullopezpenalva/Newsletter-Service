package com.raullopezpenalva.newsletter_service.service;

import com.raullopezpenalva.newsletter_service.model.VerificationToken;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Service
public class EmailService {

    @Autowired
    private final JavaMailSender mailSender = null;

    @Value("${app.frontendBaseUrl}")
    private String frontendBaseUrl;

    // Send verification email
    public void sendVerificationEmail(String toEmail, VerificationToken token) {
        String to = toEmail;
        String subject = "Please verify your email address";
        String verificationUrl = String.format("%s/verify?token=%s", frontendBaseUrl, token.getToken());
        String text = String.format("Click the following link to verify your email address: %s", verificationUrl);

        try {
            var message = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, false); // true indicates HTML content
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception (e.g., log it)
        }
    }

    // Generate unsubscribe link
    public String generateUnsubscribeLink(VerificationToken token) {
        return String.format("%s/unsubscribe?token=%s", frontendBaseUrl, token.getToken());
    }
}