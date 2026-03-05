package com.example.alumniassocaition1.service;

import com.example.alumniassocaition1.dto.user.UserProfileDto;
import com.example.alumniassocaition1.dto.user.UserSummaryDto;
import com.example.alumniassocaition1.dto.user.UserUpdateRequest;
import com.example.alumniassocaition1.entity.User;
import com.example.alumniassocaition1.entity.UserFollow;
import com.example.alumniassocaition1.entity.UserFollowId;
import com.example.alumniassocaition1.exception.ResourceNotFoundException;
import com.example.alumniassocaition1.repository.UserFollowRepository;
import com.example.alumniassocaition1.repository.UserRepository;
import com.example.alumniassocaition1.util.DtoMapper;
import com.example.alumniassocaition1.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link UserService}.
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserFollowRepository userFollowRepository;
    private final FileStorageService fileStorageService;
    private final SecurityUtils securityUtils;
    private final DtoMapper dtoMapper;

    public UserServiceImpl(UserRepository userRepository,
            UserFollowRepository userFollowRepository,
            FileStorageService fileStorageService,
            SecurityUtils securityUtils,
            DtoMapper dtoMapper) {
        this.userRepository = userRepository;
        this.userFollowRepository = userFollowRepository;
        this.fileStorageService = fileStorageService;
        this.securityUtils = securityUtils;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPasswordHash(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase())));
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileDto getUserProfile(Long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return dtoMapper.toUserProfile(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileDto getCurrentUserProfile() {
        User user = securityUtils.getCurrentUser();
        return dtoMapper.toUserProfile(user);
    }

    @Override
    @Transactional
    public UserProfileDto updateUserProfile(UserUpdateRequest updateRequest) {
        User user = securityUtils.getCurrentUser();

        if (updateRequest.getName() != null)
            user.setName(updateRequest.getName());
        if (updateRequest.getHeadline() != null)
            user.setProfileHeadline(updateRequest.getHeadline());
        if (updateRequest.getLocation() != null)
            user.setProfileLocation(updateRequest.getLocation());
        if (updateRequest.getAbout() != null)
            user.setProfileAbout(updateRequest.getAbout());

        if (updateRequest.getProfilePicture() != null && !updateRequest.getProfilePicture().isEmpty()) {
            if (StringUtils.hasText(user.getProfilePictureUrl())) {
                try {
                    String oldFileName = user.getProfilePictureUrl()
                            .substring(user.getProfilePictureUrl().lastIndexOf("/") + 1);
                    if (!oldFileName.isEmpty())
                        fileStorageService.deleteFile(oldFileName);
                } catch (Exception e) {
                    logger.warn("Could not delete old profile picture: {}", e.getMessage());
                }
            }
            String storedResult = fileStorageService.storeFile(updateRequest.getProfilePicture());
            // If stored result is a full URL (Cloudinary), use it directly; otherwise build
            // local URL
            if (storedResult.startsWith("http")) {
                user.setProfilePictureUrl(storedResult);
            } else {
                String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/posts/uploads/").path(storedResult).toUriString();
                user.setProfilePictureUrl(fileUrl);
            }
        }

        User updatedUser = userRepository.save(user);
        return dtoMapper.toUserProfile(updatedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserSummaryDto> getFollowers(Long userId) throws ResourceNotFoundException {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        return userFollowRepository.findByIdFollowingId(userId).stream()
                .map(uf -> dtoMapper.toUserSummary(uf.getFollower()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserSummaryDto> getFollowing(Long userId) throws ResourceNotFoundException {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        return userFollowRepository.findByIdFollowerId(userId).stream()
                .map(uf -> dtoMapper.toUserSummary(uf.getFollowing()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void followUser(Long userIdToFollow) throws ResourceNotFoundException {
        User currentUser = securityUtils.getCurrentUser();
        if (currentUser.getUserId().equals(userIdToFollow)) {
            throw new IllegalArgumentException("Cannot follow yourself.");
        }

        User userToFollow = userRepository.findById(userIdToFollow)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userIdToFollow));

        UserFollowId followId = new UserFollowId(currentUser.getUserId(), userToFollow.getUserId());
        if (userFollowRepository.existsById(followId))
            return;

        UserFollow follow = new UserFollow();
        follow.setId(followId);
        follow.setFollower(currentUser);
        follow.setFollowing(userToFollow);
        userFollowRepository.save(follow);
    }

    @Override
    @Transactional
    public void unfollowUser(Long userIdToUnfollow) throws ResourceNotFoundException {
        User currentUser = securityUtils.getCurrentUser();
        if (currentUser.getUserId().equals(userIdToUnfollow)) {
            throw new IllegalArgumentException("Cannot unfollow yourself.");
        }
        if (!userRepository.existsById(userIdToUnfollow)) {
            throw new ResourceNotFoundException("User", "id", userIdToUnfollow);
        }

        UserFollowId followId = new UserFollowId(currentUser.getUserId(), userIdToUnfollow);
        if (!userFollowRepository.existsById(followId))
            return;
        userFollowRepository.deleteById(followId);
    }

    @Override
    @Transactional(readOnly = true)
    public User findUserById(Long userId) throws ResourceNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

    @Override
    @Transactional(readOnly = true)
    public User findUserByEmail(String email) throws ResourceNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    @Override
    public User getCurrentAuthenticatedUserEntity() {
        return securityUtils.getCurrentUser();
    }
}