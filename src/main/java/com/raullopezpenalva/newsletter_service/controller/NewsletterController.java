package com.raullopezpenalva.newsletter_service.controller;

import com.raullopezpenalva.newsletter_service.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.raullopezpenalva.newsletter_service.service.NewsletterService;
import org.springframework.http.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/newsletter")
public class NewsletterController {

    @Autowired
    private NewsletterService newsletterService;

    @GetMapping("/admin/subscribers")
    public ResponseEntity<List<Subscriber>> getAllSubscribers() {
        List<Subscriber> subscribers = newsletterService.getAllSubscribers();
        return ResponseEntity.ok(subscribers);
    }

    @PostMapping("/subscribe")
    public ResponseEntity<Map<String, Object>> subscribe(@RequestBody Subscriber bodySubscriber) {
        var result = newsletterService.subscribe(bodySubscriber.getEmail()); // Subscribe email; result.existed() true if already present

        Map<String, Object> payload = Map.<String, Object>of(
            "status", result.gestStatus(),
            "email", result.getEmail(),
            "from user", result.isUserCreated()
        );
        if (result.getStatus() == SubscriptionStatus.ACTIVE) {
            payload.put("message", "You are already subscribed.");
            return ResponseEntity.status(HttpStatus.OK).body(payload);
        }

        return ResponseEntity
            .status(result.existed() ? HttpStatus.OK : HttpStatus.CREATED)
            .body(payload);
    }

    @GetMapping("/verify")
    public ResponseEntity<Map<String, Object>> confirmSubscription(@RequestParam String token) {
        var result = newsletterService.confirmSubscription(token);
        if (result.success() == true) {
            Map<String, Object> payload = Map.<String, Object>of(
                "status", "ACTIVE",
                "message", result.getMessage()
            );
            return ResponseEntity.ok(payload);
        } else {
            Map<String, Object> payload = Map.<String, Object>of(
                "status", "error",
                "messge", result.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(payload);
        }

    }
}