package com.example.alumniassocaition1.repository;

import com.example.alumniassocaition1.entity.College;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for {@link College} entities.
 */
@Repository
public interface CollegeRepository extends JpaRepository<College, Long> {

    Optional<College> findByName(String name);
}
