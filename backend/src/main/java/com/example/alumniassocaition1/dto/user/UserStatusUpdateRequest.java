package com.example.alumniassocaition1.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

<<<<<<< HEAD
/**
 * Request payload for updating a user's account status (e.g. ACTIVE, SUSPENDED).
 */
@Data
public class UserStatusUpdateRequest {

    @NotBlank(message = "Status is required")
=======
@Data
public class UserStatusUpdateRequest {
    @NotBlank
>>>>>>> upstream/main
    private String status;
}
