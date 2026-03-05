package com.example.alumniassocaition1.repository;

import com.example.alumniassocaition1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for {@link User} entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByCollegeCollegeId(Long collegeId);

    List<User> findByNameContainingIgnoreCase(String name);

    boolean existsByEmail(String email);

    List<User> findByCollegeCollegeIdAndNameContainingIgnoreCase(Long collegeId, String name);

    List<User> findByCollegeCollegeIdAndEmailContainingIgnoreCase(Long collegeId, String email);

    @Query("SELECT u FROM User u WHERE u.college.collegeId = :collegeId AND " +
            "(LOWER(u.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<User> searchUsersInCollegeByNameOrEmail(@Param("collegeId") Long collegeId,
            @Param("searchTerm") String searchTerm);
}