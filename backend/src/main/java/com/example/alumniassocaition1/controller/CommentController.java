package com.example.alumniassocaition1.controller;

import com.example.alumniassocaition1.dto.ApiResponse;
<<<<<<< HEAD
import com.example.alumniassocaition1.dto.CommentCreateRequest;
import com.example.alumniassocaition1.dto.CommentDto;
=======
import com.example.alumniassocaition1.dto.CommentCreateRequest; // Assuming DTO path
import com.example.alumniassocaition1.dto.CommentDto; // Assuming DTO path
>>>>>>> upstream/main
import com.example.alumniassocaition1.service.CommentService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
<<<<<<< HEAD
=======
import org.springframework.beans.factory.annotation.Autowired;
>>>>>>> upstream/main
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

<<<<<<< HEAD
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
=======
@RestController
@RequestMapping("/api") // Using a base /api, specific paths in methods
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);
    @Autowired
    private CommentService commentService;

    // Get comments for a specific post
>>>>>>> upstream/main
    @GetMapping("/posts/{postId}/comments")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CommentDto>> getCommentsForPost(@PathVariable Long postId) {
        List<CommentDto> comments = commentService.getCommentsForPost(postId);
        return ResponseEntity.ok(comments);
    }

<<<<<<< HEAD
    /** Adds a comment to the specified post. */
    @PostMapping("/posts/{postId}/comments")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentDto> addCommentToPost(
            @PathVariable Long postId,
            @Valid @RequestBody CommentCreateRequest createRequest) {
        logger.info("Adding comment to postId={}", postId);
=======
    // Add a comment to a post
    @PostMapping("/posts/{postId}/comments")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentDto> addCommentToPost(@PathVariable Long postId,
                                                       @Valid @RequestBody CommentCreateRequest createRequest) {
        logger.info("CommentController: addCommentToPost called for postId: {}. Received text: '{}'", postId, createRequest.getText());
>>>>>>> upstream/main
        CommentDto newComment = commentService.addCommentToPost(postId, createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newComment);
    }

<<<<<<< HEAD
    /** Deletes a comment by its ID. */
    @DeleteMapping("/comments/{commentId}")
    @PreAuthorize("isAuthenticated()")
=======
    // Delete a comment
    // The API doc suggested /comments/:commentId or /posts/:postId/comments/:commentId
    // Using /comments/:commentId for simplicity here.
    @DeleteMapping("/comments/{commentId}")
    @PreAuthorize("isAuthenticated()") // Authorization (comment author, post author, or admin) is handled in CommentService
>>>>>>> upstream/main
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(new ApiResponse(true, "Comment deleted successfully."));
    }
}
