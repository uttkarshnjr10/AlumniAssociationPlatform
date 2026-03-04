package com.example.alumniassocaition1.dto.user;

import lombok.Data;

<<<<<<< HEAD
/**
 * Request payload for updating the current user's profile.
 * All fields are optional — only non-null fields are applied.
 */
@Data
public class UserUpdateRequest {

=======
@Data
public class UserUpdateRequest {
>>>>>>> upstream/main
    private String name;
    private String headline;
    private String location;
    private String about;
<<<<<<< HEAD
=======
    // profilePictureUrl might be handled by a separate endpoint if using local storage for profile pics
    // Or, if updating via this DTO, the backend would handle saving the new file and updating the URL.
    // For simplicity, we'll assume profile picture updates might need a dedicated endpoint or
    // the URL is manually set if not uploading a new file.
>>>>>>> upstream/main
}
