package com.example.alumniassocaition1.dto.user;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Full user profile DTO including social metrics (followers/following counts).
 */
@Data
public class UserProfileDto {

    private Long id;
    private String name;
    private String email;
    private String role;
    private String headline;
    private String location;
    private String about;
    private String profilePictureUrl;
    private Long followersCount;
    private Long followingCount;
    private LocalDateTime createdAt;
}
