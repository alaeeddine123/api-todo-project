package com.CHRESTAPI.todolist.auth;


import com.CHRESTAPI.todolist.models.TokenRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication")
public class AuthController {

    private final AuthenticationService authenticationService;


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request) throws MessagingException, javax.mail.MessagingException {
        authenticationService.register(request);
        return ResponseEntity.accepted().build();
    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse response
    ) {
        var authResponse = authenticationService.authenticate(request);

        ResponseCookie cookie = ResponseCookie.from("jwt", authResponse.getToken())
                .httpOnly(true)
                .secure(false)  // Set to true in production
                .path("/")
                .domain("localhost")
                .maxAge(Duration.ofHours(1))
                .sameSite("Lax")
                .build();

        // Set cookie using response header
        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/verify-session")
    public ResponseEntity<Void> verifySession(@CookieValue(name = "jwt", required = false) String token, HttpServletRequest request
    ) {
        log.info("Verify session endpoint called with token", token);

        // Get JWT from cookie if not already present
        if (token == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("jwt".equals(cookie.getName())) {
                        token = cookie.getValue(); // Correctly assign to token
                        break;
                    }
                }
            }
        }

        if (token == null) {
            log.info("No JWT cookie found");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        log.info("Verifying token...");
        boolean isValid = authenticationService.verifySession(token);
        log.info("Token validation result: {}", isValid);

        if (isValid) {
            log.info("Token is valid, returning OK");
            return ResponseEntity.ok().build();
        }

        log.info("Token is invalid, returning UNAUTHORIZED");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/activate-account")
    public void confirm(@RequestParam String token) throws MessagingException, javax.mail.MessagingException {
        authenticationService.activateAccount(token);
    }


    @PostMapping("/set-cookie")
    public ResponseEntity<Void> setCookie(@RequestBody TokenRequest tokenRequest, HttpServletResponse response) {
        String token = tokenRequest.getToken();

        // Create and set the JWT cookie
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);  // Set to false for local development
        cookie.setPath("/");
        cookie.setMaxAge(3600);  // 1 hour
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        // Create a new cookie with the same name, but set max age to 0 to delete it
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false)  // Set to true in production
                .path("/")
                .domain("localhost")
                .maxAge(0)  // Immediate expiration
                .sameSite("Lax")
                .build();

        // Add the cookie to the response
        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.noContent().build(); // Return a no-content response
    }

}
