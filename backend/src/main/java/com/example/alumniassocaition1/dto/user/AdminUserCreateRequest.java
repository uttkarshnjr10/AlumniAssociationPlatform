package com.example.alumniassocaition1.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Request payload for an admin to create a new user within their college.
 */
@Data
public class AdminUserCreateRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Must be a valid email address")
    private String email;

    @NotBlank(message = "Role is required")
    private String role;

    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private String status;

    private Long collegeId;
}
