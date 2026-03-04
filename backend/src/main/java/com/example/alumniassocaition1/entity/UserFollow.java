package com.example.alumniassocaition1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

<<<<<<< HEAD
/**
 * Join entity representing a follow relationship between two {@link User}s.
 *
 * <p>Uses composite key {@link UserFollowId} to enforce one follow
 * record per follower–following pair.</p>
 */
=======
>>>>>>> upstream/main
@Entity
@Getter
@Setter
@Table(name = "user_follows")
public class UserFollow {

    @EmbeddedId
    private UserFollowId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("followerId")
    @JoinColumn(name = "follower_id")
<<<<<<< HEAD
    private User follower;
=======
    private User follower; // The user doing the following
>>>>>>> upstream/main

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("followingId")
    @JoinColumn(name = "following_id")
<<<<<<< HEAD
    private User following;
=======
    private User following; // The user being followed
>>>>>>> upstream/main

    @Column(name = "followed_at", updatable = false)
    private LocalDateTime followedAt;

    @PrePersist
    protected void onCreate() {
        followedAt = LocalDateTime.now();
    }
}
