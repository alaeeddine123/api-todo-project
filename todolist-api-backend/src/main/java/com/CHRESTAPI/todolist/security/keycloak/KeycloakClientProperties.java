package com.CHRESTAPI.todolist.security.keycloak;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.keycloak")
@Data
public class KeycloakClientProperties {
    private String clientId;
    private String clientSecret;
    private String authorizationGrantType;
    private String scope;
    private String redirectUri;

    @Data
    public static class Credentials {
        private String secret;
    }
}
