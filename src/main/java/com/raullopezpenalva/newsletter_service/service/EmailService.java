package com.raullopezpenalva.newsletter_service.service;

import com.raullopezpenalva.newsletter_service.model.Subscriber;
import com.raullopezpenalva.newsletter_service.model.VerificationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Autowired
    private final JavaMailSender mailSender;
    
    @Value("${app.frontend.base-url}")
    private String frontendBaseUrl;

    public void sendVerificationEmail(String toEmail, VerificationToken token) {
        String to = toEmail;
        String subject = "Please verify your email address";
        String verificationUrl = String.format("%s/verify?token=%s", frontendBaseUrl, token.getId());
        String text = String.format("Click the following link to verify your email address: %s", verificationUrl);

        try {
            var message = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true); // true indicates HTML content
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception (e.g., log it)
        }
    }
}