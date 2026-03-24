package com.raullopezpenalva.newsletter_service.modules.newsletter.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.pub.request.SubscribeRequest;
import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.pub.request.UnsubscribeConfirmationRequest;
import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.pub.request.UnsubscribeRequest;
import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.pub.response.GenerateLinksResponse;
import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.pub.response.SubscribeConfirmationResponse;
import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.pub.response.SubscribeResponse;
import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.pub.response.UnsubscribeConfirmationResponse;
import com.raullopezpenalva.newsletter_service.modules.newsletter.api.dto.pub.response.UnsubscribeResponse;
import com.raullopezpenalva.newsletter_service.modules.newsletter.api.error.ApiError;
import com.raullopezpenalva.newsletter_service.modules.newsletter.application.model.ClientContext;
import com.raullopezpenalva.newsletter_service.modules.newsletter.application.service.NewsletterPublicService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
// import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.*;

@Tag(name = "Newsletter Public API", description = "Endpoints for public newsletter operations such as subscribing, confirming subscription, generating unsubscribe links, and unsubscribing.")
@RestController
@RequestMapping("/api/v1/newsletter")
public class NewsletterPublicController {

    @Autowired
    private NewsletterPublicService newsletterService;


    // Subscribe endpoint
    @Operation(
        summary = "Subscribe to the newsletter",
        description = "Subscribe to the newsletter with your email address. If the email is already subscribed, a conflict status will be returned. If double opt-in is enabled and the request does not originate from user creation, a confirmation email will be sent.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Subscription request containing the email address to subscribe and if the request comes from a user system.",
            required = true,
            content = @Content(
                schema = @Schema(implementation = SubscribeRequest.class)
            )
        )
    )
    @ApiResponses (value = {
            @ApiResponse(
                responseCode = "200",
                description = "Subscription successful. If the request does not originate from user creation, a confirmation email will be sent.",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = SubscribeResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid request. The email address is missing or not in a valid format.",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class)
                )
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error. An unexpected error occurred while processing the subscription request.",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class)
                )
            )
        }
    )
    @PostMapping(
        value = "/subscribe",
         consumes = MediaType.APPLICATION_JSON_VALUE,
         produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SubscribeResponse> subscribe(@Valid @RequestBody SubscribeRequest request, HttpServletRequest httpRequest) {

        ClientContext clientContext = new ClientContext(
            httpRequest.getHeader("X-Forwarded-For") != null ? httpRequest.getHeader("X-Forwarded-For") : httpRequest.getRemoteAddr(),
            httpRequest.getHeader("User-Agent")
        );
        SubscribeResponse response = newsletterService.subscribe(request, clientContext);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Verify endpoint
    @Operation(
        summary = "Confirm newsletter subscription",
        description = "Confirm your newsletter subscription using the token sent to your email. If the token is valid, your subscription will be activated."
    )
    @ApiResponses (value = {
            @ApiResponse(
                responseCode = "200",
                description = "Subscription confirmed",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = SubscribeConfirmationResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid request. The token is missing, invalid, or has already been used.",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class)
                )
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error. An unexpected error occurred while processing the subscription request.",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class)
                )
            )
        }
    )
    @PostMapping(
        value = "/verify",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SubscribeConfirmationResponse> confirmSubscription(@RequestParam String token) {
        SubscribeConfirmationResponse response = newsletterService.confirmSubscription(token);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    // UnsubscribeLinks Generation endpoint
    @Operation(
        summary = "Generate unsubscribe links",
        description = "Generate unsubscribe links for all active email addresses. These links can be used to unsubscribe from the newsletter."
    )
    @ApiResponses (value = {
            @ApiResponse(
                responseCode = "200",
                description = "Unsubscribe links generated",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = GenerateLinksResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Email not found",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class)
                )
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error. An unexpected error occurred while generating the unsubscribe links.",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class)
                )
             )
        }
    )
    @GetMapping(
        value = "/generate-unsubscribe-links",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<GenerateLinksResponse> generateUnsubscribeLinks() {
        GenerateLinksResponse response = newsletterService.generateUnsubscribeLinks();
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Unsubscribe endpoint
    @Operation(
        summary = "Unsubscribe from the newsletter",
        description = "Unsubscribe from the newsletter using the token sent to your email. If the token is valid redirection to the frontend to confirm unsubscription.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Unsubscription request containing the token sent to the user's email.",
                required = true,
                content = @Content(
                    schema = @Schema(implementation = UnsubscribeRequest.class)
                )
            )
    )
    @ApiResponses (value = {
            @ApiResponse(
                responseCode = "200",
                description = "Token valid.",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UnsubscribeResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid or expired token",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class)
                )
             ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error. An unexpected error occurred while unsubscribing.",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class)
                )
             )
        }
    )
    @PostMapping(
        value = "/unsubscribe",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UnsubscribeResponse> unsubscribe(@Valid @RequestBody UnsubscribeRequest request) {
        UnsubscribeResponse response = newsletterService.unsubscribe(request);
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    
    }

    // Confirm Unsubscription endpoint
    @Operation(
        summary = "Confirm unsubscription from the newsletter",
        description = "Confirm your unsubscription from the newsletter using the the frontend confirmation. If successful, your subscription will be marked as unsubscribed.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Unsubscription confirmation request containing the token sent to the user's email.",
                required = true,
                content = @Content(
                    schema = @Schema(implementation = UnsubscribeConfirmationRequest.class)
                )
            )
    )
    @ApiResponses (value = {
            @ApiResponse(
                responseCode = "200",
                description = "Unsubscription successful",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UnsubscribeConfirmationResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Email not found",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class)
                )
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error. An unexpected error occurred while confirming unsubscription.",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiError.class)
                )
             )
        }
    )
    @PostMapping(
        value = "/confirm-unsubscription",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UnsubscribeConfirmationResponse> confirmUnsubscription(@Valid @RequestBody UnsubscribeConfirmationRequest request) {
        UnsubscribeConfirmationResponse response = newsletterService.confirmUnsubscription(request);
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}