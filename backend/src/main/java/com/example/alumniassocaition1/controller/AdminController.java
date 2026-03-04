<<<<<<< HEAD
package com.example.alumniassocaition1.controller;

import com.example.alumniassocaition1.dto.ApiResponse;
import com.example.alumniassocaition1.dto.user.AdminUserCreateRequest;
import com.example.alumniassocaition1.dto.user.UserStatusUpdateRequest;
import com.example.alumniassocaition1.dto.user.UserSummaryDto;
import com.example.alumniassocaition1.service.AdminService;

import jakarta.validation.Valid;
=======
package com.example.alumniassocaition1.controller; // Or your actual package

import com.example.alumniassocaition1.dto.ApiResponse;
import com.example.alumniassocaition1.dto.user.AdminUserCreateRequest; // Ensure this DTO path is correct
import com.example.alumniassocaition1.dto.user.UserStatusUpdateRequest;  // Ensure this DTO path is correct
import com.example.alumniassocaition1.dto.user.UserSummaryDto;       // Ensure this DTO path is correct
import com.example.alumniassocaition1.service.AdminService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
>>>>>>> upstream/main
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

<<<<<<< HEAD
/**
 * REST controller for college-level administration.
 *
 * <p>All endpoints require the {@code ADMIN} role.
 * Operations are scoped to the admin's own college.</p>
 */
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /** Lists all users belonging to the admin's college. */
=======
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')") // All endpoints in this controller require ADMIN role
public class AdminController {

    @Autowired
    private AdminService adminService;

>>>>>>> upstream/main
    @GetMapping("/users")
    public ResponseEntity<List<UserSummaryDto>> getUsersForAdminCollege() {
        List<UserSummaryDto> users = adminService.getAllUsersForAdminCollege();
        return ResponseEntity.ok(users);
    }

<<<<<<< HEAD
    /** Creates a new user within the admin's college. */
    @PostMapping("/users")
    public ResponseEntity<UserSummaryDto> addUserByAdmin(
            @Valid @RequestBody AdminUserCreateRequest createRequest) {
=======
    @PostMapping("/users")
    public ResponseEntity<UserSummaryDto> addUserByAdmin(@Valid @RequestBody AdminUserCreateRequest createRequest) {
>>>>>>> upstream/main
        UserSummaryDto newUser = adminService.adminAddUser(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

<<<<<<< HEAD
    /** Removes a user from the admin's college. */
=======
>>>>>>> upstream/main
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> removeUserByAdmin(@PathVariable Long userId) {
        adminService.adminRemoveUser(userId);
        return ResponseEntity.ok(new ApiResponse(true, "User removed successfully."));
    }

<<<<<<< HEAD
    /** Updates a user's status (e.g. active/inactive) via PATCH. */
    @PatchMapping("/users/{userId}/status")
    public ResponseEntity<UserSummaryDto> updateUserStatusByAdmin(
            @PathVariable Long userId,
            @Valid @RequestBody UserStatusUpdateRequest statusRequest) {
=======
    @PatchMapping("/users/{userId}/status")
    public ResponseEntity<UserSummaryDto> updateUserStatusByAdmin(@PathVariable Long userId,
                                                                  @Valid @RequestBody UserStatusUpdateRequest statusRequest) {
>>>>>>> upstream/main
        UserSummaryDto updatedUser = adminService.adminUpdateUserStatus(userId, statusRequest);
        return ResponseEntity.ok(updatedUser);
    }

<<<<<<< HEAD
    /** Updates a user's status (e.g. active/inactive) via PUT. */
    @PutMapping("/users/{userId}/status")
    public ResponseEntity<UserSummaryDto> putUserStatusByAdmin(
            @PathVariable Long userId,
            @Valid @RequestBody UserStatusUpdateRequest statusRequest) {
=======
    // PUT can also be used for status update if preferred, mapping to the same service method
    @PutMapping("/users/{userId}/status")
    public ResponseEntity<UserSummaryDto> putUserStatusByAdmin(@PathVariable Long userId,
                                                               @Valid @RequestBody UserStatusUpdateRequest statusRequest) {
>>>>>>> upstream/main
        UserSummaryDto updatedUser = adminService.adminUpdateUserStatus(userId, statusRequest);
        return ResponseEntity.ok(updatedUser);
    }

<<<<<<< HEAD
    /** Removes an event within the admin's college scope. */
=======
>>>>>>> upstream/main
    @DeleteMapping("/events/{eventId}")
    public ResponseEntity<?> removeEventByAdmin(@PathVariable Long eventId) {
        adminService.adminRemoveEvent(eventId);
        return ResponseEntity.ok(new ApiResponse(true, "Event removed by admin successfully."));
    }
}
