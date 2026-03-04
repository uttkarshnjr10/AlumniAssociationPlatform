<<<<<<< HEAD
=======
// File: com/example/alumniassocaition1/dto/EventCreateRequest.java
>>>>>>> upstream/main
package com.example.alumniassocaition1.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

<<<<<<< HEAD
/**
 * Immutable request payload for creating or updating an event (JSON endpoint).
 *
 * <p>Uses {@link JsonCreator} for explicit deserialization to guarantee
 * all required fields are provided at construction time.</p>
 */
public class EventCreateRequest {

    @NotBlank(message = "Title is required")
    private final String title;

    @NotBlank(message = "Description is required")
    private final String description;

    @NotNull(message = "Date is required")
    @Future(message = "Event date must be in the future")
    private final LocalDateTime date;

    @NotBlank(message = "Location is required")
=======
public class EventCreateRequest {

    @NotBlank(message = "must not be blank")
    private final String title;

    @NotBlank(message = "must not be blank")
    private final String description;

    @NotNull(message = "must not be null")
    @Future
    private final LocalDateTime date;

    @NotBlank(message = "must not be blank")
>>>>>>> upstream/main
    private final String location;

    private final Long collegeId;

    @JsonCreator
    public EventCreateRequest(
            @JsonProperty("title") String title,
            @JsonProperty("description") String description,
            @JsonProperty("date") LocalDateTime date,
            @JsonProperty("location") String location,
            @JsonProperty("collegeId") Long collegeId) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.location = location;
        this.collegeId = collegeId;
    }

<<<<<<< HEAD
    public String getTitle() { return title; }

    public String getDescription() { return description; }

    public LocalDateTime getDate() { return date; }

    public String getLocation() { return location; }

    public Long getCollegeId() { return collegeId; }
=======
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public Long getCollegeId() {
        return collegeId;
    }
>>>>>>> upstream/main
}
