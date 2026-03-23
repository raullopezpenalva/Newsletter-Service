package com.raullopezpenalva.newsletter_service.modules.newsletter.application.mapper;

import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.pub.response.UnsubscribeConfirmationResponse;
import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.pub.response.UnsubscribeResponse;
import com.raullopezpenalva.newsletter_service.modules.newsletter.domain.model.Subscriber;

public class UnsubscribeFlowMapper {
    
    /* Unsubscribe Flow mappers  */
    public static UnsubscribeResponse toUnsubscribeResponse(Subscriber subscriber, String token) {
        return new UnsubscribeResponse(subscriber.getEmail(), token);
    }

    public static UnsubscribeConfirmationResponse toUnsubscribeConfirmationResponse(Subscriber subscriber) {
        
        return new UnsubscribeConfirmationResponse(subscriber.getId(), subscriber.getEmail(), subscriber.getStatus());
    }
}
