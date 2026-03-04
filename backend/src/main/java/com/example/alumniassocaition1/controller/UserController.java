package com.example.alumniassocaition1.controller;

import com.example.alumniassocaition1.dto.ApiResponse;
<<<<<<< HEAD
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
=======
import com.example.alumniassocaition1.dto.user.UserProfileDto; // Assuming DTO path
import com.example.alumniassocaition1.dto.user.UserSummaryDto; // Assuming DTO path
import com.example.alumniassocaition1.dto.user.UserUpdateRequest; // Assuming DTO path
import com.example.alumniassocaition1.service.UserService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
// Potentially needed if handling profile picture uploads directly here
// import org.springframework.web.multipart.MultipartFile;
// import org.springframework.http.MediaType;

import java.util.List;

>>>>>>> upstream/main
@RestController
@RequestMapping("/api/users")
public class UserController {

<<<<<<< HEAD
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /** Returns the full profile of the specified user. */
    @GetMapping("/{userId}")
    @PreAuthorize("isAuthenticated()")
=======
    @Autowired
    private UserService userService;

    // Get public profile details of a specific user
    @GetMapping("/{userId}")
    @PreAuthorize("isAuthenticated()") // Or adjust if some profiles are truly public without login
>>>>>>> upstream/main
    public ResponseEntity<UserProfileDto> getUserProfile(@PathVariable Long userId) {
        UserProfileDto userProfile = userService.getUserProfile(userId);
        return ResponseEntity.ok(userProfile);
    }

<<<<<<< HEAD
    /** Replaces the current user's profile with the provided data. */
    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileDto> updateCurrentUserProfile(
            @Valid @RequestBody UserUpdateRequest updateRequest) {
=======
    // Update the profile of the currently logged-in user
    // If profile picture updates involve file upload, this endpoint would need to handle multipart data
    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileDto> updateCurrentUserProfile(@Valid @RequestBody UserUpdateRequest updateRequest) {
        // If UserUpdateRequest also handles profilePictureUrl (as a string from a separate upload service), this is fine.
        // If handling MultipartFile here, the signature would change:
        // public ResponseEntity<UserProfileDto> updateCurrentUserProfile(
        // @Valid @RequestPart("userData") UserUpdateRequest updateRequest,
        // @RequestPart(value="profilePictureFile", required=false) MultipartFile profilePictureFile) {
        // userService.updateUserProfile(updateRequest, profilePictureFile);
>>>>>>> upstream/main
        UserProfileDto updatedProfile = userService.updateUserProfile(updateRequest);
        return ResponseEntity.ok(updatedProfile);
    }

<<<<<<< HEAD
    /** Partially updates the current user's profile. */
    @PatchMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileDto> patchCurrentUserProfile(
            @Valid @RequestBody UserUpdateRequest updateRequest) {
=======
    @PatchMapping("/me") // Alias for PUT for partial updates
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileDto> patchCurrentUserProfile(@Valid @RequestBody UserUpdateRequest updateRequest) {
        // Ensure your UserServiceImpl.updateUserProfile can handle partial updates gracefully
        // or create a separate service method for patching.
>>>>>>> upstream/main
        UserProfileDto updatedProfile = userService.updateUserProfile(updateRequest);
        return ResponseEntity.ok(updatedProfile);
    }

<<<<<<< HEAD
    /** Lists users who follow the specified user. */
=======
    // Get the list of users following the specified user
>>>>>>> upstream/main
    @GetMapping("/{userId}/followers")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UserSummaryDto>> getFollowers(@PathVariable Long userId) {
        List<UserSummaryDto> followers = userService.getFollowers(userId);
        return ResponseEntity.ok(followers);
    }

<<<<<<< HEAD
    /** Lists users the specified user is following. */
=======
    // Get the list of users the specified user is following
>>>>>>> upstream/main
    @GetMapping("/{userId}/following")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UserSummaryDto>> getFollowing(@PathVariable Long userId) {
        List<UserSummaryDto> following = userService.getFollowing(userId);
        return ResponseEntity.ok(following);
    }

<<<<<<< HEAD
    /** Makes the current user follow the target user. */
=======
    // Make the logged-in user follow the user specified by :userId
>>>>>>> upstream/main
    @PostMapping("/{userId}/follow")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> followUser(@PathVariable Long userId) {
        userService.followUser(userId);
        return ResponseEntity.ok(new ApiResponse(true, "Successfully followed user."));
    }

<<<<<<< HEAD
    /** Makes the current user unfollow the target user. */
=======
    // Make the logged-in user unfollow the user specified by :userId
>>>>>>> upstream/main
    @DeleteMapping("/{userId}/follow")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> unfollowUser(@PathVariable Long userId) {
        userService.unfollowUser(userId);
        return ResponseEntity.ok(new ApiResponse(true, "Successfully unfollowed user."));
    }
}
<<<<<<< HEAD
=======

>>>>>>> upstream/main
