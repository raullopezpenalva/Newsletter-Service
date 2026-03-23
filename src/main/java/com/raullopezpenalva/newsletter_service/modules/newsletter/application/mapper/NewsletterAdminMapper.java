package com.raullopezpenalva.newsletter_service.modules.newsletter.application.mapper;

import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.admin.response.GetAllByStatusResponse;
import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.admin.response.GetbyIdResponse;
import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.admin.response.UpdateSubscriberStatusResponse;
import com.raullopezpenalva.newsletter_service.modules.newsletter.domain.model.Subscriber;

public class NewsletterAdminMapper {
    
    public static GetAllByStatusResponse toGetAllByStatusResponse(Subscriber subscriber) {
        return new GetAllByStatusResponse(
            subscriber.getId(),
            subscriber.getEmail(),
            subscriber.getStatus(),
            subscriber.getCreatedAt(),
            subscriber.getVerifiedAt(),
            subscriber.getUpdatedAt()
        );
    }

    public static GetbyIdResponse toGetByIdResponse(Subscriber subscriber) {
        return new GetbyIdResponse(
            subscriber.getId(),
            subscriber.getEmail(),
            subscriber.getStatus(),
            subscriber.getCreatedAt(),
            subscriber.getVerifiedAt(),
            subscriber.getUpdatedAt(),
            subscriber.getAdminNote(),
            subscriber.getSourceIp(),
            subscriber.getUserAgent()
        );
    }




    public static UpdateSubscriberStatusResponse toUpdateSubscriberStatusResponse(Subscriber subscriber) {
        return new UpdateSubscriberStatusResponse(
            subscriber.getId(),
            subscriber.getEmail(),
            subscriber.getStatus(),
            subscriber.getAdminNote(),
            subscriber.getUpdatedAt()
        );
    }
}
