package com.example.alumniassocaition1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
<<<<<<< HEAD
import java.util.HashSet;
import java.util.Set;

/**
 * JPA entity representing a user-authored post within a college feed.
 *
 * <p>A post always belongs to a {@link College} and has an {@link User author}.
 * It may optionally include an image. Comments and likes are cascade-deleted
 * when the post is removed.</p>
 */
=======
import java.util.HashSet; // Import HashSet
import java.util.Set;

>>>>>>> upstream/main
@Entity
@Getter
@Setter
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
<<<<<<< HEAD
    @JoinColumn(name = "college_id", nullable = false)
    private College college;

    @Lob
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;
=======
    @JoinColumn(name = "college_id", nullable = false) // Posts must belong to a college
    private College college;

    @Lob
    @Column(name = "content", columnDefinition = "TEXT") // Ensure TEXT type for potentially long content
    private String content; // Can be blank if imageUrl is present, validated in service
>>>>>>> upstream/main

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
<<<<<<< HEAD
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostLike> likes = new HashSet<>();
=======
    private Set<Comment> comments = new HashSet<>(); // Initialize collections

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostLike> likes = new HashSet<>(); // Initialize collections
>>>>>>> upstream/main

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
        if (this.content == null) { // Ensure content is not null in DB if column is NOT NULL
            this.content = "";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        if (this.content == null) {
            this.content = "";
        }
    }
<<<<<<< HEAD
=======

    // Consider adding custom equals and hashCode if not using @Data
    // and if these entities will be part of Sets or used in HashMaps where identity matters.
    // For simplicity with @Getter @Setter, we'll omit them for now unless issues arise.
>>>>>>> upstream/main
}
