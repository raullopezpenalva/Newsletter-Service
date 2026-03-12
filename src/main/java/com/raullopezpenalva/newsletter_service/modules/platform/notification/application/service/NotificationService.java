package com.raullopezpenalva.newsletter_service.modules.platform.notification.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.raullopezpenalva.newsletter_service.modules.platform.notification.application.model.NotificationSubscribeConfirmation;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final NotificationChannel notificationChannel;
    private final boolean notificationEnabled;

    public NotificationService(
        NotificationChannel notificationChannel,
        @Value("${app.notifications.enabled:true}") boolean notificationEnabled) {
        this.notificationChannel = notificationChannel;
        this.notificationEnabled = notificationEnabled;
    }

    public void sendNotificationSubscribeConfirmationEmail(NotificationSubscribeConfirmation notification) {
        if (!notificationEnabled) {
            log.debug("Notification is disabled, skipping sending email for eventId={}, email={}", notification.getEventId(), notification.getEmail());
            return;
        }
        try {
            notificationChannel.sendNotificationSubscribeConfirmationEmail(notification);
            log.info("Notification sent successfully for eventId={}, email={}", notification.getEventId());
        } catch (Exception ex) {
            log.error("Failed to send notification for eventId={}, email={}, error={}", notification.getEventId(), notification.getEmail(), ex);
        }
    }

    
}
