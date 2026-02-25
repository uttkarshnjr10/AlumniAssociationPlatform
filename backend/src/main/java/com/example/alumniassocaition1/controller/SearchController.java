package com.example.alumniassocaition1.controller;

import com.example.alumniassocaition1.dto.user.UserSummaryDto;
import com.example.alumniassocaition1.service.SearchService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for user searches.
 *
 * <p>Searches are scoped to the authenticated user's college.</p>
 */
@RestController
@RequestMapping("/api/search")
public class SearchController {

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
        }
        List<UserSummaryDto> results = searchService.searchUsersInMyCollege(query);
        return ResponseEntity.ok(results);
    }
}
