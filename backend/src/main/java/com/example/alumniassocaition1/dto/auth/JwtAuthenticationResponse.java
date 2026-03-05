package main.java.com.example.alumniassocaition1.dto.auth;

import com.example.alumniassocaition1.dto.user.UserSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response payload returned after successful authentication.
 * Contains the JWT token and authenticated user summary.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthenticationResponse {

    private String token;
    private UserSummaryDto user;
}
