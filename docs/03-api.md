# Newsletter Service API

## Overview

This document describes the main HTTP API exposed by the `newsletter-service`.

The API supports the core newsletter lifecycle:

- subscribe to newsletter
- confirm subscription
- generate unsubscribe links
- verify unsubscribe token
- confirm unsubscription

The service uses DTO-based contracts and consistent error responses.

---

## Base URL and Versioning

All endpoints are versioned under:

```http
/api/v1
```
Main base path:
```
/api/v1/newsletter
```
---
## Security

The service currently exposes both public and administrative endpoints.

**Public endpoints**

Public endpoints are intended for:

- subscribers

- frontend clients

- external email sending services

**Admin endpoints**

Administrative endpoints are intended for internal management use and may require authentication depending on the current configuration.

---
## Public Endpoints
### 1. Subscribe to newsletter
Endpoint
```
POST /api/v1/newsletter/subscribe
```
Description

Creates or processes a newsletter subscription request.

Depending on the current state of the email, the result may be:

- new subscriber created

- existing subscriber already registered

- confirmation still pending

**Request DTO**
```json
{
  "email": "user@example.com"
}
```
**Response DTO example**
```
{
  "status": "CREATED",
  "message": "Subscriber created successfully",
  "email": "user@example.com"
}
```
### Possible business statuses

- `SUBSCRIBED`

- `ALREADY_EXISTS`

- `CONFIRMATION_PENDING`

Common HTTP responses

- `200 OK`

- `400 Bad Request`

- `500 Internal Server Error`

### 2. Confirm subscription token
Endpoint
```
GET /api/v1/newsletter/confirm?token=<token>
```
**Description**

Validates a confirmation token and activates the subscriber if the token is valid.

**Example request**
```
POST /api/v1/newsletter/confirm?token=abc123
```
**Response DTO example**
```json
{
  "confirmed": true,
  "message": "Subscription confirmed successfully",
  "email": "user@example.com"
}
```

**Common HTTP responses**

- `200 OK`

- `400 Bad Request`

- `404 Not Found`

- `500 Internal Server Error`

### 3. Generate unsubscribe links
Endpoint
```
GET /api/v1/newsletter/generate-links
```
**Description**

- Generates personalized unsubscribe links for all subscribers with status ACTIVE.

- This endpoint is intended for the external email sending system.

**Response DTO example**
```json
{
  "generatedAt": "2026-03-05T10:00:00Z",
  "count": 2,
  "links": [
    {
      "email": "user1@example.com",
      "unsubscribeUrl": "https://<frontend-domain>/unsubscribe?token=<token>"
    },
    {
      "email": "user2@example.com",
      "unsubscribeUrl": "https://<frontend-domain>/unsubscribe?token=<token>"
    }
  ]
}
```
**Notes**

- Only ACTIVE subscribers are included

- This endpoint replaced the previous multi-request unsubscribe link generation flow

**Common HTTP responses**

- `200 OK`

- `500 Internal Server Error`

### 4. Unsubscribe flow
Endpoint
```
POST /api/v1/newsletter/unsubscribe
```

**Description**

Validates an unsubscribe token before showing the unsubscribe confirmation UI in the frontend.

This endpoint does not change system state.

**Example request**
```json
{
    "token": "abc123"
}
```
**Response DTO example**
```json
{
  "token": "abc123",
  "email": "user@example.com",
}
```
**Common HTTP responses**

- `200 OK`

- `400 Bad Request`

- `404 Not Found`

- `500 Internal Server Error`

### 5. Confirm unsubscription
Endpoint
```
POST /api/v1/newsletter/confirm-unsubscription
```

**Description**

Confirms the unsubscribe action for the subscriber associated with the token.

This endpoint changes system state:

- token is validated again

- token is invalidated / marked as used

- subscriber status is updated to UNSUBSCRIBED

**Example request**
```json
{
    "email": "email@email.com",
    "token": "abc123"
}
```
**Response DTO example**
```json
{
  "id": "abc123",
  "email": "user@example.com",
  "status": "SubscriptionStatis"
}
```

**Common HTTP responses**

- `200 OK`

- `400 Bad Request`

- `404 Not Found`

- `500 Internal Server Error`
---
## Admin Endpoints
### 1. Get subscribers
Endpoint
```
GET /api/v1/admin/newsletter/subscribers
```
**Description**

Returns the subscriber list pagabled for administrative usage with status parameter and pagable parameters (size and page)

**Resquest example**
```http
GET /api/v1/admin/newsletter/subscribers?status=<status>&page=0&size=5
```

**Response DTO example**
```json
{
    "content": [
        {
            "id": "3b9cba0c-e53f-4300-9eac-967f3fb94e78",
            "email": "test@test.com",
            "status": "ACTIVE",
            "createdAt": "2026-03-17T08:09:17.131318",
            "verifiedAt": "2026-03-17T08:09:17.128986",
            "updatedAt": null
        },
        {
            "id": "7fcb2253-524c-47c6-bc87-cc405e68d6d1",
            "email": "prueba-front4-subscribe@test.com",
            "status": "ACTIVE",
            "createdAt": "2026-03-16T16:24:46.901035",
            "verifiedAt": "2026-03-16T17:20:17.869231",
            "updatedAt": null
        },
        {
            "id": "b38460dd-55fa-4292-8898-5c68ca299682",
            "email": "prueba-front3-subscribe@test.com",
            "status": "ACTIVE",
            "createdAt": "2026-03-16T16:04:37.785561",
            "verifiedAt": "2026-03-16T17:31:55.958474",
            "updatedAt": null
        },
        {
            "id": "58b6a6e8-235c-48f1-8d32-a14e20ee3574",
            "email": "prueba-front2-subscribe@test.com",
            "status": "ACTIVE",
            "createdAt": "2026-03-16T15:52:10.939051",
            "verifiedAt": "2026-03-16T15:52:10.937531",
            "updatedAt": null
        },
        {
            "id": "399b800a-6065-4743-ab8c-5b1377153d91",
            "email": "prueba-front1-subscribe@test.com",
            "status": "ACTIVE",
            "createdAt": "2026-03-16T15:47:58.402862",
            "verifiedAt": "2026-03-16T17:32:30.836732",
            "updatedAt": null
        }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 5,
        "sort": {
            "sorted": true,
            "empty": false,
            "unsorted": false
        },
        "offset": 0,
        "paged": true,
        "unpaged": false
    },
    "totalElements": 6,
    "totalPages": 2,
    "last": false,
    "size": 5,
    "number": 0,
    "sort": {
        "sorted": true,
        "empty": false,
        "unsorted": false
    },
    "numberOfElements": 5,
    "first": true,
    "empty": false
}
```
2. Get subscriber by id
Endpoint
```
GET /api/v1/admin/subscribers/{id}
```
**Description**

Returns subscriber data for a specific id.

**Common Response Patterns**
**Success responses**

Success responses use dedicated response DTOs specific to each use case.

Examples:

- SubscribeResponseDTO

- GenerateLinksResponseDTO

- UnsubscribeLinkDTO

- ConfirmSubscriptionResponseDTO

- VerifyUnsubscribeTokenResponseDTO

- ConfirmUnsubscriptionResponseDTO

**Error responses**

All errors should return a consistent error format.

---
## Error Response Format

Example:
```json
{
  "timestamp": "2026-03-12T16:43:26.369221749Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/v1/newsletter/subscribe",
  "details": [
    {
      "field": "email",
      "message": "must not be blank"
    }
  ]
}
```
Fields

- timestamp → error timestamp

- status → HTTP status code

- error → HTTP error name

- message → general error message

- path → request path

- details → field-level or domain-level error details

---
## Swagger / OpenAPI

The API is documented with SpringDoc OpenAPI.

Swagger UI
```
http://localhost:8080/swagger-ui.html
```
OpenAPI JSON
```
http://localhost:8080/v3/api-docs
```

Swagger should be treated as the executable reference for the current implementation, while this document provides a higher-level explanation of the API design.