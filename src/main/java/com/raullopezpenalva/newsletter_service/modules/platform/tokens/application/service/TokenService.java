package com.raullopezpenalva.newsletter_service.modules.platform.tokens.application.service;

import com.raullopezpenalva.newsletter_service.modules.platform.tokens.application.exception.ResourceNotFoundException;
import com.raullopezpenalva.newsletter_service.modules.platform.tokens.application.mapper.TokensMapper;
import com.raullopezpenalva.newsletter_service.modules.platform.tokens.application.model.VerificationResult;
import com.raullopezpenalva.newsletter_service.modules.platform.tokens.domain.TokenType;
import com.raullopezpenalva.newsletter_service.modules.platform.tokens.domain.VerificationToken;
import com.raullopezpenalva.newsletter_service.modules.platform.tokens.infrastructure.repository.VerificationTokenRepository;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class TokenService implements TokenManagementService {

    private final VerificationTokenRepository verificationTokenRepository;
    
    public TokenService(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    // Create token
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

    // Invalidate tokens
    public void invalidateTokens(UUID subscriberId, TokenType type) {
        List<VerificationToken> tokens = verificationTokenRepository.findBySubscriberIdAndType(subscriberId, type);
        for (var token : tokens) {
            token.setUsed(true);
        }
        verificationTokenRepository.saveAll(tokens);
    }

    // Verify token
    public VerificationResult verifyToken(String token) {
        var vtoken = verificationTokenRepository.findByToken(token);
        if (vtoken.isEmpty()) {
            return TokensMapper.toVerificationResult(false, "Invalid token", null, null);
        }
        var verificationToken = vtoken.get();
        if (verificationToken.isUsed()) {
            return TokensMapper.toVerificationResult(false, "Token already used", verificationToken.getId(), verificationToken.getSubscriberId());
        }
        if (verificationToken.getExpiresAt() != null && verificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            return TokensMapper.toVerificationResult(false, "Token expired", verificationToken.getId(), verificationToken.getSubscriberId());
        }
        return TokensMapper.toVerificationResult(true, "Token is valid", verificationToken.getId(), verificationToken.getSubscriberId());
    }

    // Mark token as used
    @Transactional
    public void markTokenAsUsed(UUID tokenId) {
        var token = verificationTokenRepository.findById(tokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found"));
        token.setUsed(true);
    }
}
