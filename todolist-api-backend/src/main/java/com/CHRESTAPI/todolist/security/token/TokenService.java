package com.CHRESTAPI.todolist.security.token;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("http://localhost:8088/api/v1")  // Make sure this matches your security config
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        log.debug("Generated token: {}", token);
        return token;
    }

    public boolean validateToken(String token) {
        try {
            log.debug("Attempting to validate token");
            Jwt jwt = jwtDecoder.decode(token);
            log.debug("Decoded token claims: {}", jwt.getClaims());

            // Validate expiration
            Instant expiration = jwt.getExpiresAt();
            if (expiration == null || expiration.isBefore(Instant.now())) {
                log.debug("Token is expired");
                return false;
            }

            // Validate issuer
            String issuer = jwt.getIssuer() != null ? jwt.getIssuer().toString() : null;
            if (!"http://localhost:8088/api/v1".equals(issuer)) {
                log.debug("Invalid issuer: {}", issuer);
                return false;
            }

            log.debug("Token is valid");
            return true;
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            String username = jwt.getSubject();
            log.debug("Extracted username from token: {}", username);
            return username;
        } catch (Exception e) {
            log.error("Failed to extract username: {}", e.getMessage());
            throw e;
        }
    }

    public Jwt decodeToken(String token) {
    try {
        log.info("Attempting to decode token: {}", token);
        Jwt jwt = jwtDecoder.decode(token);
        log.info("Successfully decoded token. Claims: {}", jwt.getClaims());
        return jwt;
    } catch (Exception e) {
        log.error("Failed to decode token: {} with error: {}", token, e.getMessage());
        throw e;
    }
}
}
