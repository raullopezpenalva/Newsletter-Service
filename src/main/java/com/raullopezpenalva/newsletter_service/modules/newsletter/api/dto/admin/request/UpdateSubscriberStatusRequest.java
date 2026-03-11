package com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.admin.request;

import com.raullopezpenalva.newsletter_service.modules.newsletter.domain.model.SubscriptionStatus;

public class UpdateSubscriberStatusRequest {
    
    private SubscriptionStatus status;

    public UpdateSubscriberStatusRequest() {}

    public UpdateSubscriberStatusRequest(SubscriptionStatus status) {
        this.status = status;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }
}
