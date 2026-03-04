package com.example.alumniassocaition1.dto.user;

import lombok.Data;
<<<<<<< HEAD
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Lightweight user summary for embedding in other DTOs (posts, comments, events).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSummaryDto {

=======

@Data
public class UserSummaryDto {
>>>>>>> upstream/main
    private Long id;
    private String name;
    private String email;
    private String role;
<<<<<<< HEAD
    private String status;
=======
    public String Status;
    // Add other fields as needed for summary
>>>>>>> upstream/main
}
