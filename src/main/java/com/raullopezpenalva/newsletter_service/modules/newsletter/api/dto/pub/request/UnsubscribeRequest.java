package com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.pub.request;

public class UnsubscribeRequest {
    
    private String token;

    public UnsubscribeRequest() {}

    public UnsubscribeRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
