package com.example.Bookstore.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "User API",
                version = "Version 1.0",
                contact = @Contact(
                        name = "Tushar Dutta",
                        email = "tushar.dutta1395@gmail.com",
                        url = "https://www.amazon.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licence/LICENSE-2.0"
                ),
                termsOfService = "https://www.amazon.com",
                description = "Bookstore Project using Swagger UI"
        )
)
public class SwaggerConfig {
}
