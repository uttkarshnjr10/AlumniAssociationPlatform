package com.example.alumniassocaition1.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Lightweight user summary for embedding in other DTOs (posts, comments,
 * events).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSummaryDto {

    private Long id;
    private String name;
    private String email;
    private String role;
    private String status;
    private String profilePictureUrl;
}
