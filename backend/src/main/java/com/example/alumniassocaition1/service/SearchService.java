package com.example.alumniassocaition1.service;

import com.example.alumniassocaition1.dto.user.UserSummaryDto;

import java.util.List;

/**
 * Service contract for searching users within the authenticated user's college.
 */
public interface SearchService {

    List<UserSummaryDto> searchUsersInMyCollege(String searchTerm);
}