package com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.pub.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.pub.types.SubscribeResult;

public class SubscribeResponse {
    
    private UUID id;
    private String email;
    private SubscribeResult status;
    private String message;
    private LocalDateTime createdAt;

    public SubscribeResponse() {}

    public SubscribeResponse(UUID id, String email, SubscribeResult status, String message, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.status = status;
        this.message = message;
        this.createdAt = createdAt;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
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

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
