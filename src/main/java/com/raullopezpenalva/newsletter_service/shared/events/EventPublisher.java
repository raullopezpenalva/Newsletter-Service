package com.raullopezpenalva.newsletter_service.shared.events;

public interface EventPublisher {
    void publish(DomainEvent event);
}
