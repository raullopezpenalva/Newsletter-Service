package com.raullopezpenalva.newsletter_service.modules.newsletter.api.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;

import org.springframework.http.*;

import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.admin.request.UpdateSubscriberStatusRequest;
import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.admin.response.GetAllByStatusResponse;
import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.admin.response.GetbyIdResponse;
import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.admin.response.UpdateSubscriberStatusResponse;
import com.raullopezpenalva.newsletter_service.modules.newsletter.application.service.NewsletterAdminService;
import com.raullopezpenalva.newsletter_service.modules.newsletter.domain.model.SubscriptionStatus;


@Tag(name = "Newsletter Service API (Admin)", description = "API for handling newsletter management (Admin)")   
@RestController
@RequestMapping("/api/v1/admin/newsletter")
public class NewsletterAdminController {

    @Autowired
    private NewsletterAdminService newsletterAdminService;

    @GetMapping(
        value = "/subscribers",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Page<GetAllByStatusResponse>> listSubscribers(@Valid @RequestParam(required = false) SubscriptionStatus status, @PageableDefault(size = 20) Pageable pageable) {

        Page<GetAllByStatusResponse> response = newsletterAdminService.getSubscribers(status, pageable);
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(
        value = "/subscribers/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<GetbyIdResponse> getSubscriberById(@PathVariable UUID id) {

        GetbyIdResponse response = newsletterAdminService.getSubscriberById(id);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping(
        value = "/subscribers/{id}/status",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UpdateSubscriberStatusResponse> updateSubscriberStatus(
        @PathVariable UUID id,
        @Valid @RequestBody UpdateSubscriberStatusRequest request
    ) {
        UpdateSubscriberStatusResponse response = newsletterAdminService.updateSubscriberStatus(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }
    
    


}
