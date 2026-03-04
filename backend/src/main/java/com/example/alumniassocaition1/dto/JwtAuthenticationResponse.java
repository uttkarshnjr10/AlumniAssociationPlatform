package com.example.alumniassocaition1.dto;

import com.example.alumniassocaition1.dto.user.UserSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

<<<<<<< HEAD
/**
 * Response payload returned after successful authentication.
 * Contains the JWT token and authenticated user summary.
 */
=======
>>>>>>> upstream/main
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthenticationResponse {
<<<<<<< HEAD

=======
>>>>>>> upstream/main
    private String token;
    private UserSummaryDto user;
}
