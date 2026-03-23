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
import jakarta.validation.Valid;

import org.springframework.http.*;

import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.admin.request.UpdateSubscriberStatusRequest;
import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.admin.response.GetAllByStatusResponse;
import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.admin.response.GetbyIdResponse;
import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.admin.response.UpdateSubscriberStatusResponse;
import com.raullopezpenalva.newsletter_service.modules.newsletter.api.error.ApiError;
import com.raullopezpenalva.newsletter_service.modules.newsletter.application.service.NewsletterAdminService;
import com.raullopezpenalva.newsletter_service.modules.newsletter.domain.model.SubscriptionStatus;


@Tag(name = "Newsletter Service API (Admin)", description = "API for handling newsletter management (Admin)")   
@RestController
@RequestMapping("/api/v1/admin/newsletter")
public class NewsletterAdminController {

    @Autowired
    private NewsletterAdminService newsletterAdminService;

    @Operation(
        summary = "List subscribers",
        description = "Retrieve a paginated list of subscribers. You can filter by subscription status."
    )
    @ApiResponses (value = {
            @ApiResponse(
                responseCode = "200",
                description = "Subscribers retrieved successfully",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = GetAllByStatusResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid request. The provided parameters are not valid.",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class)
                )
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error. An unexpected error occurred while processing the request.",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class)
                )
            )
        }
    )
    @GetMapping(
        value = "/subscribers",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Page<GetAllByStatusResponse>> listSubscribers(@Valid @RequestParam(required = false) SubscriptionStatus status, @PageableDefault(size = 20) Pageable pageable) {

        Page<GetAllByStatusResponse> response = newsletterAdminService.getSubscribers(status, pageable);
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
        summary = "Get subscriber by ID",
        description = "Retrieve a subscriber's details by their unique ID."
    )
    @ApiResponses (value = {
            @ApiResponse(
                responseCode = "200",
                description = "Subscriber retrieved successfully",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = GetbyIdResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Subscriber not found",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class)
                )
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error. An unexpected error occurred while processing the request.",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class)
                )
            )
        }   
    )
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
