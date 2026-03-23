package com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.pub.response;

public class UnsubscribeResponse {
    
    private String email;
    private String token;

    public UnsubscribeResponse() {}

    public UnsubscribeResponse(String email, String token) {
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
