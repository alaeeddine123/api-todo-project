package com.CHRESTAPI.todolist.security.keycloak;


import com.CHRESTAPI.todolist.auth.AuthenticationRequest;
import com.CHRESTAPI.todolist.auth.RegisterRequest;
import com.CHRESTAPI.todolist.security.token.TokenResponse;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.TokenVerifier;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.common.util.Time;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakService {
    private final Keycloak keycloak;
    private final KeycloakProperties keycloakProperties;
    private final KeycloakClientProperties keycloakClientProperties;

    public void registerUser(RegisterRequest request) {
        try {
            log.info("Attempting to register user with email: {}", request.getEmail());

            UserRepresentation user = new UserRepresentation();
            user.setEnabled(true);
            user.setUsername(request.getEmail());
            user.setEmail(request.getEmail());
            user.setFirstName(request.getFirstname());
            user.setLastName(request.getLastname());

            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(request.getPassword());
            credential.setTemporary(false);
            user.setCredentials(List.of(credential));

            UsersResource usersResource = keycloak.realm(keycloakProperties.getRealm()).users();
            Response response = usersResource.create(user);

            if (response.getStatus() == 201) {
                String userId = CreatedResponseUtil.getCreatedId(response);
                log.info("User created with ID: {}", userId);

                try {
                    RoleRepresentation userRole = keycloak.realm(keycloakProperties.getRealm())
                            .roles()
                            .get("USER")
                            .toRepresentation();

                    usersResource.get(userId).roles()
                            .realmLevel()
                            .add(Collections.singletonList(userRole));

                    log.info("USER role assigned to user: {}", request.getEmail());
                } catch (Exception e) {
                    log.error("Failed to assign USER role", e);
                }

                log.info("User registered successfully: {}", request.getEmail());
            } else {
                String responseBody = response.readEntity(String.class);
                log.error("Failed to register user. Status: {} Body: {}",
                        response.getStatus(), responseBody);
                throw new RuntimeException("User registration failed");
            }
        } catch (WebApplicationException e) {
            log.error("WebApplicationException during user registration", e);
            throw new RuntimeException("User registration failed: " + e.getMessage(), e);
        }
    }


    public TokenResponse authenticate(AuthenticationRequest request) {
        try {
            Keycloak keycloakClient = Keycloak.getInstance(
                    keycloakProperties.getAuthServerUrl(),
                    keycloakProperties.getRealm(),
                    request.getEmail(),
                    request.getPassword(),
                    keycloakClientProperties.getClientId(),
                    keycloakClientProperties.getClientSecret());

            AccessTokenResponse response = keycloakClient.tokenManager().getAccessToken();

            log.info("Authentication successful for user: {}", request.getEmail());

            return TokenResponse.builder()
                    .accessToken(response.getToken())
                    .refreshToken(response.getRefreshToken())
                    .expiresIn(response.getExpiresIn())
                    .build();
        } catch (Exception e) {
            log.error("Authentication failed for user: {}", request.getEmail(), e);
            throw new RuntimeException("Authentication failed", e);
        }
    }

    public boolean validateToken(String token) {
    try {
        // Basic JWT validation
        TokenVerifier<AccessToken> verifier = TokenVerifier.create(token, AccessToken.class);
        AccessToken accessToken = verifier.getToken();

        // Check if the token has expired
        int currentTime = Time.currentTime();
        if (accessToken.getExp() <= currentTime) {
            return false;
        }

        // Create a new HTTP client to validate the token with Keycloak server
        RestTemplate restTemplate = new RestTemplate();

        // Build the introspection endpoint URL
        String introspectionUrl = keycloakProperties.getAuthServerUrl() +
            "/realms/" + keycloakProperties.getRealm() +
            "/protocol/openid-connect/token/introspect";

        // Prepare headers and form data
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Add client authentication
        String auth = keycloakClientProperties.getClientId() + ":" + keycloakClientProperties.getClientSecret();
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.US_ASCII));
        String authHeader = "Basic " + new String(encodedAuth);
        headers.set("Authorization", authHeader);

        // Form data
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("token", token);
        formData.add("token_type_hint", "access_token");

        // Create request entity
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

        // Make the request
        ResponseEntity<Map> response = restTemplate.exchange(
            introspectionUrl,
            HttpMethod.POST,
            requestEntity,
            Map.class
        );

        // Check the response
        if (response.getBody() != null && response.getBody().containsKey("active")) {
            Boolean active = (Boolean) response.getBody().get("active");
            return Boolean.TRUE.equals(active);
        }

        return false;
    } catch (Exception e) {
        log.error("Error validating token", e);
        return false;
    }
}

    public void logoutFromKeycloak(String token) {
    try {
        RestTemplate restTemplate = new RestTemplate();

        // Build the logout endpoint URL
        String logoutUrl = keycloakProperties.getAuthServerUrl() +
            "/realms/" + keycloakProperties.getRealm() +
            "/protocol/openid-connect/logout";

        // Prepare headers and form data
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Add client authentication
        String auth = keycloakClientProperties.getClientId() + ":" + keycloakClientProperties.getClientSecret();
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.US_ASCII));
        String authHeader = "Basic " + new String(encodedAuth);
        headers.set("Authorization", authHeader);

        // Form data
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("token", token);
        formData.add("client_id", keycloakClientProperties.getClientId());
        formData.add("client_secret", keycloakClientProperties.getClientSecret());

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

        restTemplate.exchange(
            logoutUrl,
            HttpMethod.POST,
            requestEntity,
            String.class
        );

        log.info("Successfully logged out from Keycloak");
    } catch (Exception e) {
        log.error("Error logging out from Keycloak", e);
    }
}
}