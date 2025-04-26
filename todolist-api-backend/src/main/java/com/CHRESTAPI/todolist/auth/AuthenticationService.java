package com.CHRESTAPI.todolist.auth;

import com.CHRESTAPI.todolist.entities.User;
import com.CHRESTAPI.todolist.repositories.UserRepository;
import com.CHRESTAPI.todolist.security.keycloak.KeycloakService;
import com.CHRESTAPI.todolist.security.token.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;
    private final KeycloakService keycloakService;

    public void register(RegisterRequest request)  {
        // Register in Keycloak first
        keycloakService.registerUser(request);

        // Then store additional user info in your database
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .accountLocked(false)
                .enabled(true)
                .dateOfBirth(LocalDate.now())
                .build();
        userRepository.save(user);
    }

     public TokenResponse authenticate(AuthenticationRequest request) {
        return keycloakService.authenticate(request);
    }

      /**
     * Sync user info from JWT token to local database if needed
     */
    public User syncUserFromToken(Jwt jwt) {
        String email = jwt.getClaimAsString("email");
        if (email == null) {
            log.warn("No email found in JWT token");
            return null;
        }

        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            return existingUser.get();
        } else {
            // User not in our database, create from token info
            String firstName = jwt.getClaimAsString("given_name");
            String lastName = jwt.getClaimAsString("family_name");

            User newUser = User.builder()
                    .firstname(firstName != null ? firstName : "")
                    .lastname(lastName != null ? lastName : "")
                    .email(email)
                    .accountLocked(false)
                    .enabled(true)
                    .dateOfBirth(LocalDate.now())
                    .build();

            return userRepository.save(newUser);
        }
    }
}
