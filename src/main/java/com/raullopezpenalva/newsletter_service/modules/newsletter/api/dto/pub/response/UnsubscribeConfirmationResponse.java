package com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.pub.response;

import java.util.UUID;

import com.raullopezpenalva.newsletter_service.modules.newsletter.domain.model.SubscriptionStatus;

public class UnsubscribeConfirmationResponse {

    private UUID id;
    private String email;
    private SubscriptionStatus status;

    public UnsubscribeConfirmationResponse() {}

    public UnsubscribeConfirmationResponse(UUID id, String email, SubscriptionStatus status) {
        this.id = id;
        this.email = email;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }
}
