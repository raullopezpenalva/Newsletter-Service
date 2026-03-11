package com.raullopezpenalva.newsletter_service.modules.newsletter.application.mapper;

import java.util.List;

import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.pub.response.GenerateLinksResponse;
import com.raullopezpenalva.newsletter_service.modules.newsletter.application.model.UnsubscribeLink;

public class GenerateLinksFlowMapper {
    
    /* Generate Confirmation Links mappers  */
    public static UnsubscribeLink toUnsubscribeLink(String email, String unsubscribeUrl) {
        return new UnsubscribeLink(email, unsubscribeUrl);
    }

    public static GenerateLinksResponse toGenerateLinksResponse(List<UnsubscribeLink> links) {
        return new GenerateLinksResponse(links);
    }
}
