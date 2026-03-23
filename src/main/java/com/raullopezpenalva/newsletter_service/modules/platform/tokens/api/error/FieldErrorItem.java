package com.raullopezpenalva.newsletter_service.modules.platform.tokens.api.error;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "FieldErrorItem", description = "Represents a validation error for a specific field in the request.")
public record FieldErrorItem(
    String field,
    String message
) {}
