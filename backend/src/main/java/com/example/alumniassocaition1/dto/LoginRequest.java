package com.example.alumniassocaition1.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

<<<<<<< HEAD
/**
 * Request payload for user authentication (login).
 */
@Data
public class LoginRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Must be a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
=======
@Data
public class LoginRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
>>>>>>> upstream/main
    private String password;
}
