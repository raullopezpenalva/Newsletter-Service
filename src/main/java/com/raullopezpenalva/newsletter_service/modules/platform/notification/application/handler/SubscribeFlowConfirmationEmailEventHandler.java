package com.raullopezpenalva.newsletter_service.modules.platform.notification.application.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.raullopezpenalva.newsletter_service.modules.newsletter.domain.model.events.SubscribeFlowConfirmationEmailEvent;
import com.raullopezpenalva.newsletter_service.modules.platform.notification.application.mapper.NotificationSubscribeConfirmationMapper;
import com.raullopezpenalva.newsletter_service.modules.platform.notification.application.model.NotificationSubscribeConfirmation;
import com.raullopezpenalva.newsletter_service.modules.platform.notification.application.service.NotificationService;

@Component
public class SubscribeFlowConfirmationEmailEventHandler {

    private static final Logger log = LoggerFactory.getLogger(SubscribeFlowConfirmationEmailEventHandler.class);
    private final NotificationService notificationService;

    public SubscribeFlowConfirmationEmailEventHandler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @EventListener
    public void handle(SubscribeFlowConfirmationEmailEvent event) {
        try {
            NotificationSubscribeConfirmation notification = NotificationSubscribeConfirmationMapper.toNotificationSubscribeConfirmation(event);

            log.info(
                "Handling eventType={}, eventId={}, email={}",
                event.eventType(),
                event.eventId(),
                event.email()
            );
            notificationService.sendNotificationSubscribeConfirmationEmail(notification);
        } catch (Exception ex) {

            log.error(
                "Failed to handle SubscribeFlowConfirmationEmailEvent, eventId={}, email={}, error={}",
                event.eventId(),
                event.email(),
                ex
            );
        }
    }
}
