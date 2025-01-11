package com.CHRESTAPI.todolist.auth;


import com.CHRESTAPI.todolist.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RegisterRequest {

    @NotEmpty(message = "firstname is mandatory")
    @NotBlank(message = "firstname is mandatory")
    private String firstname;
    @NotEmpty(message = "lastName is mandatory")
    @NotBlank(message = "lastName is mandatory")
    private String lastname;
    @NotEmpty(message = "email is mandatory")
    @NotBlank(message = "email is mandatory")
    @Email(message = "email is not formatted correctly")
    private String email;
    @NotEmpty(message = "password is mandatory")
    @NotBlank(message = "password is mandatory")
    //@Size(min = 8 , message = "password should be 8 character at least")
    private String password;
}
