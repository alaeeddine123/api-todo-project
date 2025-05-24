package com.CHRESTAPI.todolist.auth;

import com.CHRESTAPI.todolist.entities.User;
import com.CHRESTAPI.todolist.repositories.UserRepository;
import com.CHRESTAPI.todolist.security.keycloak.KeycloakService;
import com.CHRESTAPI.todolist.security.token.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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

    public void  logout( String token){
        keycloakService.logoutFromKeycloak(token);
    }
}
