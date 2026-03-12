FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY . .

RUN mvn clean package

FROM dhi.io/eclipse-temurin:21 AS service

WORKDIR /app

COPY --from=build /app/target/newsletter-service-0.0.1-SNAPSHOT.jar newsletter-service.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "newsletter-service.jar"]