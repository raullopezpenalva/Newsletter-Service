package com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.pub.request;

public class UnsubscribeConfirmationRequest {
    
    private String email;
    private String token;

    public UnsubscribeConfirmationRequest() {}

    public UnsubscribeConfirmationRequest(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

