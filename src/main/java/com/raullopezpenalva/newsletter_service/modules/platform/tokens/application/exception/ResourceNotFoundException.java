package com.raullopezpenalva.newsletter_service.modules.platform.tokens.application.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
}
