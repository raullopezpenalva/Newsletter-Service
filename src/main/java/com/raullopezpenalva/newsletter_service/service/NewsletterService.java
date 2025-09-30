package com.raullopezpenalva.newsletter_service.service;

import com.raullopezpenalva.newsletter_service.model.Subscriber;
import com.raullopezpenalva.newsletter_service.model.SubscriptionStatus;
import com.raullopezpenalva.newsletter_service.model.TokenType;
import com.raullopezpenalva.newsletter_service.repository.SubscriberRepository;
import com.raullopezpenalva.newsletter_service.repository.VerificationTokenRepository;

import lombok.RequiredArgsConstructor;

import java.util.UUID;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewsletterService {

    @Autowired
    private final SubscriberRepository subscriberRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final TokenService tokenService;
    private final EmailService emailService;

    // Constructor is no longer needed due to @RequiredArgsConstructor

    // Service methods go here

    public record SubscribeResult (String finalStatus) {}

    public SubscribeResult subscribe(Subscriber rawSubscriber) {
        String norm = rawSubscriber.getEmail() == null ? "" : rawSubscriber.getEmail().trim().toLowerCase();

        var existed = subscriberRepository.findByEmail(norm);
        System.out.println("Result of findByEmail "+ existed);
        
        if (existed.isPresent() && existed.get().getStatus() == SubscriptionStatus.ACTIVE) {
            return new SubscribeResult("already_subscribed");

        } else if (existed.isPresent() && existed.get().getStatus() == SubscriptionStatus.PENDING) {
            // Generate new token, invalidate old tokens and send verification email again
            tokenService.invalidateTokens(existed.get().getId(), TokenType.CONFIRMATION);
            var token = tokenService.createToken(existed.get().getId(), TokenType.CONFIRMATION);
            emailService.sendVerificationEmail(existed.get().getEmail(), token);
            System.out.println("Resent verification email to: " + existed.get().getEmail());
            return new SubscribeResult("confirmation_email_sent");

        } else {
            var newSubscriber = new Subscriber();
            if (rawSubscriber.isUserCreated() == true) {
                newSubscriber.setEmail(norm);
                newSubscriber.setStatus (SubscriptionStatus.ACTIVE);
                newSubscriber.setUserCreated(rawSubscriber.isUserCreated());
                newSubscriber.setCreatedAt(LocalDateTime.now());
                newSubscriber.setVerifiedAt(LocalDateTime.now());
                var saved = subscriberRepository.save(newSubscriber);
                System.out.println("Saved subscriber: " + saved.getEmail() + " with ID: " + saved.getId());
                return new SubscribeResult("subscribed");
            } else {
                newSubscriber.setEmail(norm);
                newSubscriber.setStatus(SubscriptionStatus.PENDING);
                newSubscriber.setUserCreated(rawSubscriber.isUserCreated());
                newSubscriber.setCreatedAt(LocalDateTime.now());
                var saved = subscriberRepository.save(newSubscriber);
                System.out.println("Saved subscriber: " + saved.getEmail() + " with ID: " + saved.getId());
                // Generate verification token, save it and send verification email
                var token = tokenService.createToken(saved.getId(), TokenType.CONFIRMATION);
                emailService.sendVerificationEmail(saved.getEmail(), token);
                return new SubscribeResult("confirmation_email_sent");
            }
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
        var updateSubscriber = subscriberRepository.findById(result.subscriberId());
        updateSubscriber.get().setVerifiedAt(LocalDateTime.now());

        subscriberRepository.activateSubscriber(updateSubscriber.get().getId());

        return new ConfirmSubscription(true, result.message() + "- Subscription verified");
    }
}
