# Newsletter Service
This repository delivers a robust microservice built with **Spring Boot** for comprehensive newsletter subscription management. It allows seamless addition and removal of subscribers, dispatches confirmation emails, and generates personalized unsubscribe links for each newsletter. The service is architected to optimize subscriber management workflows and ensure adherence to industry-standard email compliance practices.

## Table of Contents

- [Newsletter Service](#newsletter-service)
  - [Table of Contents](#table-of-contents)
  - [Used Technologies](#used-technologies)
  - [Entities](#entities)
    - [Subscriber](#subscriber)
    - [VerificationToken](#verificationtoken)
    - [Classes](#classes)
  - [Repository Structure](#repository-structure)
    - [Folders structure](#folders-structure)
    - [Components](#components)
  - [Use case and flows](#use-case-and-flows)
    - [Subscription Flow](#subscription-flow)
    - [Unsubscribe Link Generation Flow](#unsubscribe-link-generation-flow)
    - [Unsubscribe Flow](#unsubscribe-flow)
  - [Endpoints](#endpoints)
  - [Security](#security)
  - [Configuration](#configuration)
  - [Docker Compose](#docker-compose)

## Used Technologies

- Java 21
- Spring Boot 3.5.6
    - Spring Web
    - Spring Data JPA
    - Spring Boot Starter Mail
    - Spring Security Starter
- PostgreSQL 15
- MailDev (development email server)
- Docker & Docker Compose

## Entities

This service defines two primary entities that represent the core data models within the system. These entities are structured to accurately capture and manage the key components involved in newsletter subscription processes.

### Subscriber

Represents an individual who is in the process.

| Parameter    | Type     | Description                                                      |
|:-------------|:---------|:-----------------------------------------------------------------|
| `id`         | UUID     | unique id                                                        |
| `email`      | String   | subscriber email                                                 |
| `status`     | Enum     | PENDING, ACTIVE, UNSUBSCRIBED                                    |
| `createdAt`  | Timestamp| subscrition date                                                 |
| `verifiedAt` | Timestamp| verification date                                                |
| `userCreated`| Boolean  | True: comes from a user account -> Don't sent confirmation email |

A subcriber has three states:

*![subscribe_state_diagrama](./out/plantUML/subscriber_state__diagram/state_diagram_subscriber.png)

### VerificationToken

Represents a token.

| Parameter     | Type     | Description                                                      |
|:--------------|:---------|:-----------------------------------------------------------------|
| `id`          | UUID     | unique id                                                        |
| `token`       | String   | safety random token                                              |
| `subscribedId`| UUID     | Id from the Subscriber                                           |
| `type`        | Enum     | CONFIRMATION, UNSUBSCRIBE                                        |
| `createdAt`   | Timestamp| creation date                                                    |
| `expireAt`    | Timestamp| expiration date                                                  |
| `used`        | Boolean  | True: if the token was used                                      |


### Classes
Additionally, this repository contains service classes that encapsulate the business logic for each core functionality. Each service is designed with a clear separation of concerns, ensuring maintainability and scalability of the application.

- **Newsletter Service**
- **Token Service**
- **Email Service**
- **Frontend Service**

![Class Diagram](./out/plantUML/class_diagram/class_diagram.png)

## Repository Structure

### Folders structure
```plaintext
newsletter-service/
├── src/
│   ├── main/
│   │   ├── java/com/raullopezpenalva/newsletter/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   │   └── NewsletterController.java
│   │   │   ├── model/
│   │   │   │   ├── Subscriber.java
│   │   │   │   ├── VerificationToken.java
│   │   │   │   ├── SubscriptionStatus.java
│   │   │   │   └── TokeType.java
│   │   │   ├── repository/
│   │   │   │   ├── SubscriberRepository.java
│   │   │   │   └── VerificationTokenRepository.java
│   │   │   ├── service/
│   │   │   │   ├── NewsletterService.java
│   │   │   │   ├── TokenService.java
│   │   │   │   └── EmailService.java
│   │   │   └── NewsletterServiceApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/java/com/raullopezpenalva/newsletter_service/
│       └── NewsletterServiceApplicationTests.java
├── target/                # maven output .jar
├── docker-compose.yml
├── Dockerfile
└── pom.xml
```
### Components

The repository is organized into distinct components to promote a clear structure and ensure scalability of the service. This modular approach facilitates maintainability, enables easier feature expansion, and supports best practices in microservice architecture.

*![Component Diagram](./out/plantUML/components_diagram/component_diagram_newsletter_service.png)

## Use case and flows

The use case are designed that the service defines 4 external entities that use the service. 

- **Annonimous subscriber**
   Is a person that only subscribes to the newsletter and not in ther others services that offers the company.
   This person has the next flows to use:
     - Subscribe to newsletter
     - Confirm subscription
     - View unsubscribe form
     - Confirm unsubcribe with reason

- **Registered User**
  Is a persona that subcribes from creating a user account in the company. This external entity don't need to confirm the subscription because is done by the user creation flow in other micro-service.
  This person has the next flows to use:
    - Subscribe to newsletter
    - Views unsubscribe form
    - Confirm unsubscribe with reason

- **Email Sending System**
  This the other micro-service that gets all the emails with active subscription and get the personal unsubscribe link for each email and send the newsletter emails with the personal unsubscribe link.
  This entity has the flows to use:
    - Get personalized unsubscribe link

- **Administrator**
  This is the administratos from the all system. It needs to enter and manage the micro-service.
  This entity has the flows to use:
    - View subscriber list
    - Views subscriber list

*![Use Case Diagram](./out/plantUML/use_case_diagram/use_case_diagram.png)

### Subscription Flow

*![subscribe_sequence_diagram](./out/plantUML/subscribe_sequence_diagram/subscribe_sequence_diagram.png)

### Unsubscribe Link Generation Flow

*![unsubcribelink_generation_sequence_diagram](./out/plantUML/unsubscribelink_generation_sequence_diagram/unsubscribeLink_generation_sequence_diagram.png)

### Unsubscribe Flow

*![unsubscribe_sequence_diagram](./out/plantUML/unsubscribe_sequence_diagram/unsubscribe_sequence_diagram.png)


## Endpoints

## Security

## Configuration

## Docker Compose