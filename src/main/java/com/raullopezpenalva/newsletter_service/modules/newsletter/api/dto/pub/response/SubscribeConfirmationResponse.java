package com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.pub.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.pub.types.SubscribeResult;

public class SubscribeConfirmationResponse {
    
    private UUID id;
    private String email;
    private SubscribeResult status;
    private String message;
    private LocalDateTime verifiedAt;

    public SubscribeConfirmationResponse() {}

    public SubscribeConfirmationResponse(UUID id, String email, SubscribeResult status, String message, LocalDateTime verifiedAt) {
        this.id = id;
        this.email = email;
        this.status = status;
        this.message = message;
        this.verifiedAt = verifiedAt;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public SubscribeResult getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getVerifiedAt() {
        return verifiedAt;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStatus(SubscribeResult status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setVerifiedAt(LocalDateTime verifiedAt) {
        this.verifiedAt = verifiedAt;
    }
}

