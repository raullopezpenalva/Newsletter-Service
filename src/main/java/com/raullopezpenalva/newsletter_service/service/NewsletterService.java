package com.raullopezpenalva.newsletter_service.service;

import com.raullopezpenalva.newsletter_service.model.Subscriber;
import com.raullopezpenalva.newsletter_service.model.SubscriptionStatus;
import com.raullopezpenalva.newsletter_service.model.TokenType;
import com.raullopezpenalva.newsletter_service.repository.SubscriberRepository;
import com.raullopezpenalva.newsletter_service.repository.VerificationTokenRepository;

import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewsletterService {

    @Autowired
    private final SubscriberRepository subscriberRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final TokenService tokenService;
    private EmailService emailService;

    // Constructor is no longer needed due to @RequiredArgsConstructor

    // Service methods go here

    public record SubscribeResult (String finalStatus) {}

    public SubscribeResult subscribe(Subscriber rawSubscriber) {
        String norm = rawSubscriber.getEmail() == null ? "" : rawSubscriber.getEmail().trim().toLowerCase();

        var existed = subscriberRepository.findByEmail(norm);
        
        if (existed.isPresent() && existed.get().getStatus() == SubscriptionStatus.ACTIVE) {
            return new SubscribeResult("already_subscribed");

        } else if (existed.isPresent() && existed.get().getStatus() == SubscriptionStatus.PENDING) {
            // Generate new token, invalidate old tokens and send verification email again
            tokenService.invalidateTokens(existed.get().getId(), TokenType.CONFIRMATION);
            var token = tokenService.createToken(existed.get().getId(), TokenType.CONFIRMATION);
            emailService.sendVerificationEmail(existed.get().getEmail(), token);
            return new SubscribeResult("confirmation_email_sent");

        } else {
            var newSubscriber = new Subscriber();
            newSubscriber.setEmail(norm);
            if (rawSubscriber.isUserCreated()) {
                newSubscriber.setStatus (SubscriptionStatus.ACTIVE);
                newSubscriber.setUserCreated(rawSubscriber.isUserCreated());
                subscriberRepository.save(newSubscriber);
                return new SubscribeResult("subscribed");
            }
            newSubscriber.setStatus(SubscriptionStatus.PENDING);
            newSubscriber.setUserCreated(rawSubscriber.isUserCreated());
            var saved = subscriberRepository.save(newSubscriber);
            // Generate verification token, save it and send verification email
            var token = tokenService.createToken(saved.getId(), TokenType.CONFIRMATION);
            emailService.sendVerificationEmail(saved.getEmail(), token);
            return new SubscribeResult("confirmation_email_sent");
        }
    }

    public record ConfirmSubscription(boolean success, String message) {}

    public ConfirmSubscription confirmSubscription(String token) {
        var result = tokenService.verifyToken(token);
        if (result.success() == false) {
            return new ConfirmSubscription(false, result.message());
        }

        // Mark token as used
        UUID tokenID = result.tokenId();
        verificationTokenRepository.markUsed(tokenID);

        // Update subscriber status to ACTIVE
        subscriberRepository.activateSubscriber(result.subscriberId());

        return new ConfirmSubscription(true, result.message() + "- Subscription verified");
    }
}
