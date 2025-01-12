package com.CHRESTAPI.todolist.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${application.security.jwt.expiration}")
    private  long jwtExpiration;

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    public String extractUsername(String token) {
        return extractClaim(token , Claims::getSubject);
    }

    public <T> T extractClaim(String token , Function<Claims , T>
                            claimResolver){
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInkey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generatetoken(UserDetails userDetails){
        return generatetoken(new HashMap<>(),userDetails);

    }

    public String generatetoken(Map<String, Object> claims, UserDetails userDetails) {

        return buildtoken(claims,userDetails,jwtExpiration);
    }

    private String buildtoken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long jwtExpiration) {

        var authorities = userDetails.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .toList();
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration( new Date(System.currentTimeMillis() + jwtExpiration))
                .claim("authorities",authorities)
                .signWith(getSignInkey())
                .compact();
  
    }

   public boolean isTokenValid(String token, UserDetails userDetails) {
    try {
        System.out.println("Token Validation Details:");
        System.out.println("Input Token: " + token);
        System.out.println("Username from token: " + extractUsername(token));
        System.out.println("UserDetails username: " + userDetails.getUsername());

        final String username = extractUsername(token);
        boolean isUsernameMatch = username.equals(userDetails.getUsername());
        boolean isTokenExpired = isTokenExpired(token);

        System.out.println("Username Match: " + isUsernameMatch);
        System.out.println("Token Expired: " + isTokenExpired);

        return (isUsernameMatch) && !isTokenExpired;
    } catch (Exception e) {
        System.out.println("Token Validation Exception:");
        e.printStackTrace();
        return false;
    }
}

    /*private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }*/
    private boolean isTokenExpired(String token) {
    try {
        Date expiration = extractExpiration(token);
        boolean isExpired = expiration.before(new Date());
        System.out.println("Token Expiration: " + expiration);
        System.out.println("Current Date: " + new Date());
        System.out.println("Is Expired: " + isExpired);
        return isExpired;
    } catch (Exception e) {
        System.out.println("Expiration Check Exception:");
        e.printStackTrace();
        return true;
    }
}

    private Date extractExpiration(String token) {
        return extractClaim(token , Claims::getExpiration);
    }


private Key getSignInkey() {
    System.out.println("Secret Key before decoding: " + secretKey);
    if (secretKey == null || secretKey.isEmpty()) {
        System.out.println("Generating new key due to empty secret key");
        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }
    try {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        System.out.println("Key bytes length: " + keyBytes.length);
        return Keys.hmacShaKeyFor(keyBytes);
    } catch (WeakKeyException e) {
        System.out.println("Weak key exception. Generating new key.");
        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
    } catch (Exception e) {
        System.out.println("Unexpected error generating key: " + e.getMessage());
        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }
}

}
