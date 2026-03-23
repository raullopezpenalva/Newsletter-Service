package com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.admin.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.raullopezpenalva.newsletter_service.modules.newsletter.domain.model.SubscriptionStatus;

public class GetAllByStatusResponse {

    private UUID id;
    private String email;
    private SubscriptionStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime verifiedAt;
    private LocalDateTime updatedAt;

    public GetAllByStatusResponse() {}

    public GetAllByStatusResponse(UUID id, String email, SubscriptionStatus status, LocalDateTime createdAt, LocalDateTime verifiedAt, LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.status = status;
        this.createdAt = createdAt;
        this.verifiedAt = verifiedAt;
        this.updatedAt = updatedAt;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(LocalDateTime verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}