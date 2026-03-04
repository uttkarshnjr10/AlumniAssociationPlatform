package com.example.alumniassocaition1.controller;

import com.example.alumniassocaition1.dto.ApiResponse;
import com.example.alumniassocaition1.dto.JwtAuthenticationResponse;
import com.example.alumniassocaition1.dto.LoginRequest;
import com.example.alumniassocaition1.dto.user.UserSummaryDto;
import com.example.alumniassocaition1.entity.User;
import com.example.alumniassocaition1.security.JwtTokenProvider;
import com.example.alumniassocaition1.service.UserService;
<<<<<<< HEAD

import jakarta.validation.Valid;
=======
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
>>>>>>> upstream/main
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

<<<<<<< HEAD
/**
 * REST controller for authentication operations.
 *
 * <p>Provides login, logout, and current-user endpoints.
 * Stateless JWT tokens are issued on successful authentication.</p>
 */
=======
>>>>>>> upstream/main
@RestController
@RequestMapping("/api/auth")
public class AuthController {

<<<<<<< HEAD
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider tokenProvider,
                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    /**
     * Authenticates a user and returns a JWT token with user summary.
     */
=======
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    UserService userService;

>>>>>>> upstream/main
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);

            User user = userService.findUserByEmail(loginRequest.getEmail());
<<<<<<< HEAD
            UserSummaryDto userSummary = mapToUserSummary(user);
=======
            UserSummaryDto userSummary = new UserSummaryDto();
            BeanUtils.copyProperties(user, userSummary);
            userSummary.setId(user.getUserId());
>>>>>>> upstream/main

            return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, userSummary));

        } catch (BadCredentialsException e) {
<<<<<<< HEAD
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, "Invalid email or password"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
=======
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, "Invalid email or password"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
>>>>>>> upstream/main
                    .body(new ApiResponse(false, "An error occurred during authentication"));
        }
    }

<<<<<<< HEAD
    /**
     * Clears the server-side security context (client must discard the JWT).
     */
=======

>>>>>>> upstream/main
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(new ApiResponse(true, "Logout successful"));
    }

<<<<<<< HEAD
    /**
     * Returns the profile summary of the currently authenticated user.
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, "User not authenticated"));
        }
        User user = userService.findUserByEmail(authentication.getName());
        UserSummaryDto userSummary = mapToUserSummary(user);
=======
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.status(401).body(new ApiResponse(false, "User not authenticated"));
        }
        String email = authentication.getName();
        User user = userService.findUserByEmail(email);
        UserSummaryDto userSummary = new UserSummaryDto();
        BeanUtils.copyProperties(user, userSummary);
        userSummary.setId(user.getUserId());
>>>>>>> upstream/main
        userSummary.setRole(user.getRole());

        return ResponseEntity.ok(userSummary);
    }
<<<<<<< HEAD

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private UserSummaryDto mapToUserSummary(User user) {
        UserSummaryDto dto = new UserSummaryDto();
        dto.setId(user.getUserId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());
        dto.setProfilePictureUrl(user.getProfilePictureUrl());
        return dto;
    }
=======
>>>>>>> upstream/main
}
