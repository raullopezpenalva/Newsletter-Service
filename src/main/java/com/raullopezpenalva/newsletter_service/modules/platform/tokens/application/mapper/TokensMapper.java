package com.raullopezpenalva.newsletter_service.modules.platform.tokens.application.mapper;

import java.util.UUID;

import com.raullopezpenalva.newsletter_service.modules.platform.tokens.application.model.VerificationResult;

public class TokensMapper {
    
    public static VerificationResult toVerificationResult(boolean success, String message, UUID tokenId, UUID subscriberId) {
        VerificationResult result = new VerificationResult();
        result.setSuccess(success);
        result.setMessage(message);
        result.setTokenId(tokenId);
        result.setSubscriberId(subscriberId);


        return new VerificationResult(success, message, tokenId, subscriberId);
    }
}
