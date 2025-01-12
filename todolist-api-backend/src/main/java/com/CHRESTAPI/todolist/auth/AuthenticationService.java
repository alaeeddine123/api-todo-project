package com.CHRESTAPI.todolist.auth;


import com.CHRESTAPI.todolist.email.EmailService;
import com.CHRESTAPI.todolist.email.EmailTemplateName;
import com.CHRESTAPI.todolist.entities.Token;
import com.CHRESTAPI.todolist.entities.User;
import com.CHRESTAPI.todolist.repositories.RoleRepository;
import com.CHRESTAPI.todolist.repositories.TokenRepository;
import com.CHRESTAPI.todolist.repositories.UserRepository;
import com.CHRESTAPI.todolist.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor

public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private String activationUrl = "http://localhost:4200/activate-account";

    public void register(RegisterRequest request) throws MessagingException, jakarta.mail.MessagingException {
        var userRole = roleRepository.findByName("USER")
                // todo - better exception handling
                .orElseThrow(() -> new IllegalStateException("ROLE USER was not initiated"));
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .dateOfBirth(LocalDate.now())
                .build();
        userRepository.save(user);
        sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) throws MessagingException, jakarta.mail.MessagingException {
        var newToken = generateAndSaveActivationToken(user);

        emailService.sendEmail(
                user.getEmail(),
                user.getFullname(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "account Activation"
        );
    }

    private String generateAndSaveActivationToken(User user) {
        String generateToken = generateActivationCode(8);
        var token = Token.builder()
                .token(generateToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();

        tokenRepository.save(token);

        return generateToken;
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();

        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }

    @Transactional
    public void activateAccount(String token) throws jakarta.mail.MessagingException, MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                // todo exception has to be defined
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation token has expired. A new token has been send to the same email address");
        }

        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);

        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            var authToken = new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            );
            var auth = authenticationManager.authenticate(authToken);
            var user = ((User) auth.getPrincipal());
            var claims = new HashMap<String, Object>();
            claims.put("fullName", user.getFullname());
            var jwtToken = jwtService.generatetoken(claims, user);
            return AuthenticationResponse.builder().token(jwtToken).build();

        } catch (BadCredentialsException e) {
            System.out.println("Bad credentials: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.out.println("Authentication failed: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
   /*  public boolean verifySession(String token) {
        if (token != null) {
            try {
                String username = jwtService.extractUsername(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                return jwtService.isTokenValid(token, userDetails);
            } catch (Exception e) {
                System.out.println("Token verification failed");
                return false;
            }
        }
        return false;
    }*/
    public boolean verifySession(String token) {
    if (token != null) {
        try {
            System.out.println("Session Verification Attempt:");
            String username = jwtService.extractUsername(token);
            System.out.println("Extracted Username: " + username);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            System.out.println("UserDetails Found: " + (userDetails != null));

            boolean isValid = jwtService.isTokenValid(token, userDetails);
            System.out.println("Session Verification Result: " + isValid);

            return isValid;
        } catch (Exception e) {
            System.out.println("Session Verification Exception:");
            e.printStackTrace();
            return false;
        }
    }
    return false;
}

}
