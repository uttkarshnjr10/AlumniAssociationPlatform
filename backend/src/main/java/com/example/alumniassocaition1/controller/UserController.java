package com.example.alumniassocaition1.controller;

import com.example.alumniassocaition1.dto.ApiResponse;
import com.example.alumniassocaition1.dto.user.UserProfileDto;
import com.example.alumniassocaition1.dto.user.UserSummaryDto;
import com.example.alumniassocaition1.dto.user.UserUpdateRequest;
import com.example.alumniassocaition1.service.UserService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for user profiles and follow relationships.
 *
 * <p>Authenticated users can view profiles, update their own profile,
 * and manage follow/unfollow actions.</p>
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /** Returns the full profile of the specified user. */
    @GetMapping("/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileDto> getUserProfile(@PathVariable Long userId) {
        UserProfileDto userProfile = userService.getUserProfile(userId);
        return ResponseEntity.ok(userProfile);
    }

    /** Replaces the current user's profile with the provided data. */
    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileDto> updateCurrentUserProfile(
            @Valid @RequestBody UserUpdateRequest updateRequest) {
        UserProfileDto updatedProfile = userService.updateUserProfile(updateRequest);
        return ResponseEntity.ok(updatedProfile);
    }

    /** Partially updates the current user's profile. */
    @PatchMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileDto> patchCurrentUserProfile(
            @Valid @RequestBody UserUpdateRequest updateRequest) {
        UserProfileDto updatedProfile = userService.updateUserProfile(updateRequest);
        return ResponseEntity.ok(updatedProfile);
    }

    /** Lists users who follow the specified user. */
    @GetMapping("/{userId}/followers")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UserSummaryDto>> getFollowers(@PathVariable Long userId) {
        List<UserSummaryDto> followers = userService.getFollowers(userId);
        return ResponseEntity.ok(followers);
    }

    /** Lists users the specified user is following. */
    @GetMapping("/{userId}/following")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UserSummaryDto>> getFollowing(@PathVariable Long userId) {
        List<UserSummaryDto> following = userService.getFollowing(userId);
        return ResponseEntity.ok(following);
    }

    /** Makes the current user follow the target user. */
    @PostMapping("/{userId}/follow")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> followUser(@PathVariable Long userId) {
        userService.followUser(userId);
        return ResponseEntity.ok(new ApiResponse(true, "Successfully followed user."));
    }

    /** Makes the current user unfollow the target user. */
    @DeleteMapping("/{userId}/follow")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> unfollowUser(@PathVariable Long userId) {
        userService.unfollowUser(userId);
        return ResponseEntity.ok(new ApiResponse(true, "Successfully unfollowed user."));
    }
}
