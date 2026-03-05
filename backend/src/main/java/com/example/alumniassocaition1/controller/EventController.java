package com.example.alumniassocaition1.controller;

import com.example.alumniassocaition1.dto.ApiResponse;
import com.example.alumniassocaition1.dto.event.EventCreateRequest;
import com.example.alumniassocaition1.dto.event.EventDto;
import com.example.alumniassocaition1.entity.Event;
import com.example.alumniassocaition1.entity.User;
import com.example.alumniassocaition1.service.EventService;
import com.example.alumniassocaition1.service.FileStorageService;
import com.example.alumniassocaition1.util.SecurityUtils;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for event CRUD, attendance, and image serving.
 */
@RestController
@RequestMapping("/api/events")
public class EventController {

    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    private final EventService eventService;
    private final FileStorageService fileStorageService;
    private final SecurityUtils securityUtils;

    public EventController(EventService eventService,
            FileStorageService fileStorageService,
            SecurityUtils securityUtils) {
        this.eventService = eventService;
        this.fileStorageService = fileStorageService;
        this.securityUtils = securityUtils;
    }

    @GetMapping
    public ResponseEntity<List<EventDto>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDto> getEventById(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getEventById(eventId));
    }

    /** Multipart event creation with optional image. */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ALUMNUS', 'ADMIN')")
    public ResponseEntity<EventDto> createEventMultipart(
            @RequestPart("title") String title,
            @RequestPart("description") String description,
            @RequestPart("date") String dateString,
            @RequestPart("location") String location,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        User currentUser = securityUtils.getCurrentUser();
        LocalDateTime eventDate = LocalDateTime.parse(dateString);

        Event event = new Event();
        event.setTitle(title);
        event.setDescription(description);
        event.setEventDate(eventDate);
        event.setLocation(location);

        if (image != null && !image.isEmpty()) {
            String storedResult = fileStorageService.storeFile(image);
            event.setImageUrl(buildFileUrl(storedResult, "/api/events/uploads/"));
        }

        EventDto savedEvent = eventService.createAndSaveEvent(event, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
    }

    /** JSON-only event creation. */
    @PostMapping(value = "/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ALUMNUS', 'ADMIN')")
    public ResponseEntity<EventDto> createEventJson(
            @Valid @RequestBody EventCreateRequest createRequest) {
        User currentUser = securityUtils.getCurrentUser();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(eventService.createEventFromDto(createRequest, currentUser));
    }

    /** Multipart event update with optional image replacement. */
    @PutMapping(value = "/{eventId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ALUMNUS', 'ADMIN')")
    public ResponseEntity<EventDto> updateEventMultipart(
            @PathVariable Long eventId,
            @RequestPart("title") String title,
            @RequestPart("description") String description,
            @RequestPart("date") String dateString,
            @RequestPart("location") String location,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        User currentUser = securityUtils.getCurrentUser();
        Event existingEvent = eventService.findEventEntityById(eventId);

        existingEvent.setTitle(title);
        existingEvent.setDescription(description);
        existingEvent.setEventDate(LocalDateTime.parse(dateString));
        existingEvent.setLocation(location);

        if (image != null && !image.isEmpty()) {
            // Delete old image
            if (existingEvent.getImageUrl() != null) {
                try {
                    String oldFile = existingEvent.getImageUrl()
                            .substring(existingEvent.getImageUrl().lastIndexOf("/") + 1);
                    if (!oldFile.isEmpty())
                        fileStorageService.deleteFile(oldFile);
                } catch (Exception e) {
                    logger.warn("Could not delete old event image: {}", e.getMessage());
                }
            }
            String storedResult = fileStorageService.storeFile(image);
            existingEvent.setImageUrl(buildFileUrl(storedResult, "/api/events/uploads/"));
        }

        return ResponseEntity.ok(eventService.createAndSaveEvent(existingEvent, currentUser));
    }

    /** JSON-only event update. */
    @PutMapping(value = "/{eventId}/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ALUMNUS', 'ADMIN')")
    public ResponseEntity<EventDto> updateEventJson(
            @PathVariable Long eventId,
            @Valid @RequestBody EventCreateRequest updateRequest) {
        User currentUser = securityUtils.getCurrentUser();
        return ResponseEntity.ok(eventService.updateEventFromDto(eventId, updateRequest, currentUser));
    }

    @DeleteMapping("/{eventId}")
    @PreAuthorize("hasAnyRole('ALUMNUS', 'ADMIN')")
    public ResponseEntity<ApiResponse> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.ok(new ApiResponse(true, "Event deleted successfully."));
    }

    @PostMapping("/{eventId}/join")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> joinEvent(@PathVariable Long eventId) {
        eventService.joinEvent(eventId);
        return ResponseEntity.ok(new ApiResponse(true, "Joined event successfully."));
    }

    @PostMapping("/{eventId}/leave")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> leaveEvent(@PathVariable Long eventId) {
        eventService.leaveEvent(eventId);
        return ResponseEntity.ok(new ApiResponse(true, "Left event successfully."));
    }

    /** Serves locally uploaded files. */
    @GetMapping("/uploads/{fileName:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String fileName) {
        Resource resource = fileStorageService.loadFileAsResource(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    private String buildFileUrl(String storedResult, String pathPrefix) {
        if (storedResult.startsWith("http"))
            return storedResult;
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(pathPrefix).path(storedResult).toUriString();
    }
}
