package com.raullopezpenalva.newsletter_service.modules.newsletter.domain.model.events;

import java.time.Instant;
import java.util.UUID;

import com.raullopezpenalva.newsletter_service.shared.events.DomainEvent;

public record SubscribeFlowConfirmationEmailEvent(
    UUID eventId,
    Instant ocurredAt,
    String eventType,
    String email,
    String token,
    String correlationId
) implements DomainEvent {

    public static final String TYPE = "SubscribeFlowConfirmationEmailEvent";

    public SubscribeFlowConfirmationEmailEvent {
        if (eventId == null) {
            throw new IllegalArgumentException("eventId cannot be null");
        }
        if (ocurredAt == null) {
            throw new IllegalArgumentException("ocurredAt cannot be null");
        }
    }
    
    @Override
    public String eventType() {
        return TYPE;
    }
}
