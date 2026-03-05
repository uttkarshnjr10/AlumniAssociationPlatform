package com.example.alumniassocaition1.service;

import com.example.alumniassocaition1.dto.user.UserSummaryDto;
import com.example.alumniassocaition1.entity.User;
import com.example.alumniassocaition1.repository.UserRepository;
import com.example.alumniassocaition1.util.DtoMapper;
import com.example.alumniassocaition1.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link SearchService}.
 *
 * <p>
 * Searches users within the authenticated user's college using a single
 * combined name-or-email query, excluding the current user from results.
 * </p>
 */
@Service
public class SearchServiceImpl implements SearchService {

        private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

        private final UserRepository userRepository;
        private final SecurityUtils securityUtils;
        private final DtoMapper dtoMapper;

        public SearchServiceImpl(UserRepository userRepository,
                        SecurityUtils securityUtils,
                        DtoMapper dtoMapper) {
                this.userRepository = userRepository;
                this.securityUtils = securityUtils;
                this.dtoMapper = dtoMapper;
        }

        @Override
        @Transactional(readOnly = true)
        public List<UserSummaryDto> searchUsersInMyCollege(String searchTerm) {
                User currentUser = securityUtils.getCurrentUser();

                if (currentUser.getCollege() == null) {
                        logger.warn("User {} is not associated with a college — returning empty results.",
                                        currentUser.getUserId());
                        return List.of();
                }

                Long collegeId = currentUser.getCollege().getCollegeId();
                logger.info("Searching for '{}' in college {}.", searchTerm, collegeId);

                List<User> results = userRepository.searchUsersInCollegeByNameOrEmail(collegeId, searchTerm);

                return results.stream()
                                .filter(user -> !user.getUserId().equals(currentUser.getUserId()))
                                .map(dtoMapper::toUserSummary)
                                .collect(Collectors.toList());
        }
}
