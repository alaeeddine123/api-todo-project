package com.CHRESTAPI.todolist.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        contact = @Contact(
            name = "Charrik Alae-eddine",
            email = "alaeeddine.charrik@gmail.com"
        ),
        description = "Open Api documentation for the task App",
        title = "Open Api specs - CHARRIK",
        version = "1.0",
        license = @License(
            name = "Licence name",
            url = "http://test-license.com"
        ),
        termsOfService = "Terms of service"
    ),
    servers = {
        @Server(
            description = "Local Env",
            url = "http://localhost:8088/api/v1"
        ),
        @Server(
            description = "Production Env",
            url = "http://mytasky.com/app"
        )
    },
    security = {
        @SecurityRequirement(name = "bearerAuth")
    }
)
@SecurityScheme(
    name = "bearerAuth",
    description = "JWT auth description",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT"
)
public class OpenAPiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSchemas("AuthenticationRequest", createAuthRequestSchema())
                        .addSchemas("AuthenticationResponse", createAuthResponseSchema())
                );
    }

    private Schema createAuthRequestSchema() {
        Schema schema = new Schema<Object>()
                .type("object")
                .addProperties("email", new Schema<String>()
                        .type("string")
                        .example("user@example.com")
                        .description("User's email address"))
                .addProperties("password", new Schema<String>()
                        .type("string")
                        .example("password123")
                        .description("User's password"));
        schema.setRequired(java.util.Arrays.asList("email", "password"));
        return schema;
    }

    private Schema createAuthResponseSchema() {
        return new Schema<Object>()
                .type("object")
                .addProperties("token", new Schema<String>()
                        .type("string")
                        .example("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
                        .description("JWT token"));
    }
}