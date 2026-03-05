package com.example.alumniassocaition1.repository;

import com.example.alumniassocaition1.entity.UserFollow;
import com.example.alumniassocaition1.entity.UserFollowId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for {@link UserFollow} entities.
 */
@Repository
public interface UserFollowRepository extends JpaRepository<UserFollow, UserFollowId> {

    List<UserFollow> findByIdFollowerId(Long followerId);

    List<UserFollow> findByIdFollowingId(Long followingId);

    Optional<UserFollow> findByIdFollowerIdAndIdFollowingId(Long followerId, Long followingId);

    void deleteByIdFollowerIdAndIdFollowingId(Long followerId, Long followingId);

    boolean existsByIdFollowerIdAndIdFollowingId(Long followerId, Long followingId);

    long countByIdFollowerId(Long followerId);

    long countByIdFollowingId(Long followingId);

    void deleteByIdFollowerId(Long followerId);

    void deleteByIdFollowingId(Long followingId);
}
