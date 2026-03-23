package com.raullopezpenalva.newsletter_service.modules.platform.notification.application.model;

import java.time.Instant;
import java.util.UUID;

public class NotificationSubscribeConfirmation {
    
    private UUID eventId;
    private Instant ocurredAt;
    private String email;
    private String token;

    public NotificationSubscribeConfirmation() {}

    public NotificationSubscribeConfirmation(UUID eventId, Instant ocurredAt, String email, String token) {
        this.eventId = eventId;
        this.ocurredAt = ocurredAt;
        this.email = email;
        this.token = token;
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public Instant getOcurredAt() {
        return ocurredAt;
    }

    public void setOcurredAt(Instant ocurredAt) {
        this.ocurredAt = ocurredAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
