package com.example.alumniassocaition1.dto.user;

import lombok.Data;

import java.time.LocalDateTime;

<<<<<<< HEAD
/**
 * Full user profile DTO including social metrics (followers/following counts).
 */
@Data
public class UserProfileDto {

=======
@Data
public class UserProfileDto {
>>>>>>> upstream/main
    private Long id;
    private String name;
    private String email;
    private String role;
    private String headline;
    private String location;
    private String about;
<<<<<<< HEAD
    private String profilePictureUrl;
=======
    private String profilePictureUrl; // This will be a URL to a locally served file
>>>>>>> upstream/main
    private Long followersCount;
    private Long followingCount;
    private LocalDateTime createdAt;
}
