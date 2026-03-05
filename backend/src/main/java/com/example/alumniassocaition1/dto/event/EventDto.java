package com.example.alumniassocaition1.dto.event;

import com.example.alumniassocaition1.dto.user.UserSummaryDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

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

    @JsonInclude(JsonInclude.Include.ALWAYS)
    private boolean isAttending;
}
