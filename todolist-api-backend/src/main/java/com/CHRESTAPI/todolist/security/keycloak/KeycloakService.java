package com.CHRESTAPI.todolist.security.keycloak;


import com.CHRESTAPI.todolist.auth.AuthenticationRequest;
import com.CHRESTAPI.todolist.auth.RegisterRequest;
import com.CHRESTAPI.todolist.security.token.TokenResponse;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakService {
    private final Keycloak keycloak;
    private final KeycloakProperties keycloakProperties;


   /* public void registerUser(RegisterRequest request) {
        try {
            // Detailed logging of input
            log.info("Attempting to register user with email: {}", request.getEmail());
            log.info("Keycloak Realm: {}", keycloakProperties.getRealm());
            log.info("Auth Server URL: {}", keycloakProperties.getAuthServerUrl());
            log.info("Client ID: {}", keycloakProperties.getResource());

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

            // Verify roles exist before attempting to assign
            try {
                RoleRepresentation userRole = keycloak.realm(keycloakProperties.getRealm())
                    .roles()
                    .get("default-roles-todolist-app")
                    .toRepresentation();

                log.info("User role found: {}", userRole.getName());
            } catch (Exception roleException) {
                log.error("Failed to retrieve USER role", roleException);
                throw new RuntimeException("USER role not found in realm");
            }

            UsersResource usersResource = keycloak.realm(keycloakProperties.getRealm()).users();

            Response response = usersResource.create(user);

            log.info("User creation response status: {}", response.getStatus());

            if (response.getStatus() == 201) {
                String userId = CreatedResponseUtil.getCreatedId(response);

                // Assign default role
                RoleRepresentation userRole = keycloak.realm(keycloakProperties.getRealm())
                    .roles()
                    .get("USER")
                    .toRepresentation();

                usersResource.get(userId).roles().realmLevel().add(Collections.singletonList(userRole));

                log.info("User registered successfully: {}", request.getEmail());
            } else {
                // Read response body for more details
                String responseBody = response.readEntity(String.class);
                log.error("Failed to register user. Response status: {}", response.getStatus());
                log.error("Response body: {}", responseBody);
                throw new RuntimeException("User registration failed. Status: " + response.getStatus());
            }
        } catch (WebApplicationException e) {
            log.error("WebApplicationException during user registration", e);
            log.error("Error response: {}", e.getResponse().readEntity(String.class));
            throw new RuntimeException("User registration failed due to authorization error", e);
        } catch (Exception e) {
            log.error("Unexpected error during user registration", e);
            throw new RuntimeException("User registration failed", e);
        }
    }*/


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
                    // Get the default role (USER)
                    RoleRepresentation userRole = keycloak.realm(keycloakProperties.getRealm())
                        .roles()
                        .get("USER")
                        .toRepresentation();

                    // Assign the role to the user
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
                keycloakProperties.getResource(),
                keycloakProperties.getCredentials().getSecret()
            );

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
}