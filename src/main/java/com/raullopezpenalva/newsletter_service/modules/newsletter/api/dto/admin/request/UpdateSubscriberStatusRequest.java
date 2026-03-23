package com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.admin.request;

import com.raullopezpenalva.newsletter_service.modules.newsletter.domain.model.SubscriptionStatus;

public class UpdateSubscriberStatusRequest {
    
    private SubscriptionStatus status;
    private String adminNote;

    public UpdateSubscriberStatusRequest() {}

    public UpdateSubscriberStatusRequest(SubscriptionStatus status, String adminNote) {
        this.status = status;
        this.adminNote = adminNote;
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
