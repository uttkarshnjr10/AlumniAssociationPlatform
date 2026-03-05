package com.example.alumniassocaition1.controller;

import com.example.alumniassocaition1.dto.ApiResponse;
import com.example.alumniassocaition1.dto.user.AdminUserCreateRequest;
import com.example.alumniassocaition1.dto.user.UserStatusUpdateRequest;
import com.example.alumniassocaition1.dto.user.UserSummaryDto;
import com.example.alumniassocaition1.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for college-admin operations (user and event management).
 */
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserSummaryDto>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsersForAdminCollege());
    }

    @PostMapping("/users")
    public ResponseEntity<UserSummaryDto> addUser(
            @Valid @RequestBody AdminUserCreateRequest createRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(adminService.adminAddUser(createRequest));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ApiResponse> removeUser(@PathVariable Long userId) {
        adminService.adminRemoveUser(userId);
        return ResponseEntity.ok(new ApiResponse(true, "User removed successfully."));
    }

    @PutMapping("/users/{userId}/status")
    public ResponseEntity<UserSummaryDto> updateUserStatus(
            @PathVariable Long userId,
            @Valid @RequestBody UserStatusUpdateRequest statusUpdateRequest) {
        return ResponseEntity.ok(adminService.adminUpdateUserStatus(userId, statusUpdateRequest));
    }

    @DeleteMapping("/events/{eventId}")
    public ResponseEntity<ApiResponse> removeEvent(@PathVariable Long eventId) {
        adminService.adminRemoveEvent(eventId);
        return ResponseEntity.ok(new ApiResponse(true, "Event removed successfully."));
    }
}
