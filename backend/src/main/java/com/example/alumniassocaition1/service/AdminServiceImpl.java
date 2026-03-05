package com.example.alumniassocaition1.service;

import com.example.alumniassocaition1.dto.user.AdminUserCreateRequest;
import com.example.alumniassocaition1.dto.user.UserStatusUpdateRequest;
import com.example.alumniassocaition1.dto.user.UserSummaryDto;
import com.example.alumniassocaition1.entity.*;
import com.example.alumniassocaition1.exception.ResourceNotFoundException;
import com.example.alumniassocaition1.repository.*;
import com.example.alumniassocaition1.util.DtoMapper;
import com.example.alumniassocaition1.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link AdminService}.
 *
 * <p>
 * All operations are scoped to the authenticated admin's college.
 * </p>
 */
@Service
public class AdminServiceImpl implements AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserFollowRepository userFollowRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final DonationRepository donationRepository;
    private final EventAttendeeRepository eventAttendeeRepository;
    private final PostLikeRepository postLikeRepository;
    private final FileStorageService fileStorageService;
    private final SecurityUtils securityUtils;
    private final DtoMapper dtoMapper;

    public AdminServiceImpl(UserRepository userRepository, EventRepository eventRepository,
            PasswordEncoder passwordEncoder, UserFollowRepository userFollowRepository,
            PostRepository postRepository, CommentRepository commentRepository,
            DonationRepository donationRepository,
            EventAttendeeRepository eventAttendeeRepository,
            PostLikeRepository postLikeRepository, FileStorageService fileStorageService,
            SecurityUtils securityUtils, DtoMapper dtoMapper) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.passwordEncoder = passwordEncoder;
        this.userFollowRepository = userFollowRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.donationRepository = donationRepository;
        this.eventAttendeeRepository = eventAttendeeRepository;
        this.postLikeRepository = postLikeRepository;
        this.fileStorageService = fileStorageService;
        this.securityUtils = securityUtils;
        this.dtoMapper = dtoMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserSummaryDto> getAllUsersForAdminCollege() {
        User admin = securityUtils.getCurrentAdminUser();
        College adminCollege = requireAdminCollege(admin);

        return userRepository.findByCollegeCollegeId(adminCollege.getCollegeId()).stream()
                .filter(user -> !user.getUserId().equals(admin.getUserId()))
                .map(dtoMapper::toUserSummary)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserSummaryDto adminAddUser(AdminUserCreateRequest createRequest) {
        User admin = securityUtils.getCurrentAdminUser();
        College adminCollege = requireAdminCollege(admin);

        if (userRepository.existsByEmail(createRequest.getEmail())) {
            throw new IllegalArgumentException("User with email " + createRequest.getEmail() + " already exists.");
        }

        User newUser = new User();
        newUser.setName(createRequest.getName());
        newUser.setEmail(createRequest.getEmail());
        newUser.setPasswordHash(passwordEncoder.encode(createRequest.getPassword()));
        newUser.setRole(createRequest.getRole());
        newUser.setStatus(createRequest.getStatus() != null ? createRequest.getStatus().toLowerCase() : "active");
        newUser.setCollege(adminCollege);

        User savedUser = userRepository.save(newUser);
        return dtoMapper.toUserSummary(savedUser);
    }

    @Override
    @Transactional
    public void adminRemoveUser(Long userId) throws ResourceNotFoundException {
        User admin = securityUtils.getCurrentAdminUser();
        User userToRemove = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (userToRemove.getUserId().equals(admin.getUserId())) {
            throw new AccessDeniedException("Admin cannot remove themselves.");
        }
        validateSameCollege(admin, userToRemove);

        logger.info("Removing user ID: {} and all related entities.", userId);

        // Cascade deletions
        userFollowRepository.deleteByIdFollowerId(userId);
        userFollowRepository.deleteByIdFollowingId(userId);
        postLikeRepository.deleteByIdUserId(userId);
        eventAttendeeRepository.deleteByIdUserId(userId);

        commentRepository.deleteAllInBatch(commentRepository.findByAuthorUserId(userId));

        for (Post post : postRepository.findByAuthorUserIdOrderByCreatedAtDesc(userId)) {
            deleteImage(post.getImageUrl());
            postRepository.delete(post);
        }

        for (Event event : eventRepository.findByCreatedByUserId(userId)) {
            deleteImage(event.getImageUrl());
            eventRepository.delete(event);
        }

        donationRepository.deleteAllInBatch(donationRepository.findByUserUserId(userId));
        userRepository.delete(userToRemove);
        logger.info("Successfully removed user ID: {}", userId);
    }

    @Override
    @Transactional
    public UserSummaryDto adminUpdateUserStatus(Long userId, UserStatusUpdateRequest statusUpdateRequest)
            throws ResourceNotFoundException {
        User admin = securityUtils.getCurrentAdminUser();
        User userToUpdate = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (userToUpdate.getUserId().equals(admin.getUserId())) {
            throw new AccessDeniedException("Admin cannot update their own status.");
        }
        validateSameCollege(admin, userToUpdate);

        String newStatus = statusUpdateRequest.getStatus();
        if (!("active".equalsIgnoreCase(newStatus) || "inactive".equalsIgnoreCase(newStatus))) {
            throw new IllegalArgumentException("Invalid status: " + newStatus + ". Must be 'active' or 'inactive'.");
        }

        userToUpdate.setStatus(newStatus.toLowerCase());
        User updatedUser = userRepository.save(userToUpdate);
        return dtoMapper.toUserSummary(updatedUser);
    }

    @Override
    @Transactional
    public void adminRemoveEvent(Long eventId) throws ResourceNotFoundException {
        User admin = securityUtils.getCurrentAdminUser();
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));

        if (event.getCollege() == null || admin.getCollege() == null
                || !event.getCollege().getCollegeId().equals(admin.getCollege().getCollegeId())) {
            throw new AccessDeniedException("Admin can only remove events from their own college.");
        }

        deleteImage(event.getImageUrl());
        eventRepository.delete(event);
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private College requireAdminCollege(User admin) {
        if (admin.getCollege() == null) {
            throw new IllegalStateException("Admin is not associated with a college.");
        }
        return admin.getCollege();
    }

    private void validateSameCollege(User admin, User target) {
        if (target.getCollege() == null || admin.getCollege() == null
                || !target.getCollege().getCollegeId().equals(admin.getCollege().getCollegeId())) {
            throw new AccessDeniedException("Admin cannot manage users from other colleges.");
        }
    }

    private void deleteImage(String imageUrl) {
        if (!StringUtils.hasText(imageUrl))
            return;
        try {
            if (imageUrl.startsWith("http") && imageUrl.contains("cloudinary")) {
                fileStorageService.deleteFile(imageUrl);
            } else {
                String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                if (!fileName.isEmpty())
                    fileStorageService.deleteFile(fileName);
            }
        } catch (Exception e) {
            logger.warn("Could not delete image: {}", e.getMessage());
        }
    }
}
