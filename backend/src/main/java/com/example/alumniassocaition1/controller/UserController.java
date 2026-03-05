package com.example.alumniassocaition1.controller;

import com.example.alumniassocaition1.dto.ApiResponse;
import com.example.alumniassocaition1.dto.user.UserProfileDto;
import com.example.alumniassocaition1.dto.user.UserSummaryDto;
import com.example.alumniassocaition1.dto.user.UserUpdateRequest;
import com.example.alumniassocaition1.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for user profiles and follow relationships.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileDto> getUserProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    @GetMapping("/profile/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileDto> getCurrentUserProfile() {
        return ResponseEntity.ok(userService.getCurrentUserProfile());
    }

    @PutMapping("/profile/update")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileDto> updateProfile(
            @ModelAttribute UserUpdateRequest updateRequest) {
        return ResponseEntity.ok(userService.updateUserProfile(updateRequest));
    }

    @GetMapping("/{userId}/followers")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UserSummaryDto>> getFollowers(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getFollowers(userId));
    }

    @GetMapping("/{userId}/following")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UserSummaryDto>> getFollowing(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getFollowing(userId));
    }

    @PostMapping("/{userId}/follow")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> followUser(@PathVariable Long userId) {
        userService.followUser(userId);
        return ResponseEntity.ok(new ApiResponse(true, "Successfully followed user."));
    }

    @PostMapping("/{userId}/unfollow")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> unfollowUser(@PathVariable Long userId) {
        userService.unfollowUser(userId);
        return ResponseEntity.ok(new ApiResponse(true, "Successfully unfollowed user."));
    }
}
