package com.raullopezpenalva.newsletter_service.modules.platform.notification.infrastructure.emailSender;

import org.springframework.stereotype.Component;

import com.raullopezpenalva.newsletter_service.modules.platform.notification.application.service.NotificationChannel;
import com.raullopezpenalva.newsletter_service.modules.platform.notification.application.model.NotificationSubscribeConfirmation;

@Component
public class EmailNotificationChannel implements NotificationChannel {

    private final EmailSender emailSender;

    public EmailNotificationChannel(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendNotificationSubscribeConfirmationEmail(NotificationSubscribeConfirmation notification) {
        try {
            emailSender.sendSubscribeConfirmationEmail(notification);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to send subscribe confirmation email", ex);
        }
    }
}
