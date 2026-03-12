package com.raullopezpenalva.newsletter_service.modules.platform.notification.application.mapper;

import com.raullopezpenalva.newsletter_service.modules.newsletter.domain.model.events.SubscribeFlowConfirmationEmailEvent;
import com.raullopezpenalva.newsletter_service.modules.platform.notification.application.model.NotificationSubscribeConfirmation;

public class NotificationSubscribeConfirmationMapper {
    
    public static NotificationSubscribeConfirmation toNotificationSubscribeConfirmation(SubscribeFlowConfirmationEmailEvent event) {
        return new NotificationSubscribeConfirmation(
            event.eventId(),
            event.ocurredAt(),
            event.email(),
            event.token()
        );
    }
}
