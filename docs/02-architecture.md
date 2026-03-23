# Newsletter Service Architecture

## Overview

The `newsletter-service` is designed as a **modular backend application** following a layered architecture inspired by **Domain Driven Design (DDD)** principles.

The system separates responsibilities into three main categories:

1. **Business Modules** – domain-specific functionality
2. **Platform Capabilities** – reusable technical services
3. **Shared Kernel** – common abstractions used across modules

This architecture promotes:

- separation of concerns
- modularity
- maintainability
- extensibility for future features

---

# High-Level Architecture
```
modules/
newsletter/ → Business domain module

platform/
notification/ → Cross-module notification capability
tokens/ → Token generation and validation capability

shared/
events/ → Base event abstractions
```

Each module follows a layered structure:
```
api
application
domain
infrastructure
```

---

# Layer Responsibilities

## API Layer

Location:
```
api/
```

Responsibilities:

- REST controllers
- request/response DTOs
- input validation
- mapping between DTOs and application models

This layer represents the **external interface** of the module.

---

## Application Layer

Location:
```
application/
```

Responsibilities:

- application services
- orchestration of use cases
- interaction between domain and infrastructure
- event publishing

The application layer coordinates the flow of the system but **does not contain business rules**.

---

## Domain Layer

Location:
```
domain/
```

Responsibilities:

- domain models
- domain rules
- domain events
- repository interfaces

The domain layer contains the **core business logic** and should remain independent from frameworks and infrastructure concerns.

Each module defines its own domain events inside the module.

Example:
```
modules/newsletter/domain/events/
SubscriberSubscribedEvent
SubscriberUnsubscribedEvent
```

---

## Infrastructure Layer

Location:
```
infrastructure/
```

Responsibilities:

- persistence implementation
- database repositories
- security components
- external integrations
- framework configuration

This layer contains the **technical implementation details** required by the application.

---

# Business Module: Newsletter

Location:
```
modules/newsletter
```

This module contains the business logic for managing newsletter subscriptions.

Main responsibilities:

- subscriber registration
- unsubscribe link generation
- unsubscribe flow management
- interaction with token management
- publishing domain events

Example events:
```
SubscriberSubscribedEvent
SubscriberUnsubscribedEvent
```

These events can be consumed by other modules such as notification services.

---

# Platform Modules

Platform modules provide **technical capabilities reusable across business modules**.

They are not tied to a specific business domain.

---

## Token Management

Location:
```
platform/tokens
```

Purpose:

Provide a centralized mechanism for generating, validating, and managing tokens used by different modules.

Examples of possible use cases:

- unsubscribe tokens
- email verification tokens
- password reset tokens
- invitation tokens
- temporary authentication links

Responsibilities:

- token creation
- token validation
- token expiration handling
- token persistence

Business modules use this capability instead of implementing their own token logic.

---

## Notification Module

Location:
```
platform/notification
```

Purpose:

Provide a reusable notification infrastructure decoupled from business logic.

Notifications are triggered via events.

Example flow:
```
SubscriberSubscribedEvent
↓
Notification Handler
↓
Email Notification Service
↓
SMTP Provider
```

Responsibilities:

- event listeners
- email template generation
- notification delivery
- logging and monitoring hooks

Future channels can be added easily:

- Telegram
- Push notifications
- SMS

---

# Shared Kernel

Location:
```
shared/
```

Contains abstractions shared across modules.

Examples:
```
shared/events
```

Responsibilities:

- base event classes
- event interfaces
- shared event publishing utilities

Concrete event implementations remain inside the modules that produce them.

---

# Event-Driven Design

The system uses an **event-driven approach** to decouple modules.

Example:
```
Newsletter Module
↓
Publish SubscriberSubscribedEvent
↓
Notification Module
↓
Send welcome email
```

Benefits:

- loose coupling
- extensibility
- easier feature additions

Modules communicate through events rather than direct dependencies.

---

# Architectural Goals

This architecture aims to achieve:

- **clear module boundaries**
- **separation between domain and infrastructure**
- **reusable platform capabilities**
- **event-driven extensibility**

The result is a backend system that can grow over time while remaining maintainable and modular.


```plaintext
newsletter-service/
├── src/
│   ├── main/
│   │   ├── java/com/raullopezpenalva/newsletter_service/
│   │   │   ├── modules/
│   │   │   │   ├── newsletter/
│   │   │   │   │   ├── api/
│   │   │   │   │   │   ├── controller/
│   │   │   │   │   │   ├── dto/
│   │   │   │   │   │   └── error/
│   │   │   │   │   ├── application/
│   │   │   │   │   │   ├── exception/
│   │   │   │   │   │   ├── mapper/
│   │   │   │   │   │   ├── model/
│   │   │   │   │   │   └── service/
│   │   │   │   │   ├── domain/
│   │   │   │   │   │   ├── model/
│   │   │   │   │   │   └── events
│   │   │   │   │   └── infrastructure/
│   │   │   │   │       ├── config/
│   │   │   │   │       ├── repository/
│   │   │   │   │       └── security/
│   │   │   │   └── platform/
│   │   │   │       ├── notification/
│   │   │   │       │    ├── api/
│   │   │   │       │    ├── application/
│   │   │   │       │    ├── domain/
│   │   │   │       │    └── infrastructure/
│   │   │   │       └── tokens/
│   │   │   │               ├── api/
│   │   │   │               ├── application/
│   │   │   │               ├── domain/
│   │   │   │               └── infrastructure/
│   │   │   └── shared/
│   │   │       └── events/
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── docker-compose.yml
├── Dockerfile
└── pom.xml
```