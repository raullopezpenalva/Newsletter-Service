package com.raullopezpenalva.newsletter_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;


@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI newsletterOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Newsletter Service API")
                        .version("1.0")
                        .description("API documentation for the Newsletter Service")
                        .contact(new Contact()
                                .name("Raul Lopez Penalva")
                                .email("raullopezpenalva@icloud.com")
                                .url("https://raullopezpenalva.com")));
    }
}
