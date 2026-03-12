package com.raullopezpenalva.newsletter_service.modules.newsletter.application.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.admin.request.UpdateSubscriberStatusRequest;
import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.admin.response.GetAllByStatusResponse;
import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.admin.response.GetbyIdResponse;
import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.admin.response.UpdateSubscriberStatusResponse;
import com.raullopezpenalva.newsletter_service.modules.newsletter.application.mapper.NewsletterAdminMapper;
import com.raullopezpenalva.newsletter_service.modules.newsletter.domain.model.Subscriber;
import com.raullopezpenalva.newsletter_service.modules.newsletter.domain.model.SubscriptionStatus;
import com.raullopezpenalva.newsletter_service.modules.newsletter.infrastructure.repository.SubscriberRepository;
import com.raullopezpenalva.newsletter_service.modules.platform.tokens.application.exception.ResourceNotFoundException;

@Service
public class NewsletterAdminService {

    private final SubscriberRepository subscriberRepository;
    
    public NewsletterAdminService(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }

    public Page<GetAllByStatusResponse> getSubscribers(SubscriptionStatus status, Pageable pageable) {
        
        Pageable sorted = PageRequest.of(
            pageable.getPageNumber(),
            pageable.getPageSize(),
            Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<Subscriber> page = (status == null)
            ? subscriberRepository.findAll(sorted)
            : subscriberRepository.findByStatus(status, sorted);

        return page.map(NewsletterAdminMapper::toGetAllByStatusResponse);
    }

    public GetbyIdResponse getSubscriberById(UUID id) {
        Subscriber subscriber = subscriberRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Subscriber not found with id: " + id
        ));
        return NewsletterAdminMapper.toGetByIdResponse(subscriber);
    }

    public UpdateSubscriberStatusResponse updateSubscriberStatus(UUID id, UpdateSubscriberStatusRequest request) {
        Subscriber subscriber = subscriberRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Subscriber not found with id: " + id
        ));
        if (subscriber.getStatus() != request.getStatus()) {
            subscriber.setStatus(request.getStatus());
        }
        subscriber.setAdminNote(request.getAdminNote());
        subscriber.setUpdatedAt(LocalDateTime.now());
        Subscriber updatedSubscriber = subscriberRepository.save(subscriber);

        return NewsletterAdminMapper.toUpdateSubscriberStatusResponse(updatedSubscriber);
    }

}
