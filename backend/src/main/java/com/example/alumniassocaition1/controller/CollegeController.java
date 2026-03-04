package com.example.alumniassocaition1.controller;

import com.example.alumniassocaition1.dto.college.CollegeDto;
import com.example.alumniassocaition1.dto.college.CollegeRegistrationRequest;
import com.example.alumniassocaition1.service.CollegeService;
<<<<<<< HEAD

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for college registration.
 *
 * <p>Handles self-service college onboarding.
 * New colleges are created with a default admin user.</p>
 */
=======
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

>>>>>>> upstream/main
@RestController
@RequestMapping("/api/colleges")
public class CollegeController {

<<<<<<< HEAD
    private final CollegeService collegeService;

    public CollegeController(CollegeService collegeService) {
        this.collegeService = collegeService;
    }

    /** Registers a new college along with its initial admin account. */
    @PostMapping("/register")
    public ResponseEntity<?> registerCollege(
            @Valid @RequestBody CollegeRegistrationRequest registrationRequest) {
=======
    @Autowired
    private CollegeService collegeService;

    @PostMapping("/register")
    public ResponseEntity<?> registerCollege(@Valid @RequestBody CollegeRegistrationRequest registrationRequest) {
>>>>>>> upstream/main
        CollegeDto newCollege = collegeService.registerCollege(registrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCollege);
    }
}
