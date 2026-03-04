package com.example.alumniassocaition1.repository;

import com.example.alumniassocaition1.entity.Post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

<<<<<<< HEAD
/**
 * Spring Data JPA repository for {@link Post} entities.
 *
 * <p>Supports college-scoped paginated feeds and per-user post lookups.</p>
 */
=======
>>>>>>> upstream/main
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByAuthorCollegeCollegeIdOrderByCreatedAtDesc(Long collegeId, Pageable pageable);

    List<Post> findByAuthorUserIdOrderByCreatedAtDesc(Long userId);
    // Add custom query methods if needed
}
