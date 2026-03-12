package com.raullopezpenalva.newsletter_service.modules.newsletter.application.mapper;


import java.time.Instant;
import java.util.UUID;

import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.pub.request.SubscribeRequest;
import com.raullopezpenalva.newsletter_service.modules.newsletter.application.model.ClientContext;
import com.raullopezpenalva.newsletter_service.modules.newsletter.domain.model.Subscriber;
import com.raullopezpenalva.newsletter_service.modules.newsletter.domain.model.SubscriptionStatus;
import com.raullopezpenalva.newsletter_service.modules.newsletter.domain.model.events.SubscribeFlowConfirmationEmailEvent;
import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.pub.response.SubscribeConfirmationResponse;
import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.pub.response.SubscribeResponse;
import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.pub.types.SubscribeResult;

public class SubscribeFlowMapper {
    
    /* Subscribe mappers */
    public static Subscriber toEntity(SubscribeRequest request, ClientContext clientContext) {
        Subscriber entity = new Subscriber();

        var norm = request.getEmail().toLowerCase().trim();
        entity.setEmail(norm);
        entity.setUserCreated(request.getUserCreated());
        entity.setStatus(SubscriptionStatus.PENDING);
        entity.setSourceIp(clientContext.getSourceIP());
        entity.setUserAgent(clientContext.getUserAgent());

        return entity;
    }

    public static SubscribeResponse toResponse(Subscriber subscriber, SubscribeResult status) {

        SubscribeResponse response = new SubscribeResponse();
        response.setId(subscriber.getId());
        response.setEmail(subscriber.getEmail());
        response.setCreatedAt(subscriber.getCreatedAt());
        if (status == SubscribeResult.SUBSCRIBED) {
            response.setStatus(SubscribeResult.SUBSCRIBED);
            response.setMessage("You have been successfully subscribed to the newsletter.");
        } else if (status == SubscribeResult.ALREADY_SUBSCRIBED) {
            response.setStatus(SubscribeResult.ALREADY_SUBSCRIBED);
            response.setMessage("You are already subscribed to the newsletter.");
        } else if (status == SubscribeResult.CONFIRMATION_EMAIL_SENT) {
            response.setStatus(SubscribeResult.CONFIRMATION_EMAIL_SENT);
            response.setMessage("A confirmation email has been sent to your address. Please check your inbox and click the confirmation link to complete your subscription.");
        }
        
        return response;
    }

    public static SubscribeConfirmationResponse toConfirmationResponse(Subscriber subscriber, SubscribeResult status) {
        SubscribeConfirmationResponse response = new SubscribeConfirmationResponse();
        response.setId(subscriber.getId());
        response.setEmail(subscriber.getEmail());
        response.setVerifiedAt(subscriber.getVerifiedAt());
        if (status == SubscribeResult.SUBSCRIBED) {
            response.setStatus(SubscribeResult.SUBSCRIBED);
            response.setMessage("Your subscription has been confirmed. You are now subscribed to the newsletter.");
        } else {
            response.setStatus(SubscribeResult.CONFIRMATION_FAILED);
            response.setMessage("The confirmation link is invalid or has expired. Please try subscribing again.");
        }
        
        return response;
    }
    public static SubscribeFlowConfirmationEmailEvent toConfirmationEmailEvent(Subscriber subscriber, String token) {

        SubscribeFlowConfirmationEmailEvent event = new SubscribeFlowConfirmationEmailEvent(
            UUID.randomUUID(),
            Instant.now(),
            SubscribeFlowConfirmationEmailEvent.TYPE,
            subscriber.getEmail(),
            token, 
            null
        );

        return event;
    }
}