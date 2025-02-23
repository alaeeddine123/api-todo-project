package com.CHRESTAPI.todolist.security.keycloak;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class KeycloakConfig {
     private final KeycloakProperties keycloakProperties;

    @Bean
    public Keycloak keycloak() {
        log.info("Realm: {}", keycloakProperties.getRealm());
        log.info("Auth Server URL: {}", keycloakProperties.getAuthServerUrl());
        log.info("Resource: {}", keycloakProperties.getResource());
        log.info("Client Secret: {}", keycloakProperties.getCredentials().getSecret());

        try {
            return KeycloakBuilder.builder()
                .serverUrl(keycloakProperties.getAuthServerUrl())
                .realm(keycloakProperties.getRealm())
                .clientId(keycloakProperties.getResource())
                .clientSecret(keycloakProperties.getCredentials().getSecret())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .build();
        } catch (Exception e) {
            log.error("Failed to create Keycloak instance", e);
            throw new RuntimeException("Failed to create Keycloak instance", e);
        }
    }
}