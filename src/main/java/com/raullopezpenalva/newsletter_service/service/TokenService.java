package com.raullopezpenalva.newsletter_service.service;

import com.raullopezpenalva.newsletter_service.model.TokenType;
import com.raullopezpenalva.newsletter_service.model.VerificationToken;
import com.raullopezpenalva.newsletter_service.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    @Autowired
    private final VerificationTokenRepository verificationTokenRepository;
    // Constructor is no longer needed due to @RequiredArgsConstructor

    // Service methods go here
    public VerificationToken createToken(UUID subscriberId, TokenType type) {
        var token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setSubscriberId(subscriberId);
        verificationToken.setType(type);
        verificationToken.setUsed(false);
        verificationToken.setCreatedAt(LocalDateTime.now());
        if (type == TokenType.CONFIRMATION) {
            verificationToken.setExpiresAt(LocalDateTime.now().plusHours(24)); // 24 hours for verification tokens
        } else if (type == TokenType.UNSUBSCRIBE) {
            verificationToken.setExpiresAt(LocalDateTime.now().plusDays(7)); // 7 days for unsubscribe tokens
        }
        VerificationToken savedToken = verificationTokenRepository.save(verificationToken);
        System.out.println("Created token: " + savedToken.getToken() + " for subscriber ID: " + savedToken.getSubscriberId());
        return savedToken;
    }

    public void invalidateTokens(UUID subscriberId, TokenType type) {
        List<VerificationToken> tokens = verificationTokenRepository.findBySubscriberIdAndType(subscriberId, type);
        for (var token : tokens) {
            token.setUsed(true);
        }
        verificationTokenRepository.saveAll(tokens);
    }

    public record VerificationResult(boolean success, String message, UUID tokenId, UUID subscriberId) {}

    public VerificationResult verifyToken(String token) {
        var vtoken = verificationTokenRepository.findByToken(token);
        if (vtoken.isEmpty()) {
            return new VerificationResult(false, "Invalid token", null, null);
        }
        var verificationToken = vtoken.get();
        if (verificationToken.isUsed()) {
            return new VerificationResult(false, "Token already used", verificationToken.getId(), verificationToken.getSubscriberId());
        }
        if (verificationToken.getExpiresAt() != null && verificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            return new VerificationResult(false, "Token expired", verificationToken.getId(), verificationToken.getSubscriberId());
        }
        return new VerificationResult(true, "Token is valid", verificationToken.getId(), verificationToken.getSubscriberId());
    }

}
