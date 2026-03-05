package com.example.alumniassocaition1.service;

import com.example.alumniassocaition1.dto.user.AdminUserCreateRequest;
import com.example.alumniassocaition1.dto.user.UserStatusUpdateRequest;
import com.example.alumniassocaition1.dto.user.UserSummaryDto;
import com.example.alumniassocaition1.exception.ResourceNotFoundException;

import java.util.List;

/**
 * Service contract for college-admin operations: user management and
 * event moderation within the admin's college scope.
 */
public interface AdminService {

    List<UserSummaryDto> getAllUsersForAdminCollege();

    UserSummaryDto adminAddUser(AdminUserCreateRequest createRequest);

    void adminRemoveUser(Long userId) throws ResourceNotFoundException;

    UserSummaryDto adminUpdateUserStatus(Long userId, UserStatusUpdateRequest statusUpdateRequest)
            throws ResourceNotFoundException;

    void adminRemoveEvent(Long eventId) throws ResourceNotFoundException;
}
