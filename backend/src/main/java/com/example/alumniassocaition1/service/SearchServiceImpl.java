package com.example.alumniassocaition1.service;

import com.example.alumniassocaition1.dto.user.UserSummaryDto;
import com.example.alumniassocaition1.entity.User;
<<<<<<< HEAD
import com.example.alumniassocaition1.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link SearchService}.
 *
 * <p>Searches users within the authenticated user's college using a single
 * combined name-or-email query, excluding the current user from results.</p>
 */
=======
import com.example.alumniassocaition1.entity.College; // Not strictly needed here if college comes via User
import com.example.alumniassocaition1.exception.ResourceNotFoundException; // Should not be thrown from here directly
import com.example.alumniassocaition1.repository.UserRepository;
// UserService import is correct
// import com.example.alumniassocaition1.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException; // Can be thrown if user has no college
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
// Unused import: import java.util.stream.Stream;

>>>>>>> upstream/main
@Service
public class SearchServiceImpl implements SearchService {

    private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    private final UserRepository userRepository;
<<<<<<< HEAD
    private final UserService userService;
=======
    private final UserService userService; // To get the current authenticated user
>>>>>>> upstream/main

    @Autowired
    public SearchServiceImpl(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserSummaryDto> searchUsersInMyCollege(String searchTerm) {
<<<<<<< HEAD
        User currentUser = userService.getCurrentAuthenticatedUserEntity();

        if (currentUser.getCollege() == null) {
            logger.warn("User {} is not associated with a college — returning empty search results.",
                    currentUser.getUserId());
            return List.of();
        }

        Long collegeId = currentUser.getCollege().getCollegeId();
        logger.info("Searching for '{}' in college {}.", searchTerm, collegeId);

        // Single JPQL query that searches both name and email, avoiding two queries + manual dedup
        List<User> results = userRepository.searchUsersInCollegeByNameOrEmail(collegeId, searchTerm);

        return results.stream()
                .filter(user -> !user.getUserId().equals(currentUser.getUserId()))
=======
        // Corrected line: Call the public interface method
        User currentUser = userService.getCurrentAuthenticatedUserEntity();

        if (currentUser.getCollege() == null) {
            logger.warn("User {} (ID: {}) attempted to search but is not associated with a college.",
                    currentUser.getName(), currentUser.getUserId());
            // Depending on requirements, either return empty list or throw an exception
            // For a search, returning an empty list is often acceptable.
            return List.of();
            // Or: throw new IllegalStateException("User is not associated with a college and cannot perform college-scoped search.");
        }

        Long collegeId = currentUser.getCollege().getCollegeId();
        logger.info("Searching for users with term '{}' in college ID: {}", searchTerm, collegeId);

        // Fetch users by name and by email, then combine and make distinct
        List<User> usersByName = userRepository.findByCollegeCollegeIdAndNameContainingIgnoreCase(collegeId, searchTerm);
        List<User> usersByEmail = userRepository.findByCollegeCollegeIdAndEmailContainingIgnoreCase(collegeId, searchTerm);

        // Combine and ensure uniqueness (e.g., if name and email match the same user)
        // Also, filter out the current user from the search results.
        Set<User> combinedResults = new HashSet<>();
        usersByName.stream()
                .filter(user -> !user.getUserId().equals(currentUser.getUserId()))
                .forEach(combinedResults::add);
        usersByEmail.stream()
                .filter(user -> !user.getUserId().equals(currentUser.getUserId()))
                .forEach(combinedResults::add);


        return combinedResults.stream()
                // .filter(user -> !user.getUserId().equals(currentUser.getUserId())) // Already filtered above
>>>>>>> upstream/main
                .map(this::mapUserToSummaryDto)
                .collect(Collectors.toList());
    }

    private UserSummaryDto mapUserToSummaryDto(User user) {
        UserSummaryDto dto = new UserSummaryDto();
<<<<<<< HEAD
=======
        // BeanUtils.copyProperties(user, dto); // This can be used if fields match exactly
        // Explicit mapping for clarity and to ensure correct fields from UserSummaryDto are populated
>>>>>>> upstream/main
        dto.setId(user.getUserId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());
<<<<<<< HEAD
=======
        // Add other fields to UserSummaryDto if needed for search results display
>>>>>>> upstream/main
        return dto;
    }
}
