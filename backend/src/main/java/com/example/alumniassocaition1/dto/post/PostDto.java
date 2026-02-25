package com.example.alumniassocaition1.dto.post;

import com.example.alumniassocaition1.dto.user.UserSummaryDto;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Response DTO representing a post with engagement metrics.
 */
@Data
public class PostDto {

    private Long id;
    private String content;
    private String imageUrl;
    private UserSummaryDto author;
    private LocalDateTime createdAt;
    private long likesCount;
    private long commentsCount;
    private boolean likedByCurrentUser;
}
