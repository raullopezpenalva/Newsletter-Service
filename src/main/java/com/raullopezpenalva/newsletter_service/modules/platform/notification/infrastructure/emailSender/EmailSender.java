package com.raullopezpenalva.newsletter_service.modules.platform.notification.infrastructure.emailSender;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.raullopezpenalva.newsletter_service.modules.platform.notification.application.model.NotificationSubscribeConfirmation;

@Component
public class EmailSender {

    private final JavaMailSender mailSender;

    @Value("${app.frontendBaseUrl}")
    private String frontendBaseUrl;

    public EmailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSubscribeConfirmationEmail(NotificationSubscribeConfirmation notification) {
        try {
            var message = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message, true);
            helper.setTo(notification.getEmail());
            helper.setSubject("Please verify your email address");
            String verificationUrl = String.format("%s/verify?token=%s", frontendBaseUrl, notification.getToken());
            String text = String.format("Click the following link to verify your email address: %s", verificationUrl);
            helper.setText(text, false); // true indicates HTML content
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception (e.g., log it)
        }
    }
}
