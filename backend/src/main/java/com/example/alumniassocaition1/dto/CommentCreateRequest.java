package com.example.alumniassocaition1.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

<<<<<<< HEAD
/**
 * Request payload for creating a new comment on a post.
 */
@Data
public class CommentCreateRequest {

    @NotBlank(message = "Comment text is required")
=======
@Data
public class CommentCreateRequest {
    @NotBlank
>>>>>>> upstream/main
    private String text;
}
