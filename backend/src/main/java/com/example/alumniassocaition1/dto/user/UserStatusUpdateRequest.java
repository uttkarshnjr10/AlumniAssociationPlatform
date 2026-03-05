package com.example.alumniassocaition1.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Request payload for updating a user's account status.
 */
@Data
public class UserStatusUpdateRequest {

    @NotBlank(message = "Status is required")
    private String status;
}
