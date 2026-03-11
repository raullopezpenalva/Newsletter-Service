package com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.pub.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SubscribeRequest {

    @NotBlank
    @Email
    @Size(min = 5, max = 255)
    private String email;

    @NotBlank
    private Boolean userCreated;

    public SubscribeRequest() {
    }

    public SubscribeRequest(String email, Boolean userCreated) {
        this.email = email;
        this.userCreated = userCreated;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(Boolean userCreated) {
        this.userCreated = userCreated;
    }
}