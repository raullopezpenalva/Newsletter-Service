package com.raullopezpenalva.newsletter_service.controller;

import com.raullopezpenalva.newsletter_service.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.raullopezpenalva.newsletter_service.service.NewsletterService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.http.*;

import java.util.Map;

@RestController
@RequestMapping("/newsletter")
public class NewsletterController {

    @Autowired
    private NewsletterService newsletterService;

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
    @PostMapping("/subscribe")
    public ResponseEntity<Map<String, Object>> subscribe(@RequestBody Subscriber bodySubscriber) {
        var result = newsletterService.subscribe(bodySubscriber); // Subscribe email; result.existed() true if already present

        Map<String, Object> payload = Map.<String, Object>of(
            "status", result.finalStatus(),
            "message", switch (result.finalStatus()) {
                case "already_subscribed" -> "You are already subscribed.";
                case "subscribed" -> "You have been successfully subscribed.";
                case "confirmation_email_sent" -> "A confirmation email has been sent to your email address.";
                default -> "Unknown status.";
            }
        );
        if (result.finalStatus() == "already_subscribed") {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(payload);
        } else if (result.finalStatus() == "confirmation_email_sent") {
            return ResponseEntity.status(HttpStatus.OK).body(payload);

        }
        return ResponseEntity.status(HttpStatus.CREATED).body(payload);
    }

    @Operation(
        summary = "Confirm newsletter subscription",
        description = "Confirm your newsletter subscription using the token sent to your email. If the token is valid, your subscription will be activated."
    )
    @ApiResponses (value = {
            @ApiResponse(responseCode = "200", description = "Subscription confirmed"),
            @ApiResponse(responseCode = "400", description = "Invalid or expired token")
        }
    )
    @GetMapping("/verify")
    public ResponseEntity<Map<String, Object>> confirmSubscription(@RequestParam String token) {
        var result = newsletterService.confirmSubscription(token);
        if (result.success() == true) {
            Map<String, Object> payload = Map.<String, Object>of(
                "status", "ACTIVE",
                "message", result.message()
            );
            return ResponseEntity.ok(payload);
        } else {
            Map<String, Object> payload = Map.<String, Object>of(
                "status", "FAILED",
                "messge", result.message()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(payload);
        }

    }


}