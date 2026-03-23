package com.raullopezpenalva.newsletter_service.modules.newsletter.application.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
}
