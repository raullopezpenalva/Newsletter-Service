package com.raullopezpenalva.newsletter_service.modules.platform.notification.application.service;

import com.raullopezpenalva.newsletter_service.modules.platform.notification.application.model.NotificationSubscribeConfirmation;

public interface NotificationChannel {
    void sendNotificationSubscribeConfirmationEmail(NotificationSubscribeConfirmation message);
}
