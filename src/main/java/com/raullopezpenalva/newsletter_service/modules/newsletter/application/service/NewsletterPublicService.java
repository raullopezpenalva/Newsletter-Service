package com.raullopezpenalva.newsletter_service.modules.newsletter.application.service;

import com.raullopezpenalva.newsletter_service.shared.events.EventPublisher;

import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.pub.request.SubscribeRequest;
import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.pub.request.UnsubscribeConfirmationRequest;
import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.pub.request.UnsubscribeRequest;
import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.pub.response.GenerateLinksResponse;
import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.pub.response.SubscribeConfirmationResponse;
import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.pub.response.SubscribeResponse;
import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.pub.response.UnsubscribeConfirmationResponse;
import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.pub.response.UnsubscribeResponse;
import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.pub.types.SubscribeResult;
import com.raullopezpenalva.newsletter_service.modules.newsletter.application.exception.ResourceNotFoundException;
import com.raullopezpenalva.newsletter_service.modules.newsletter.application.exception.TokenNotValidException;
import com.raullopezpenalva.newsletter_service.modules.newsletter.application.mapper.SubscribeFlowMapper;
import com.raullopezpenalva.newsletter_service.modules.newsletter.application.mapper.UnsubscribeFlowMapper;
import com.raullopezpenalva.newsletter_service.modules.newsletter.application.model.ClientContext;
import com.raullopezpenalva.newsletter_service.modules.newsletter.application.model.UnsubscribeLink;
import com.raullopezpenalva.newsletter_service.modules.newsletter.domain.model.Subscriber;
import com.raullopezpenalva.newsletter_service.modules.newsletter.domain.model.SubscriptionStatus;
import com.raullopezpenalva.newsletter_service.modules.newsletter.infrastructure.repository.SubscriberRepository;
import com.raullopezpenalva.newsletter_service.modules.platform.tokens.application.service.TokenManagementService;
import com.raullopezpenalva.newsletter_service.modules.platform.tokens.domain.TokenType;

import java.util.List;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NewsletterPublicService {

    @Value("${app.frontendBaseUrl}")
    private String frontendBaseUrl;

    private final SubscriberRepository subscriberRepository;
    private final EventPublisher eventPublisher;
    private final TokenManagementService tokenManagementService;

    public NewsletterPublicService(SubscriberRepository subscriberRepository, EventPublisher eventPublisher, TokenManagementService tokenManagementService) {
        this.subscriberRepository = subscriberRepository;
        this.eventPublisher = eventPublisher;
        this.tokenManagementService = tokenManagementService;
    }

    // Subscribe method

    public SubscribeResponse subscribe(SubscribeRequest request, ClientContext clientContext) {
        String email = request.getEmail().toLowerCase().trim();
        var existingSubscriber = subscriberRepository.findByEmail(email);
        if (existingSubscriber.isPresent()) {
            var subscriber = existingSubscriber.get();
            if (subscriber.getStatus() != SubscriptionStatus.ACTIVE) {
                tokenManagementService.invalidateTokens(subscriber.getId(), TokenType.CONFIRMATION);
                var token = tokenManagementService.createToken(subscriber.getId(), TokenType.CONFIRMATION);
                eventPublisher.publish(SubscribeFlowMapper.toConfirmationEmailEvent(subscriber, token.getToken()));
                return SubscribeFlowMapper.toResponse(subscriber, SubscribeResult.CONFIRMATION_EMAIL_SENT);
            } else {
                return SubscribeFlowMapper.toResponse(subscriber, SubscribeResult.ALREADY_SUBSCRIBED);
            }
        } else {
            var newSubscriber = SubscribeFlowMapper.toEntity(request, clientContext);
            if (newSubscriber == null) {
                throw new IllegalArgumentException("Failed to create subscriber from request");
            }
            subscriberRepository.save(newSubscriber);
            var token = tokenManagementService.createToken(newSubscriber.getId(), TokenType.CONFIRMATION);
            eventPublisher.publish(SubscribeFlowMapper.toConfirmationEmailEvent(newSubscriber, token.getToken()));
            return SubscribeFlowMapper.toResponse(newSubscriber, SubscribeResult.CONFIRMATION_EMAIL_SENT);
        }
    }

    // Confirm subscription
    public SubscribeConfirmationResponse confirmSubscription(String token) {
        var result = tokenManagementService.verifyToken(token);
        if (result.getSuccess() == false) {
            throw new TokenNotValidException(result.getMessage());
        }

        // Mark token as used
        tokenManagementService.markTokenAsUsed(result.getTokenId());

        // Update subscriber status to ACTIVE
        var updateSubscriber = subscriberRepository.findById(result.getSubscriberId());
        updateSubscriber.get().setVerifiedAt(LocalDateTime.now());
        updateSubscriber.get().setStatus(SubscriptionStatus.ACTIVE);
        subscriberRepository.save(updateSubscriber.get());

        return SubscribeFlowMapper.toConfirmationResponse(updateSubscriber.get(), SubscribeResult.SUBSCRIBED);
    }



    // Generate unsubscribe link

    public GenerateLinksResponse generateUnsubscribeLinks() {
        List<Subscriber> Subscribers = subscriberRepository.findByStatus(SubscriptionStatus.ACTIVE);
        
        List<UnsubscribeLink> links = Subscribers.stream()
            .map(subscriber -> {
                var token = tokenManagementService.createToken(subscriber.getId(), TokenType.UNSUBSCRIBE);
                String unsubscribeUrl = frontendBaseUrl + "/unsubscribe?token=" + token.getToken();
            return new UnsubscribeLink(subscriber.getEmail(), unsubscribeUrl);
        }).toList();
        
        
        return new GenerateLinksResponse(links);
    }

    // Unsubscribe
    public UnsubscribeResponse unsubscribe(UnsubscribeRequest request) {
        var result = tokenManagementService.verifyToken(request.getToken());
        if (result.getSuccess() == false) {
            throw new TokenNotValidException(result.getMessage());
        }
        tokenManagementService.invalidateTokens(result.getSubscriberId(), TokenType.UNSUBSCRIBE);
        tokenManagementService.invalidateTokens(result.getSubscriberId(), TokenType.CONFIRMATION);
        var token = tokenManagementService.createToken(result.getSubscriberId(), TokenType.CONFIRMATION);
        var subscriber = subscriberRepository.findById(result.getSubscriberId());
        return UnsubscribeFlowMapper.toUnsubscribeResponse(subscriber.get(), token.getToken());
    }

    // Confirmation of unsubscription

    public UnsubscribeConfirmationResponse confirmUnsubscription(UnsubscribeConfirmationRequest request) {
        var result = tokenManagementService.verifyToken(request.getToken());
        if (result.getSuccess() == false) {
            throw new TokenNotValidException(result.getMessage());
        }

        // Mark token as used
        tokenManagementService.markTokenAsUsed(result.getTokenId());

        // Update subscriber status to UNSUBSCRIBED
        var updateSubscriber = subscriberRepository.findById(result.getSubscriberId());
        if (updateSubscriber.isEmpty()) {
            throw new ResourceNotFoundException("Email not found");
        }
        var subscriber = updateSubscriber.get();
        subscriber.setStatus(SubscriptionStatus.UNSUBSCRIBED);
        subscriberRepository.save(subscriber);

        return UnsubscribeFlowMapper.toUnsubscribeConfirmationResponse(subscriber);
        
    }
}
