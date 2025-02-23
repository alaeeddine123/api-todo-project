package com.CHRESTAPI.todolist.auth;



import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
@Slf4j
public class JwkSetController {

    private final RSAKey rsaKey;

    @GetMapping("/jwks")
    public Map<String, Object> keys() {
        return new JWKSet(rsaKey).toJSONObject();
    }
}