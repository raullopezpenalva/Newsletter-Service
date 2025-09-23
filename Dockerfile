FROM gcr.io/distroless/java21-debian12:nonroot

WORKDIR /app

COPY target/newsletter-service-0.0.1-SNAPSHOT.jar newsletter-service.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "newsletter-service.jar"]