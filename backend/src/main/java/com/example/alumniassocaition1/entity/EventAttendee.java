package com.example.alumniassocaition1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

<<<<<<< HEAD
/**
 * Join entity recording a user’s attendance at an {@link Event}.
 *
 * <p>Uses composite key {@link EventAttendeeId} to enforce one
 * attendance record per user–event pair.</p>
 */
=======
>>>>>>> upstream/main
@Entity
@Getter
@Setter
@Table(name = "event_attendees")
public class EventAttendee {

    @EmbeddedId
    private EventAttendeeId id;

    @ManyToOne(fetch = FetchType.LAZY)
<<<<<<< HEAD
    @MapsId("eventId")
=======
    @MapsId("eventId") // Maps eventId attribute of embedded id
>>>>>>> upstream/main
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
<<<<<<< HEAD
    @MapsId("userId")
=======
    @MapsId("userId") // Maps userId attribute of embedded id
>>>>>>> upstream/main
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "joined_at", updatable = false)
    private LocalDateTime joinedAt;

    @PrePersist
    protected void onCreate() {
        joinedAt = LocalDateTime.now();
    }
}
