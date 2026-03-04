package com.example.alumniassocaition1.service;
<<<<<<< HEAD

import com.example.alumniassocaition1.dto.CommentCreateRequest;
=======
import com.example.alumniassocaition1.controller.PostController;
import com.example.alumniassocaition1.dto.CommentCreateRequest; // Need to import this DTO
>>>>>>> upstream/main
import com.example.alumniassocaition1.dto.CommentDto;
import com.example.alumniassocaition1.dto.user.UserSummaryDto;
import com.example.alumniassocaition1.entity.Comment;
import com.example.alumniassocaition1.entity.Post;
import com.example.alumniassocaition1.entity.User;
<<<<<<< HEAD
import com.example.alumniassocaition1.exception.ResourceNotFoundException;
=======
import com.example.alumniassocaition1.exception.ResourceNotFoundException; // Import custom exceptions
>>>>>>> upstream/main
import com.example.alumniassocaition1.repository.CommentRepository;
import com.example.alumniassocaition1.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
<<<<<<< HEAD
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link CommentService}.
 *
 * <p>Handles comment CRUD for posts. Deletion is permitted for the comment
 * author, the post author, or any college admin.</p>
 */
@Service
public class CommentServiceImpl implements CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserService userService;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository,
                              PostRepository postRepository,
                              UserService userService) {
=======
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException; // Import AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors; // Need to import Collectors

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);
    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, UserService userService) {
>>>>>>> upstream/main
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userService = userService;
    }

<<<<<<< HEAD
=======
    private User getCurrentAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else if (principal != null) { // Handle cases where principal might not be UserDetails but is not null
            username = principal.toString();
        }
        else {
            throw new AccessDeniedException("User authentication not found."); // Or handle appropriately
        }
        // Assuming userService.findUserByEmail throws ResourceNotFoundException if not found
        return userService.findUserByEmail(username);
    }

>>>>>>> upstream/main
    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsForPost(Long postId) throws ResourceNotFoundException {
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post", "id", postId);
        }
        return commentRepository.findByPostPostIdOrderByCreatedAtAsc(postId).stream()
                .map(this::mapCommentToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto addCommentToPost(Long postId, CommentCreateRequest createRequest) throws ResourceNotFoundException {
<<<<<<< HEAD
        User currentUser = userService.getCurrentAuthenticatedUserEntity();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        Comment comment = new Comment();
        comment.setTextContent(createRequest.getText());
        comment.setPost(post);
        comment.setAuthor(currentUser);

        Comment savedComment = commentRepository.save(comment);
        logger.info("Comment {} added to post {} by user {}.",
                savedComment.getCommentId(), postId, currentUser.getUserId());
=======
        User currentUser = getCurrentAuthenticatedUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        logger.info("CommentService: addCommentToPost. Received DTO text from controller: '{}'", createRequest.getText());

        Comment comment = new Comment();
        comment.setTextContent(createRequest.getText()); // This is the crucial assignment
        comment.setPost(post);
        comment.setAuthor(currentUser);

        // Log what is being set to the entity right before saving
        logger.info("CommentService: Comment entity text_content being set before save: '{}'", comment.getTextContent());

        Comment savedComment = commentRepository.save(comment);

        // Log what was actually saved (though Hibernate might not reflect it immediately if it's a proxy)
        // More reliable is to check the database directly after this, or log the 'savedComment' details after mapping.
        logger.info("CommentService: Comment saved with ID: {}. Text content from saved entity (if available): '{}'", savedComment.getCommentId(), savedComment.getTextContent());
>>>>>>> upstream/main

        return mapCommentToDto(savedComment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) throws ResourceNotFoundException {
<<<<<<< HEAD
        User currentUser = userService.getCurrentAuthenticatedUserEntity();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        boolean isAuthor = comment.getAuthor().getUserId().equals(currentUser.getUserId());
        boolean isPostOwner = comment.getPost().getAuthor().getUserId().equals(currentUser.getUserId());
        boolean isAdmin = "admin".equalsIgnoreCase(currentUser.getRole());

        if (!(isAuthor || isPostOwner || isAdmin)) {
            throw new AccessDeniedException("User does not have permission to delete this comment.");
        }

        commentRepository.delete(comment);
        logger.info("Comment {} deleted by user {}.", commentId, currentUser.getUserId());
    }

    // -------------------------------------------------------------------------
    // Mapping helpers
    // -------------------------------------------------------------------------

    private CommentDto mapCommentToDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getCommentId());
        dto.setPostId(comment.getPost().getPostId());
        // Explicit mapping — BeanUtils won't auto-map textContent → text
        dto.setText(comment.getTextContent());
        dto.setCreatedAt(comment.getCreatedAt());

        if (comment.getAuthor() != null) {
            UserSummaryDto authorDto = new UserSummaryDto();
            authorDto.setId(comment.getAuthor().getUserId());
            authorDto.setName(comment.getAuthor().getName());
            authorDto.setEmail(comment.getAuthor().getEmail());
            authorDto.setRole(comment.getAuthor().getRole());
            authorDto.setStatus(comment.getAuthor().getStatus());
=======
        User currentUser = getCurrentAuthenticatedUser();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        boolean canDelete = comment.getAuthor().getUserId().equals(currentUser.getUserId()) ||
                comment.getPost().getAuthor().getUserId().equals(currentUser.getUserId()) ||
                "admin".equalsIgnoreCase(currentUser.getRole());

        if (!canDelete) {
            // Use Spring's AccessDeniedException for authorization failures
            throw new AccessDeniedException("User does not have permission to delete this comment.");
        }
        commentRepository.delete(comment);
    }

    private CommentDto mapCommentToDto(Comment comment) {
        CommentDto dto = new CommentDto();
        BeanUtils.copyProperties(comment, dto);
        dto.setId(comment.getCommentId());
        dto.setPostId(comment.getPost().getPostId());
        if (comment.getAuthor() != null) {
            UserSummaryDto authorDto = new UserSummaryDto();
            BeanUtils.copyProperties(comment.getAuthor(), authorDto);
            authorDto.setId(comment.getAuthor().getUserId());
>>>>>>> upstream/main
            dto.setAuthor(authorDto);
        }
        return dto;
    }
<<<<<<< HEAD
=======
    // Removed extra closing brace
>>>>>>> upstream/main
}
