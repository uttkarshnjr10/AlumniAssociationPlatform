package com.example.alumniassocaition1.dto.user;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * Request payload for updating the current user's profile.
 * All fields are optional — only non-null fields are applied.
 */
@Data
public class UserUpdateRequest {

    private String name;
    private String headline;
    private String location;
    private String about;
    private MultipartFile profilePicture;
}
