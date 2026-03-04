package com.example.alumniassocaition1.dto;

import com.example.alumniassocaition1.dto.user.UserSummaryDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

<<<<<<< HEAD
/**
 * Response DTO representing an event with attendance metadata.
 */
@Data
public class EventDto {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime date;
    private String location;
    private String imageUrl;
    private UserSummaryDto createdBy;
    private Long collegeId;
    private List<UserSummaryDto> attendees;
    private LocalDateTime createdAt;

    /** Whether the currently authenticated user is attending this event. */
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private boolean isAttending;
}
=======
@Data
public class EventDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime date; // This should ideally match the entity's eventDate name or be mapped explicitly
    private String location;
    private String imageUrl; // New field
    private UserSummaryDto createdBy;
    private Long collegeId;
    private List<UserSummaryDto> attendees;
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private boolean isAttending; // Is the current authenticated user attending?
    private LocalDateTime createdAt;
    // private LocalDateTime updatedAt; // Optional: if you want to send this to frontend
}
>>>>>>> upstream/main
