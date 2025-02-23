package com.CHRESTAPI.todolist.security.keycloak;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application.keycloak")
@Data
public class KeycloakProperties {
    private String realm;
    private String authServerUrl;
    private String resource;
    private Credentials credentials;

    @Data
    public static class Credentials {
        private String secret;
    }
}
