package com.raullopezpenalva.newsletter_service.controller;

import com.raullopezpenalva.newsletter_service.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.raullopezpenalva.newsletter_service.service.NewsletterService;
import org.springframework.http.*;

import java.util.Map;

@RestController
@RequestMapping("/newsletter")
public class NewsletterController {

    @Autowired
    private NewsletterService newsletterService;

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