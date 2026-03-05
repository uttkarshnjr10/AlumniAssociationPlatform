package com.example.alumniassocaition1.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Request payload for creating a new comment on a post.
 */
@Data
public class CommentCreateRequest {

    @NotBlank(message = "Comment text is required")
    private String text;
}
