package com.example.alumniassocaition1.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request payload for creating or updating a post (JSON endpoint).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateRequest {

    @JsonProperty("content")
    @NotBlank(message = "Post content is required")
    private String content;
}
