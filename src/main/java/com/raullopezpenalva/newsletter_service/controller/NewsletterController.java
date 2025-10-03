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

    // Get Active Subscribers endpoint
    @Operation(
        summary = "Get active subscribers",
        description = "Retrieve a list of all active subscribers to the newsletter."
    )
    @ApiResponses (value = {
            @ApiResponse(responseCode = "200", description = "List of active subscribers retrieved")
    })
    @GetMapping("/subscribers")
    public ResponseEntity<Iterable<Subscriber>> getActiveSubscribers() {
        Iterable<Subscriber> subscribers = newsletterService.getActiveSubscribers();
        return ResponseEntity.ok(subscribers);
    }

    // UnsubscribeLink Generation endpoint
    @Operation(
        summary = "Generate unsubscribe link",
        description = "Generate an unsubscribe link for a given email address. This link can be used to unsubscribe from the newsletter."
    )
    @ApiResponses (value = {
            @ApiResponse(responseCode = "200", description = "Unsubscribe link generated"),
            @ApiResponse(responseCode = "404", description = "Email not found")
        }
    )
    @GetMapping("/generate-unsubscribe-link")
    public ResponseEntity<Map<String, Object>> generateUnsubscribeLink(@RequestParam String email) {
        var result = newsletterService.generateUnsubscribeLink(email);
        if (result.success() == true) {
            Map<String, Object> payload = Map.<String, Object>of(
                "status", "link_generated",
                "unsubscribe_link", result.link()
            );
            return ResponseEntity.ok(payload);
        } else {
            Map<String, Object> payload = Map.<String, Object>of(
                "status", "email_not_found",
                "message", result.link()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(payload);
        }
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
    @GetMapping("/unsubscribe")
    public ResponseEntity<String> unsubscribe(@RequestParam String token) {
        var result = newsletterService.unsubscribe(token);
        if (result.success()) {
            String html = """
                    <!DOCTYPE html>
                    <html lang="en">
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>Confirm Unsubscribe - Newsletter Service</title>
                        <style>
                            body {
                                font-family: Arial, sans-serif;
                                background-color: #f9f9f9;
                                display: flex;
                                justify-content: center;
                                align-items: center;
                                height: 100vh;
                                margin: 0;
                            }
                            h1 {
                                color: #333;
                                font-size: 2rem;
                                text-align: center;
                                margin-bottom: 1rem;
                            }
                            .container {
                                text-align: center;
                                padding: 2rem;
                                border: 2px solid #000000;
                                background-color: #d6d4d4;
                                border-radius: 8px;
                                margin-top: 10px;
                            }
                            p {
                                color: #434444;
                                text-decoration: none;
                                font-weight: bold;
                            }
                            button {
                                background-color: #8f9091;
                                color: white;
                                border: none;
                                padding: 0.5rem 1rem;
                                border-radius: 4px;
                                cursor: pointer;
                                font-size: 1rem;
                            }
                            button:hover {
                                background-color: #5e5e5f;
                            }
                        </style>
                    </head>
                    <body>
                        <div class="container">
                            <h1>Confirm Unsubscribe</h1>
                            <p>Are you sure you want to unsubscribe from our newsletter?</p>
                            <form action="/newsletter/confirm-unsubscription" method="GET">
                                <input type="hidden" name="token" value="%s">
                                <button type="submit">Yes, Unsubscribe Me</button>
                            </form>
                        </div>
                    """.formatted(token);
        return ResponseEntity.ok().body(html);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("<h1>Invalid or expired token. Unsubscription failed.</h1>");
        }
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
    @GetMapping("/confirm-unsubscription")
    public ResponseEntity<Map<String, Object>> confirmUnsubscription(@RequestParam String token) {
        var result = newsletterService.confirmUnsubscription(token);
        if (result.success()) {
            Map<String, Object> payload = Map.<String, Object>of(
                "status", "unsubscribed",
                "message", result.message()
            );
            return ResponseEntity.ok(payload);
        } else {
            Map<String, Object> payload = Map.<String, Object>of(
                "status", "failed",
                "message", result.message()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(payload);
        }
    }

}