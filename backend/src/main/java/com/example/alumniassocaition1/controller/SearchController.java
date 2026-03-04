<<<<<<< HEAD
=======
// --- File: com/example/alumniassocaition1/controller/SearchController.java ---
>>>>>>> upstream/main
package com.example.alumniassocaition1.controller;

import com.example.alumniassocaition1.dto.user.UserSummaryDto;
import com.example.alumniassocaition1.service.SearchService;
<<<<<<< HEAD

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for user searches.
 *
 * <p>Searches are scoped to the authenticated user's college.</p>
 */
=======
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

>>>>>>> upstream/main
@RestController
@RequestMapping("/api/search")
public class SearchController {

<<<<<<< HEAD
    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    /** Searches for users by name or email within the current user's college. */
    @GetMapping("/users")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UserSummaryDto>> searchUsersInCollege(
            @RequestParam("q") String query) {
        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(List.of());
=======
    @Autowired
    private SearchService searchService;

    @GetMapping("/users")
    @PreAuthorize("isAuthenticated()") // User must be logged in to search
    public ResponseEntity<List<UserSummaryDto>> searchUsersInCollege(@RequestParam("q") String query) {
        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(List.of()); // Or an ApiResponse
>>>>>>> upstream/main
        }
        List<UserSummaryDto> results = searchService.searchUsersInMyCollege(query);
        return ResponseEntity.ok(results);
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> upstream/main
