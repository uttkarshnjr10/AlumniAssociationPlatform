package com.example.alumniassocaition1.controller;

import com.example.alumniassocaition1.dto.ApiResponse;
import com.example.alumniassocaition1.dto.auth.JwtAuthenticationResponse;
import com.example.alumniassocaition1.dto.auth.LoginRequest;
import com.example.alumniassocaition1.dto.user.UserSummaryDto;
import com.example.alumniassocaition1.entity.User;
import com.example.alumniassocaition1.exception.ResourceNotFoundException;
import com.example.alumniassocaition1.security.JwtTokenProvider;
import com.example.alumniassocaition1.service.UserService;
import com.example.alumniassocaition1.util.DtoMapper;
import com.example.alumniassocaition1.util.SecurityUtils;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller for authentication (login, register) and current-user info.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtils securityUtils;
    private final DtoMapper dtoMapper;

    public AuthController(AuthenticationManager authenticationManager,
            JwtTokenProvider tokenProvider,
            UserService userService,
            PasswordEncoder passwordEncoder,
            SecurityUtils securityUtils,
            DtoMapper dtoMapper) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.securityUtils = securityUtils;
        this.dtoMapper = dtoMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);

            User user = userService.findUserByEmail(loginRequest.getEmail());
            UserSummaryDto userDto = dtoMapper.toUserSummary(user);

            return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, userDto));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, "Invalid email or password."));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "User not found."));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> registrationRequest) {
        String name = registrationRequest.get("name");
        String email = registrationRequest.get("email");
        String password = registrationRequest.get("password");

        if (name == null || email == null || password == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Name, email, and password are required."));
        }

        try {
            userService.findUserByEmail(email);
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(false, "Email is already in use."));
        } catch (ResourceNotFoundException ignored) {
            // Email not taken — proceed
        }

        // Note: Registration without college association should be handled
        // via CollegeController.registerCollege for full flow
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "User registered. Use college registration for full onboarding."));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        try {
            User user = securityUtils.getCurrentUser();
            UserSummaryDto dto = dtoMapper.toUserSummary(user);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, "Could not authenticate user."));
        }
    }
}
