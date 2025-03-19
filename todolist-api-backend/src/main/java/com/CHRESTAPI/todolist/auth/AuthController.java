package com.CHRESTAPI.todolist.auth;

import com.CHRESTAPI.todolist.security.keycloak.KeycloakService;
import com.CHRESTAPI.todolist.security.token.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Endpoints for user authentication and session management")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final KeycloakService keycloakService;


    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Register a new user", description = "Registers a new user and sends an activation email")
    @ApiResponse(responseCode = "202", description = "User registered successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) throws MessagingException {
        authenticationService.register(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<TokenResponse> authenticate(@RequestBody AuthenticationRequest request,
                                                      HttpServletResponse response) {

        // Get tokens from Keycloak
        TokenResponse tokenResponse = authenticationService.authenticate(request);
        // Set secure cookies
        // Access token cookie
        ResponseCookie accessTokenCookie = ResponseCookie.from("ACCESS_TOKEN", tokenResponse.getAccessToken())
                .httpOnly(true)
                .secure(false) // Set to true in production with HTTPS
                .path("/")
                .maxAge(tokenResponse.getExpiresIn())
                .sameSite("Lax")
                .build();
        // Refresh token cookie (longer expiry)
        ResponseCookie refreshTokenCookie = ResponseCookie.from("REFRESH_TOKEN", tokenResponse.getRefreshToken())
                .httpOnly(true)
                .secure(false) // Set to true in production
                .path("/")
                .maxAge(tokenResponse.getExpiresIn() * 30) // Much longer than access token
                .sameSite("Lax")
                .build();

        // Add cookies to response
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        // Return tokens to frontend for now (this can be removed later for better security)
        return ResponseEntity.ok(tokenResponse);
    }

    @GetMapping("/verify-session")
    public ResponseEntity<?> verifySession(HttpServletRequest request) {
        try {
            String token = extractTokenFromCookie(request, "ACCESS_TOKEN");
            if (token != null) {
                boolean isValid = keycloakService.validateToken(token);
                if (isValid) {
                    return ResponseEntity.ok().build();
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            log.error("Error verifying session", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private String extractTokenFromCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @GetMapping("/activate-account")
    public ResponseEntity<Void> confirm(@RequestParam String token) {
        // authenticationService.activateAccount(token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Extract token from cookie
            String token = extractTokenFromCookie(request, "ACCESS_TOKEN");

            if (token != null) {
                // Log out from Keycloak
                this.keycloakService.logoutFromKeycloak(token);
            }

            // Clear access token cookie
            ResponseCookie clearAccessCookie = ResponseCookie.from("ACCESS_TOKEN", "")
                    .httpOnly(true)
                    .secure(false) // Set to true in production
                    .path("/")
                    .maxAge(0)
                    .sameSite("Lax")
                    .build();

            // Clear refresh token cookie
            ResponseCookie clearRefreshCookie = ResponseCookie.from("REFRESH_TOKEN", "")
                    .httpOnly(true)
                    .secure(false) // Set to true in production
                    .path("/")
                    .maxAge(0)
                    .sameSite("Lax")
                    .build();

            // Add cookies to response
            response.addHeader(HttpHeaders.SET_COOKIE, clearAccessCookie.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, clearRefreshCookie.toString());

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error during logout", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
