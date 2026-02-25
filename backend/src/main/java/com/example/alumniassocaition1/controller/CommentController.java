package com.example.alumniassocaition1.controller;

import com.example.alumniassocaition1.dto.ApiResponse;
import com.example.alumniassocaition1.dto.CommentCreateRequest;
import com.example.alumniassocaition1.dto.CommentDto;
import com.example.alumniassocaition1.service.CommentService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for post comments.
 *
 * <p>Provides endpoints to list, create, and delete comments.
 * Authorization for deletion is enforced at the service layer.</p>
 */
@RestController
@RequestMapping("/api")
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /** Returns all comments for the given post. */
    @GetMapping("/posts/{postId}/comments")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CommentDto>> getCommentsForPost(@PathVariable Long postId) {
        List<CommentDto> comments = commentService.getCommentsForPost(postId);
        return ResponseEntity.ok(comments);
    }

    /** Adds a comment to the specified post. */
    @PostMapping("/posts/{postId}/comments")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentDto> addCommentToPost(
            @PathVariable Long postId,
            @Valid @RequestBody CommentCreateRequest createRequest) {
        logger.info("Adding comment to postId={}", postId);
        CommentDto newComment = commentService.addCommentToPost(postId, createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newComment);
    }

    /** Deletes a comment by its ID. */
    @DeleteMapping("/comments/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(new ApiResponse(true, "Comment deleted successfully."));
    }
}
