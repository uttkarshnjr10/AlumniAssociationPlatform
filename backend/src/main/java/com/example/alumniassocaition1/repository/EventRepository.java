package com.example.alumniassocaition1.repository;

import com.example.alumniassocaition1.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for {@link Event} entities.
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByCollegeCollegeIdOrderByEventDateDesc(Long collegeId);

    List<Event> findAllByOrderByEventDateDesc();

    List<Event> findByCreatedByUserId(Long userId);

    List<Event> findByEventDateAfter(LocalDateTime date);
}
