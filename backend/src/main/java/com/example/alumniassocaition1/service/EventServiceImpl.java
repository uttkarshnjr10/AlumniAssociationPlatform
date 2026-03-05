package com.example.alumniassocaition1.service;

import com.example.alumniassocaition1.dto.event.EventCreateRequest;
import com.example.alumniassocaition1.dto.event.EventDto;
import com.example.alumniassocaition1.entity.College;
import com.example.alumniassocaition1.entity.Event;
import com.example.alumniassocaition1.entity.EventAttendee;
import com.example.alumniassocaition1.entity.EventAttendeeId;
import com.example.alumniassocaition1.entity.User;
import com.example.alumniassocaition1.exception.ResourceNotFoundException;
import com.example.alumniassocaition1.repository.CollegeRepository;
import com.example.alumniassocaition1.repository.EventAttendeeRepository;
import com.example.alumniassocaition1.repository.EventRepository;
import com.example.alumniassocaition1.util.DtoMapper;
import com.example.alumniassocaition1.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link EventService}.
 */
@Service
public class EventServiceImpl implements EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

    private final EventRepository eventRepository;
    private final EventAttendeeRepository eventAttendeeRepository;
    private final FileStorageService fileStorageService;
    private final CollegeRepository collegeRepository;
    private final SecurityUtils securityUtils;
    private final DtoMapper dtoMapper;

    public EventServiceImpl(EventRepository eventRepository,
            EventAttendeeRepository eventAttendeeRepository,
            FileStorageService fileStorageService,
            CollegeRepository collegeRepository,
            SecurityUtils securityUtils,
            DtoMapper dtoMapper) {
        this.eventRepository = eventRepository;
        this.eventAttendeeRepository = eventAttendeeRepository;
        this.fileStorageService = fileStorageService;
        this.collegeRepository = collegeRepository;
        this.securityUtils = securityUtils;
        this.dtoMapper = dtoMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventDto> getAllEvents() {
        User currentUser = securityUtils.getCurrentUserOrNull();
        List<Event> events;

        if (currentUser != null && currentUser.getCollege() != null) {
            events = eventRepository.findByCollegeCollegeIdOrderByEventDateDesc(
                    currentUser.getCollege().getCollegeId());
        } else {
            events = eventRepository.findAllByOrderByEventDateDesc();
        }

        return events.stream()
                .map(event -> dtoMapper.toEventDto(event, currentUser))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EventDto getEventById(Long eventId) throws ResourceNotFoundException {
        User currentUser = securityUtils.getCurrentUserOrNull();
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));

        // College visibility check
        if (currentUser != null && currentUser.getCollege() != null
                && event.getCollege() != null
                && !currentUser.getCollege().getCollegeId().equals(event.getCollege().getCollegeId())) {
            if (!"admin".equalsIgnoreCase(currentUser.getRole())) {
                throw new AccessDeniedException("You do not have permission to view this event.");
            }
        }

        return dtoMapper.toEventDto(event, currentUser);
    }

    @Override
    @Transactional
    public EventDto createAndSaveEvent(Event event, User creator) {
        validateEventCreator(creator);
        if (event.getCreatedBy() == null)
            event.setCreatedBy(creator);
        event.setCollege(creator.getCollege());

        Event savedEvent = eventRepository.save(event);
        logger.info("Event saved with ID: {}", savedEvent.getEventId());
        return dtoMapper.toEventDto(savedEvent, creator);
    }

    @Override
    @Transactional
    public EventDto createEventFromDto(EventCreateRequest createRequestDto, User currentUser) {
        validateEventCreator(currentUser);

        Event event = new Event();
        event.setTitle(createRequestDto.getTitle());
        event.setDescription(createRequestDto.getDescription());
        event.setEventDate(createRequestDto.getDate());
        event.setLocation(createRequestDto.getLocation());
        event.setCreatedBy(currentUser);
        event.setCollege(currentUser.getCollege());

        Event savedEvent = eventRepository.save(event);
        return dtoMapper.toEventDto(savedEvent, currentUser);
    }

    @Override
    @Transactional
    public EventDto updateEventFromDto(Long eventId, EventCreateRequest updateRequestDto, User currentUser)
            throws ResourceNotFoundException {
        if (currentUser == null)
            throw new AccessDeniedException("Authentication required.");
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));
        validateEventOwnerOrAdmin(event, currentUser);

        event.setTitle(updateRequestDto.getTitle());
        event.setDescription(updateRequestDto.getDescription());
        event.setEventDate(updateRequestDto.getDate());
        event.setLocation(updateRequestDto.getLocation());

        // College update (admin only)
        if (updateRequestDto.getCollegeId() != null && "admin".equalsIgnoreCase(currentUser.getRole())) {
            College targetCollege = collegeRepository.findById(updateRequestDto.getCollegeId())
                    .orElseThrow(() -> new ResourceNotFoundException("College", "id", updateRequestDto.getCollegeId()));
            event.setCollege(targetCollege);
        }

        Event updatedEvent = eventRepository.save(event);
        return dtoMapper.toEventDto(updatedEvent, currentUser);
    }

    @Override
    @Transactional(readOnly = true)
    public Event findEventEntityById(Long eventId) throws ResourceNotFoundException {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));
    }

    @Override
    @Transactional
    public void deleteEvent(Long eventId) throws ResourceNotFoundException {
        User currentUser = securityUtils.getCurrentUser();
        Event event = findEventEntityById(eventId);
        validateEventOwnerOrAdmin(event, currentUser);

        String imageUrl = event.getImageUrl();
        eventRepository.delete(event);
        deleteOldImage(imageUrl);
        logger.info("Event ID {} deleted by User ID {}.", eventId, currentUser.getUserId());
    }

    @Override
    @Transactional
    public void joinEvent(Long eventId) throws ResourceNotFoundException {
        User currentUser = securityUtils.getCurrentUser();
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));

        EventAttendeeId attendeeId = new EventAttendeeId(event.getEventId(), currentUser.getUserId());
        if (eventAttendeeRepository.existsById(attendeeId))
            return;

        EventAttendee attendee = new EventAttendee();
        attendee.setId(attendeeId);
        attendee.setEvent(event);
        attendee.setUser(currentUser);
        eventAttendeeRepository.save(attendee);
        logger.info("User {} joined event {}.", currentUser.getUserId(), eventId);
    }

    @Override
    @Transactional
    public void leaveEvent(Long eventId) throws ResourceNotFoundException {
        User currentUser = securityUtils.getCurrentUser();
        EventAttendeeId attendeeId = new EventAttendeeId(eventId, currentUser.getUserId());
        if (!eventAttendeeRepository.existsById(attendeeId))
            return;

        eventAttendeeRepository.deleteById(attendeeId);
        logger.info("User {} left event {}.", currentUser.getUserId(), eventId);
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private void validateEventCreator(User creator) {
        if (creator == null)
            throw new AccessDeniedException("Authentication required.");
        if (!("alumnus".equalsIgnoreCase(creator.getRole())
                || "admin".equalsIgnoreCase(creator.getRole()))) {
            throw new AccessDeniedException("User does not have permission to create events.");
        }
        if (creator.getCollege() == null) {
            throw new IllegalStateException("User is not associated with a college.");
        }
    }

    private void validateEventOwnerOrAdmin(Event event, User currentUser) {
        boolean isCreator = event.getCreatedBy().getUserId().equals(currentUser.getUserId());
        boolean isAdminOfEventCollege = "admin".equalsIgnoreCase(currentUser.getRole())
                && currentUser.getCollege() != null && event.getCollege() != null
                && currentUser.getCollege().getCollegeId().equals(event.getCollege().getCollegeId());

        if (!isCreator && !isAdminOfEventCollege) {
            throw new AccessDeniedException("User does not have permission for this event.");
        }
    }

    private void deleteOldImage(String imageUrl) {
        if (!StringUtils.hasText(imageUrl))
            return;
        try {
            if (imageUrl.startsWith("http") && imageUrl.contains("cloudinary")) {
                fileStorageService.deleteFile(imageUrl);
            } else {
                String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                if (!fileName.isEmpty())
                    fileStorageService.deleteFile(fileName);
            }
        } catch (Exception e) {
            logger.warn("Could not delete old event image: {}", e.getMessage());
        }
    }
}
