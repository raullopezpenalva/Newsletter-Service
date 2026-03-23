package com.raullopezpenalva.newsletter_service.modules.platform.tokens.application.exception;

public class TokenNotValidException extends RuntimeException {
    public TokenNotValidException(String message) {
        super(message);
    }
    
}
