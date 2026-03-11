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
import com.raullopezpenalva.newsletter_service.modules.newsletter.application.model.ClientContext;
import com.raullopezpenalva.newsletter_service.modules.newsletter.application.service.NewsletterPublicService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.*;

import java.lang.reflect.Parameter;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/newsletter")
public class NewsletterPublicController {

    @Autowired
    private NewsletterPublicService newsletterService;


    // Subscribe endpoint
    @Operation(
        summary = "Subscribe to the newsletter",
        description = "Subscribe to the newsletter with your email address. If the email is already subscribed, a conflict status will be returned. If double opt-in is enabled and the request does not originate from user creation, a confirmation email will be sent."
    )
    @ApiResponses (value = {
            @ApiResponse(responseCode = "201", description = "Successfully subscribed"),
            @ApiResponse(responseCode = "200", description = "Confirmation email sent"),
            @ApiResponse(responseCode = "409", description = "Email already subscribed")
        }
    )
    @PostMapping(
        value = "/subscribe",
         consumes = MediaType.APPLICATION_JSON_VALUE,
         produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SubscribeResponse> subscribe(@Valid @RequestBody  SubscribeRequest request, HttpServletRequest httpRequest) {

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
            @ApiResponse(responseCode = "200", description = "Subscription confirmed"),
            @ApiResponse(responseCode = "400", description = "Invalid or expired token")
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


    // UnsubscribeLink Generation endpoint
    @Operation(
        summary = "Generate unsubscribe link",
        description = "Generate an unsubscribe link for all active email addresses. This link can be used to unsubscribe from the newsletter."
    )
    @ApiResponses (value = {
            @ApiResponse(responseCode = "200", description = "Unsubscribe link generated"),
            @ApiResponse(responseCode = "404", description = "Email not found")
        }
    )
    @GetMapping(
        value = "/generate-unsubscribe-link",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<GenerateLinksResponse> generateUnsubscribeLinks() {
        GenerateLinksResponse response = newsletterService.generateUnsubscribeLinks();
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Unsubscribe endpoint
    @Operation(
        summary = "Unsubscribe from the newsletter",
        description = "Unsubscribe from the newsletter using the token sent to your email. If the token is valid redirection to the frontend to confirm unsubscription."
    )
    @ApiResponses (value = {
            @ApiResponse(responseCode = "200", description = "Unsubscription frontend confirmation sent"),
            @ApiResponse(responseCode = "400", description = "Invalid or expired token")
        }
    )
    @GetMapping(
        value = "/unsubscribe",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UnsubscribeResponse> unsubscribe(@Valid @RequestBody UnsubscribeRequest request) {
        UnsubscribeResponse response = newsletterService.unsubscribe(request);
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    
    }

    // Confirm Unsubscription endpoint
    @Operation(
        summary = "Confirm unsubscription from the newsletter",
        description = "Confirm your unsubscription from the newsletter using the the frontend confirmation. If successful, your subscription will be marked as unsubscribed."
    )
    @ApiResponses (value = {
            @ApiResponse(responseCode = "200", description = "Unsubscription successful"),
            @ApiResponse(responseCode = "404", description = "Email not found")
        }
    )
    @PostMapping(
        value = "/confirm-unsubscription",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UnsubscribeConfirmationResponse> confirmUnsubscription(@Valid @RequestBody UnsubscribeConfirmationRequest request) {
        UnsubscribeConfirmationResponse response = newsletterService.confirmUnsubscription(request.getToken());
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}