package com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.pub.response;

import java.time.Instant;
import java.util.List;

import com.raullopezpenalva.newsletter_service.modules.newsletter.application.model.UnsubscribeLink;

public class GenerateLinksResponse {

    private Instant generatedAt;
    private int count;
    private List<UnsubscribeLink> links;

    public GenerateLinksResponse() {}

    public GenerateLinksResponse(List<UnsubscribeLink> links) {
        this.generatedAt = Instant.now();
        this.count = links != null ? links.size() : 0;
        this.links = links;   
    }

    public Instant getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt() {
        this.generatedAt = Instant.now();
    }

    public int getCount() {
        count = links != null ? links.size() : 0;
        return count;
    }

    public List<UnsubscribeLink> getLinks() {
        return links;
    }

    public void setLinks(List<UnsubscribeLink> links) {
        this.count = links != null ? links.size() : 0;
        this.links = links;
    }
}