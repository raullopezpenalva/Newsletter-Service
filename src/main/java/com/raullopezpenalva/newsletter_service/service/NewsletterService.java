package com.raullopezpenalva.newsletter_service.service;

import com.raullopezpenalva.newsletter_service.model.Subscriber;
import com.raullopezpenalva.newsletter_service.model.SubscriptionStatus;
import com.raullopezpenalva.newsletter_service.model.TokenType;
import com.raullopezpenalva.newsletter_service.model.VerificationToken;
import com.raullopezpenalva.newsletter_service.repository.SubscriberRepository;
import com.raullopezpenalva.newsletter_service.repository.VerificationTokenRepository;
import com.raullopezpenalva.newsletter_service.service.EmailService;
import com.raullopezpenalva.newsletter_service.service.TokenService;
import com.weaver.email_service.service.EmailService.SubscribeResult;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import javax.swing.UIDefaults.ActiveValue;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewsletterService {

    private final SubscriberRepository subscriberRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    // Constructor is no longer needed due to @RequiredArgsConstructor

    // Service methods go here
    public List<Subscriber> getAllSubscribers() {
        return subscriberRepository.findAll();
    }

    public record SubscriptionResult (Subscriber subscriber) {}

    public SubscriptionResult subscribe(Subscriber rawSubscriber) {
        String norm = rawSubscriber.getEmail() == null ? "" : rawSubscriber.getEmail().trim().toLowerCase();

        var existed = subscriberRepository.findByEmailIgnoreCase(norm);
        
        if (existed.isPresent()) {
            existed.get().setStatus(SubscriptionStatus.ACTIVE);
            return new SubscriptionResult(existed.get());
        } else if (existed.isPresent() && existed.get().getStatus() == SubscriptionStatus.PENDING) {
            // Generate new token, invalidate old tokens and send verification email again
            invalidateTokens(existed.get().getId(), TokenType.CONFIRMATION);
            var token = createToken(existed.get().getId(), TokenType.CONFIRMATION);
            emailService.sendVerificationEmail(existed.get(), token);
            return new SubscriptionResult(existed.get());
        }else {
            var newSubscriber = new Subscriber();
            newSubscriber.setEmail(norm);
            if (rawSubscriber.isUserCreated()) {
                newSubscriber.setStatus (SubscriptionStatus.ACTIVE);
                newSubscriber.setUserCreated(rawSubscriber.isUserCreated());
                var saved = subscriberRepository.save(newSubscriber);
                return new SubscriptionResult(saved);
            }
            newSubscriber.setStatus(SubscriptionStatus.PENDING);
            newSubscriber.setUserCreated(rawSubscriber.isUserCreated());
            var saved = subscriberRepository.save(newSubscriber);
            // Generate verification token, save it and send verification email
            var token = createToken(saved.getId(), TokenType.CONFIRMATION);
            emailService.sendVerificationEmail(saved, token);
            return new SubscriptionResult(saved);
        }
    }

    public record confirmSubscription(boolean success, String message) {}

    public confirmSubscription(String token) {
        VerificationResult result = TokenService.verifyToken(token);
        if (result.success() == false) {
            return new confirmSubscription(false, result.getMessage());
        }

        // Mark token as used
        UUID tokenID = result.getTokenId();
        verificationTokenRepository.markUsed(tokenID);

        // Update subscriber status to ACTIVE
        UUID subscriberID = verificationToken.getSubscriberId();
        subscriberRepository.activateSubscriber(subscriberID);

        return new confirmSubscription(true, "Subscription verified");
    }
}
