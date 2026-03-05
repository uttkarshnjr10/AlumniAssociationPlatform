package com.example.alumniassocaition1.service;

import com.example.alumniassocaition1.dto.event.EventCreateRequest;
import com.example.alumniassocaition1.dto.event.EventDto;
import com.example.alumniassocaition1.entity.Event;
import com.example.alumniassocaition1.entity.User;
import com.example.alumniassocaition1.exception.ResourceNotFoundException;

import java.util.List;

/**
 * Service contract for event lifecycle management including creation,
 * updates, deletion, and attendee join/leave operations.
 */
public interface EventService {

    List<EventDto> getAllEvents();

    EventDto getEventById(Long eventId) throws ResourceNotFoundException;

    EventDto createAndSaveEvent(Event event, User currentUser);

    EventDto createEventFromDto(EventCreateRequest createRequestDto, User currentUser);

    EventDto updateEventFromDto(Long eventId, EventCreateRequest updateRequestDto, User currentUser)
            throws ResourceNotFoundException;

    Event findEventEntityById(Long eventId) throws ResourceNotFoundException;

    void deleteEvent(Long eventId) throws ResourceNotFoundException;

    void joinEvent(Long eventId) throws ResourceNotFoundException;

    void leaveEvent(Long eventId) throws ResourceNotFoundException;
}