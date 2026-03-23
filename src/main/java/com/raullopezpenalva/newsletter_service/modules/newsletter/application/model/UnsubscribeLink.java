package com.raullopezpenalva.newsletter_service.modules.newsletter.application.model;

public class UnsubscribeLink {

    private String email;
    private String unsubscribeUrl;

    public UnsubscribeLink() {}

    public UnsubscribeLink(String email, String unsubscribeUrl) {
        this.email = email;
        this.unsubscribeUrl = unsubscribeUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUnsubscribeUrl() {
        return unsubscribeUrl;
    }

    public void setUnsubscribeUrl(String unsubscribeUrl) {
        this.unsubscribeUrl = unsubscribeUrl;
    }
}