package com.raullopezpenalva.newsletter_service.modules.newsletter.application.exception;

public class TokenNotValidException extends RuntimeException {
    public TokenNotValidException(String message) {
        super(message);
    }
    
}
