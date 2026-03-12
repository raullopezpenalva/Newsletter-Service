package com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.admin.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.raullopezpenalva.newsletter_service.modules.newsletter.domain.model.SubscriptionStatus;

public class UpdateSubscriberStatusResponse {
    
    private UUID id;
    private String email;
    private SubscriptionStatus status;
    private String adminNote;
    private LocalDateTime updatedAt;

    public UpdateSubscriberStatusResponse() {}

    public UpdateSubscriberStatusResponse(UUID id, String email, SubscriptionStatus status, String adminNote, LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.status = status;
        this.adminNote = adminNote;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
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

    public String getAdminNote() {
        return adminNote;
    }

    public void setAdminNote(String adminNote) {
        this.adminNote = adminNote;
    }
}
