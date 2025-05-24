package com.CHRESTAPI.todolist.controllers.testroles;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/test")
@Tag(name = "test")
public class TestController {

    @GetMapping("/whoami")
    @Operation(summary = "whoami", description = "gives back profil connected")
    public Map<String, Object> whoAmI() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> response = new HashMap<>();

        response.put("username", auth.getName());
        response.put("authorities", auth.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toList()));

        return response;
    }

    @GetMapping("/admin")
    @Operation(summary = "admin", description = "test endpoint for admin")
    public String adminOnly() {
        return "This endpoint is accessible only to admins";
    }

    @GetMapping("/user")
    @Operation(summary = "user", description = "test endpoint for user")
    public String userEndpoint() {
        return "This endpoint is accessible to regular users";
    }

    @GetMapping("/shopowner")
    @Operation(summary = "shopowner", description = "test endpoint for shopowner")
    public String shopOwnerEndpoint() {
        return "This endpoint is for shop owners";
    }
}