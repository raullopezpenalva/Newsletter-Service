# Newsletter Service

A modular Spring Boot backend service for managing newsletter subscriptions, confirmation flows, and unsubscribe mechanisms.

## 🚀 Features

- Subscribe with email
- Email confirmation flow
- Unsubscribe flow with secure tokens
- Event-driven notification system
- Modular architecture (DDD-inspired)

## 🏗 Architecture

This service follows a modular architecture:

- `newsletter` → business logic
- `platform/tokens` → token management
- `platform/notification` → notifications
- `shared` → common abstractions

👉 See full architecture:
[docs/architecture.md](docs/architecture.md)

## 🔄 Main Flows

- Subscribe
- Confirm subscription
- Generate unsubscribe links
- Unsubscribe

👉 Detailed flows:
[docs/flows.md](docs/flows.md)

## 📡 API Documentation

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

👉 Full API details:
[docs/api.md](docs/api.md)

## ⚙️ Run locally

```bash
docker-compose up --build
```


## License

MIT License