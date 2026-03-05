package main.java.com.example.alumniassocaition1.dto.comment;

import com.example.alumniassocaition1.dto.user.UserSummaryDto;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Response DTO representing a comment on a post.
 */
@Data
public class CommentDto {

    private Long id;
    private Long postId;
    private UserSummaryDto author;
    private String text;
    private LocalDateTime createdAt;
}
