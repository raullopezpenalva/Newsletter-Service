package com.raullopezpenalva.newsletter_service.modules.platform.tokens.application.service;

import java.util.UUID;

import com.raullopezpenalva.newsletter_service.modules.platform.tokens.application.model.VerificationResult;
import com.raullopezpenalva.newsletter_service.modules.platform.tokens.domain.TokenType;
import com.raullopezpenalva.newsletter_service.modules.platform.tokens.domain.VerificationToken;

public interface TokenManagementService {

    VerificationToken createToken(UUID subscriberId, TokenType type);

    void invalidateTokens(UUID subscriberId, TokenType type);

    VerificationResult verifyToken(String token);

    void markTokenAsUsed(UUID tokenId);
}