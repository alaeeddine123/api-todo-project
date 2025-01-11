package com.CHRESTAPI.todolist.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Authentication Request")
public class AuthenticationRequest {

    @Schema(description = "User email", example = "user@example.com", required = true)
    @JsonProperty("email")
    private String email;

    @Schema(description = "User password", example = "password123", required = true)
    @JsonProperty("password")
    private String password;
}