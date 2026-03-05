package com.example.alumniassocaition1.util;

import com.example.alumniassocaition1.dto.comment.CommentDto;
import com.example.alumniassocaition1.dto.event.EventDto;
import com.example.alumniassocaition1.dto.post.PostDto;
import com.example.alumniassocaition1.dto.user.UserProfileDto;
import com.example.alumniassocaition1.dto.user.UserSummaryDto;
import com.example.alumniassocaition1.entity.*;
import com.example.alumniassocaition1.repository.CommentRepository;
import com.example.alumniassocaition1.repository.EventAttendeeRepository;
import com.example.alumniassocaition1.repository.PostLikeRepository;
import com.example.alumniassocaition1.repository.UserFollowRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Centralised DTO mapping utility.
 *
 * <p>
 * Eliminates the duplicated {@code mapUserToSummaryDto()},
 * {@code mapPostToDto()}, and {@code mapEventToDto()} methods that were
 * scattered across multiple service implementations.
 * </p>
 */
@Component
public class DtoMapper {

    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final EventAttendeeRepository eventAttendeeRepository;
    private final UserFollowRepository userFollowRepository;

    public DtoMapper(PostLikeRepository postLikeRepository,
            CommentRepository commentRepository,
            EventAttendeeRepository eventAttendeeRepository,
            UserFollowRepository userFollowRepository) {
        this.postLikeRepository = postLikeRepository;
        this.commentRepository = commentRepository;
        this.eventAttendeeRepository = eventAttendeeRepository;
        this.userFollowRepository = userFollowRepository;
    }

    // -------------------------------------------------------------------------
    // User mappings
    // -------------------------------------------------------------------------

    public UserSummaryDto toUserSummary(User user) {
        if (user == null)
            return null;
        UserSummaryDto dto = new UserSummaryDto();
        dto.setId(user.getUserId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());
        dto.setProfilePictureUrl(user.getProfilePictureUrl());
        return dto;
    }

    public UserProfileDto toUserProfile(User user) {
        if (user == null)
            return null;
        UserProfileDto dto = new UserProfileDto();
        dto.setId(user.getUserId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());
        dto.setHeadline(user.getProfileHeadline());
        dto.setLocation(user.getProfileLocation());
        dto.setAbout(user.getProfileAbout());
        dto.setProfilePictureUrl(user.getProfilePictureUrl());
        dto.setFollowersCount(userFollowRepository.countByIdFollowingId(user.getUserId()));
        dto.setFollowingCount(userFollowRepository.countByIdFollowerId(user.getUserId()));
        return dto;
    }

    // -------------------------------------------------------------------------
    // Post mappings
    // -------------------------------------------------------------------------

    public PostDto toPostDto(Post post, User currentUser) {
        PostDto dto = new PostDto();
        dto.setId(post.getPostId());
        dto.setContent(post.getContent());
        dto.setImageUrl(post.getImageUrl());
        dto.setCreatedAt(post.getCreatedAt());

        dto.setAuthor(post.getAuthor() != null
                ? toUserSummary(post.getAuthor())
                : unknownUser("Unknown Author"));

        dto.setLikesCount(postLikeRepository.countByIdPostId(post.getPostId()));
        dto.setCommentsCount(commentRepository.countByPostPostId(post.getPostId()));
        dto.setLikedByCurrentUser(
                currentUser != null && post.getPostId() != null
                        && postLikeRepository.existsByIdPostIdAndIdUserId(
                                post.getPostId(), currentUser.getUserId()));

        return dto;
    }

    // -------------------------------------------------------------------------
    // Event mappings
    // -------------------------------------------------------------------------

    public EventDto toEventDto(Event event, User currentUser) {
        EventDto dto = new EventDto();
        dto.setId(event.getEventId());
        dto.setTitle(event.getTitle());
        dto.setDescription(event.getDescription());
        dto.setDate(event.getEventDate());
        dto.setLocation(event.getLocation());
        dto.setImageUrl(event.getImageUrl());
        dto.setCreatedAt(event.getCreatedAt());

        dto.setCreatedBy(event.getCreatedBy() != null
                ? toUserSummary(event.getCreatedBy())
                : unknownUser("Unknown Creator"));

        if (event.getCollege() != null) {
            dto.setCollegeId(event.getCollege().getCollegeId());
        }

        dto.setAttendees(event.getAttendees() != null
                ? event.getAttendees().stream()
                        .map(ea -> toUserSummary(ea.getUser()))
                        .collect(Collectors.toList())
                : Collections.emptyList());

        dto.setAttending(
                currentUser != null && event.getEventId() != null
                        && eventAttendeeRepository.existsByIdEventIdAndIdUserId(
                                event.getEventId(), currentUser.getUserId()));

        return dto;
    }

    // -------------------------------------------------------------------------
    // Comment mappings
    // -------------------------------------------------------------------------

    public CommentDto toCommentDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getCommentId());
        dto.setPostId(comment.getPost() != null ? comment.getPost().getPostId() : null);
        dto.setText(comment.getTextContent());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setAuthor(comment.getAuthor() != null
                ? toUserSummary(comment.getAuthor())
                : unknownUser("Unknown Commenter"));
        return dto;
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private UserSummaryDto unknownUser(String name) {
        UserSummaryDto dto = new UserSummaryDto();
        dto.setName(name);
        return dto;
    }
}
