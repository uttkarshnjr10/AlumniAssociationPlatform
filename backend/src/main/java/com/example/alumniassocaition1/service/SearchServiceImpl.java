package com.example.alumniassocaition1.service;

import com.example.alumniassocaition1.dto.user.UserSummaryDto;
import com.example.alumniassocaition1.entity.User;
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
@Service
public class SearchServiceImpl implements SearchService {

    private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public SearchServiceImpl(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserSummaryDto> searchUsersInMyCollege(String searchTerm) {
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
                .map(this::mapUserToSummaryDto)
                .collect(Collectors.toList());
    }

    private UserSummaryDto mapUserToSummaryDto(User user) {
        UserSummaryDto dto = new UserSummaryDto();
        dto.setId(user.getUserId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());
        return dto;
    }
}
