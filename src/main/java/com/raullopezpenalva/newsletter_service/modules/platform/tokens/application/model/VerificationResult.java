package com.raullopezpenalva.newsletter_service.modules.platform.tokens.application.model;

import java.util.UUID;

public class VerificationResult {
    private boolean success;
    private String message;
    private UUID tokenId;
    private UUID subscriberId;

    public VerificationResult(boolean success, String message, UUID tokenId, UUID subscriberId) {
        this.success = success;
        this.message = message;
        this.tokenId = tokenId;
        this.subscriberId = subscriberId;
    }

    public VerificationResult() {
        //TODO Auto-generated constructor stub
    }

    public boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public UUID getTokenId() {
        return tokenId;
    }

    public UUID getSubscriberId() {
        return subscriberId;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTokenId(UUID tokenId) {
        this.tokenId = tokenId;
    }

    public void setSubscriberId(UUID subscriberId) {
        this.subscriberId = subscriberId;
    }
}
