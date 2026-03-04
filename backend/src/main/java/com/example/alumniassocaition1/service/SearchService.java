// --- File: com/example/alumniassocaition1/service/SearchService.java ---
package com.example.alumniassocaition1.service;

import com.example.alumniassocaition1.dto.user.UserSummaryDto;
import java.util.List;

<<<<<<< HEAD
/**
 * Service contract for searching users within the authenticated user's college.
 */
=======
>>>>>>> upstream/main
public interface SearchService {
    List<UserSummaryDto> searchUsersInMyCollege(String searchTerm);
}