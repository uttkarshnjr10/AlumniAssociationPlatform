package com.example.alumniassocaition1.controller;

import com.example.alumniassocaition1.dto.ApiResponse;
<<<<<<< HEAD
import com.example.alumniassocaition1.dto.EventCreateRequest;
import com.example.alumniassocaition1.dto.EventDto;
=======
import com.example.alumniassocaition1.dto.EventDto;
import com.example.alumniassocaition1.dto.EventCreateRequest; // For JSON endpoint
>>>>>>> upstream/main
import com.example.alumniassocaition1.entity.Event;
import com.example.alumniassocaition1.entity.User;
import com.example.alumniassocaition1.exception.MyFileNotFoundException;
import com.example.alumniassocaition1.service.EventService;
import com.example.alumniassocaition1.service.FileStorageService;
import com.example.alumniassocaition1.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
<<<<<<< HEAD
=======
import org.springframework.beans.factory.annotation.Autowired;
>>>>>>> upstream/main
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

<<<<<<< HEAD
/**
 * REST controller for event management.
 *
 * <p>Supports both multipart (with image upload) and JSON-only
 * endpoints for creating and updating events. Also handles
 * join/leave actions and serves uploaded event images.</p>
 */
=======
>>>>>>> upstream/main
@RestController
@RequestMapping("/api/events")
public class EventController {

    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

<<<<<<< HEAD
    private final EventService eventService;
    private final FileStorageService fileStorageService;
    private final UserService userService;

    public EventController(EventService eventService,
                           FileStorageService fileStorageService,
                           UserService userService) {
        this.eventService = eventService;
        this.fileStorageService = fileStorageService;
        this.userService = userService;
    }

    // -------------------------------------------------------------------------
    // Query endpoints
    // -------------------------------------------------------------------------

    /** Returns all events visible to the current user (college-scoped). */
    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<EventDto>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    /** Returns a single event by its ID. */
    @GetMapping("/{eventId}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<EventDto> getEventById(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getEventById(eventId));
    }

    // -------------------------------------------------------------------------
    // Create endpoints
    // -------------------------------------------------------------------------

    /** Creates an event from multipart form data (supports image upload). */
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAnyRole('ALUMNUS', 'ADMIN')")
    public ResponseEntity<?> createEventMultipart(
            @RequestPart("title") String title,
            @RequestPart("description") String description,
            @RequestPart("date") String dateStr,
            @RequestPart("time") String timeStr,
=======
    @Autowired
    private EventService eventService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UserService userService;

    // --- GET, DELETE, JOIN/LEAVE (remain same) ---
    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<EventDto>> getAllEvents() { /* ... */ return ResponseEntity.ok(eventService.getAllEvents()); }

    @GetMapping("/{eventId}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<EventDto> getEventById(@PathVariable Long eventId) { /* ... */ return ResponseEntity.ok(eventService.getEventById(eventId)); }


    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAnyRole('ALUMNUS', 'ADMIN')")
    public ResponseEntity<?> createEventWithIndividualParts(
            @RequestPart("title") String title,
            @RequestPart("description") String description,
            @RequestPart("date") String dateStr, // Expecting "YYYY-MM-DD"
            @RequestPart("time") String timeStr, // Expecting "HH:MM" or "HH:MM:SS"
>>>>>>> upstream/main
            @RequestPart("location") String location,
            @RequestPart(value = "collegeId", required = false) String collegeIdStr,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {

<<<<<<< HEAD
        User currentUser = resolveCurrentUser();

        if (!StringUtils.hasText(title)) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Title must not be blank."));
        }

        LocalDateTime eventDateTime;
        try {
            LocalDate parsedDate = LocalDate.parse(dateStr);
            LocalTime parsedTime = LocalTime.parse(timeStr);
            eventDateTime = LocalDateTime.of(parsedDate, parsedTime);

            if (eventDateTime.isBefore(LocalDateTime.now())) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Event date and time must be in the future."));
            }
        } catch (DateTimeParseException e) {
            logger.error("Invalid date/time format: date='{}', time='{}': {}", dateStr, timeStr, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false,
                            "Invalid date or time format. Date should be YYYY-MM-DD, Time should be HH:MM or HH:MM:SS."));
=======
        logger.info("MULTIPART POST /api/events hit. Title: [{}], DateStr: [{}], TimeStr: [{}]", title, dateStr, timeStr);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        User currentUser = userService.findUserByEmail(userPrincipal.getUsername());

        // Basic Validations
        if (!StringUtils.hasText(title)) return ResponseEntity.badRequest().body(new ApiResponse(false, "Title must not be blank."));
        // ... other similar validations for description, location, dateStr, timeStr ...

        LocalDateTime eventDateTime;
        try {
            // LocalDate.parse() correctly handles "YYYY-MM-DD" by default (ISO_LOCAL_DATE)
            LocalDate parsedDate = LocalDate.parse(dateStr);

            // LocalTime.parse() correctly handles "HH:MM" and "HH:MM:SS" by default
            LocalTime parsedTime = LocalTime.parse(timeStr);

            eventDateTime = LocalDateTime.of(parsedDate, parsedTime);

            if (eventDateTime.isBefore(LocalDateTime.now())) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Event date and time must be in the future."));
            }
        } catch (DateTimeParseException e) {
            logger.error("Invalid date/time format: date='{}', time='{}'. Error: {}", dateStr, timeStr, e.getMessage(), e);
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid date or time format. Date should be YYYY-MM-DD, Time should be HH:MM or HH:MM:SS."));
>>>>>>> upstream/main
        }

        Event event = new Event();
        event.setTitle(title);
        event.setDescription(description);
        event.setEventDate(eventDateTime);
        event.setLocation(location);
        event.setCreatedBy(currentUser);
<<<<<<< HEAD
        event.setCollege(currentUser.getCollege());

        if (StringUtils.hasText(collegeIdStr)) {
            try {
                Long.parseLong(collegeIdStr);
            } catch (NumberFormatException e) {
                logger.warn("Invalid collegeId parameter: {}", collegeIdStr);
            }
=======
        event.setCollege(currentUser.getCollege()); // Default

        // Handle collegeIdStr if provided (simplified for example)
        if (StringUtils.hasText(collegeIdStr)) {
            try { Long.parseLong(collegeIdStr); /* Further logic to set college by ID if needed */ }
            catch (NumberFormatException e) { logger.warn("Invalid collegeIdStr: {}", collegeIdStr); }
>>>>>>> upstream/main
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String fileName = fileStorageService.storeFile(imageFile);
                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/events/uploads/").path(fileName).toUriString();
                event.setImageUrl(fileDownloadUri);
            } catch (Exception e) {
<<<<<<< HEAD
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse(false, "Failed to upload image: " + e.getMessage()));
=======
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Failed to upload image: " + e.getMessage()));
>>>>>>> upstream/main
            }
        }

        EventDto savedEventDto = eventService.createAndSaveEvent(event, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEventDto);
    }

<<<<<<< HEAD
    /** Creates an event from a JSON request body. */
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasAnyRole('ALUMNUS', 'ADMIN')")
    public ResponseEntity<EventDto> createEventJson(
            @Valid @RequestBody EventCreateRequest createRequestDto) {
        User currentUser = resolveCurrentUser();
=======
    // JSON-only POST endpoint (using EventCreateRequest DTO)
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasAnyRole('ALUMNUS', 'ADMIN')")
    public ResponseEntity<EventDto> createEventWithJson(@Valid @RequestBody EventCreateRequest createRequestDto) {
        // ... (implementation using createRequestDto)
        // This would call a service method like eventService.createEventFromDto(...)
        User currentUser = userService.findUserByEmail(((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
>>>>>>> upstream/main
        EventDto newEvent = eventService.createEventFromDto(createRequestDto, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(newEvent);
    }

<<<<<<< HEAD
    // -------------------------------------------------------------------------
    // Update endpoints
    // -------------------------------------------------------------------------

    /** Updates an event via multipart form data (supports image replacement). */
    @PutMapping(value = "/{eventId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateEventMultipart(
=======

    // PUT for multipart (similar changes for dateTime parsing)
    @PutMapping(value = "/{eventId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateEventWithIndividualParts(
>>>>>>> upstream/main
            @PathVariable Long eventId,
            @RequestPart("title") String title,
            @RequestPart("description") String description,
            @RequestPart("date") String dateStr,
            @RequestPart("time") String timeStr,
            @RequestPart("location") String location,
            @RequestPart(value = "collegeId", required = false) String collegeIdStr,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {

<<<<<<< HEAD
        User currentUser = resolveCurrentUser();
        Event existingEvent = eventService.findEventEntityById(eventId);
        if (existingEvent == null) {
            return ResponseEntity.notFound().build();
        }

        if (!(existingEvent.getCreatedBy().getUserId().equals(currentUser.getUserId())
                || "admin".equalsIgnoreCase(currentUser.getRole()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse(false, "Not authorized."));
=======
        User currentUser = userService.findUserByEmail(((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        Event existingEvent = eventService.findEventEntityById(eventId); // Fetch existing
        if (existingEvent == null) return ResponseEntity.notFound().build();

        // Authorization
        if (!(existingEvent.getCreatedBy().getUserId().equals(currentUser.getUserId()) || "admin".equalsIgnoreCase(currentUser.getRole()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(false, "Not authorized."));
>>>>>>> upstream/main
        }

        LocalDateTime eventDateTime;
        try {
            LocalDate parsedDate = LocalDate.parse(dateStr);
            LocalTime parsedTime = LocalTime.parse(timeStr);
            eventDateTime = LocalDateTime.of(parsedDate, parsedTime);
<<<<<<< HEAD
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Invalid date/time format for update."));
=======
            // Future check might be optional for updates or different
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid date/time format for update."));
>>>>>>> upstream/main
        }

        existingEvent.setTitle(title);
        existingEvent.setDescription(description);
        existingEvent.setEventDate(eventDateTime);
        existingEvent.setLocation(location);
<<<<<<< HEAD

        if (imageFile != null && !imageFile.isEmpty()) {
            deleteExistingImage(existingEvent.getImageUrl());
            String fileName = fileStorageService.storeFile(imageFile);
            existingEvent.setImageUrl(
                    ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/api/events/uploads/").path(fileName).toUriString());
        }

        EventDto updatedEventDto = eventService.createAndSaveEvent(existingEvent, currentUser);
        return ResponseEntity.ok(updatedEventDto);
    }

    // -------------------------------------------------------------------------
    // Delete / Join / Leave
    // -------------------------------------------------------------------------

    /** Deletes an event. Authorization is enforced at the service layer. */
    @DeleteMapping("/{eventId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.ok(new ApiResponse(true, "Event deleted successfully."));
    }

    /** Adds the current user as an attendee of the event. */
    @PostMapping("/{eventId}/join")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> joinEvent(@PathVariable Long eventId) {
        eventService.joinEvent(eventId);
        return ResponseEntity.ok(new ApiResponse(true, "Successfully joined event."));
    }

    /** Removes the current user from the event's attendee list. */
    @DeleteMapping("/{eventId}/join")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> leaveEvent(@PathVariable Long eventId) {
        eventService.leaveEvent(eventId);
        return ResponseEntity.ok(new ApiResponse(true, "Successfully left event."));
    }

    // -------------------------------------------------------------------------
    // File serving
    // -------------------------------------------------------------------------

    /** Serves an uploaded event image by filename. */
    @GetMapping("/uploads/{filename:.+}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Resource> serveEventFile(@PathVariable String filename,
                                                   HttpServletRequest request) {
        Resource resource;
        try {
            resource = fileStorageService.loadFileAsResource(filename);
        } catch (MyFileNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type for event image: {}", filename);
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private User resolveCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        return userService.findUserByEmail(principal.getUsername());
    }

    private void deleteExistingImage(String imageUrl) {
        if (imageUrl == null) {
            return;
        }
        try {
            String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            if (!fileName.isEmpty()) {
                fileStorageService.deleteFile(fileName);
            }
        } catch (Exception e) {
            logger.warn("Could not delete previous event image: {}", e.getMessage());
        }
    }
=======
        // Handle collegeId update...

        if (imageFile != null && !imageFile.isEmpty()) {
            if (existingEvent.getImageUrl() != null) { /* delete old image */
                try { fileStorageService.deleteFile(existingEvent.getImageUrl().substring(existingEvent.getImageUrl().lastIndexOf("/") + 1)); } catch (Exception e) {/* log */}
            }
            String fileName = fileStorageService.storeFile(imageFile);
            existingEvent.setImageUrl(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/events/uploads/").path(fileName).toUriString());
        }

        EventDto updatedEventDto = eventService.createAndSaveEvent(existingEvent, currentUser); // Re-use save method
        return ResponseEntity.ok(updatedEventDto);
    }

    // GET /uploads/{filename}
    @GetMapping("/uploads/{filename:.+}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Resource> serveEventFile(@PathVariable String filename, HttpServletRequest request) {
        // ... (same as before)
        Resource resource;
        try { resource = fileStorageService.loadFileAsResource(filename); }
        catch (MyFileNotFoundException ex) { return ResponseEntity.notFound().build(); }
        String contentType = null;
        try { contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath()); }
        catch (IOException ex) { logger.info("Could not determine file type for event image: {}. Error: {}", filename, ex.getMessage()); }
        if (contentType == null) { contentType = "application/octet-stream"; }
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"").body(resource);
    }

    @DeleteMapping("/{eventId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> deleteEvent(@PathVariable Long eventId) { eventService.deleteEvent(eventId); return ResponseEntity.ok(new ApiResponse(true, "Event deleted successfully.")); }
    @PostMapping("/{eventId}/join")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> joinEvent(@PathVariable Long eventId) { eventService.joinEvent(eventId); return ResponseEntity.ok(new ApiResponse(true, "Successfully joined event.")); }
    @DeleteMapping("/{eventId}/join")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> leaveEvent(@PathVariable Long eventId) { eventService.leaveEvent(eventId); return ResponseEntity.ok(new ApiResponse(true, "Successfully left event.")); }
>>>>>>> upstream/main
}
