package com.example.alumniassocaition1.exception;

import com.example.alumniassocaition1.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Centralised exception handler that translates domain and framework exceptions
 * into consistent {@link ApiResponse} JSON payloads.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        logger.warn("Resource not found: {}", ex.getMessage());
        return new ResponseEntity<>(new ApiResponse(false, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MyFileNotFoundException.class)
    public ResponseEntity<ApiResponse> handleFileNotFound(MyFileNotFoundException ex, WebRequest request) {
        logger.warn("File not found: {}", ex.getMessage());
        return new ResponseEntity<>(new ApiResponse(false, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ApiResponse> handleFileStorage(FileStorageException ex, WebRequest request) {
        logger.error("File storage error: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(new ApiResponse(false, "File operation failed: " + ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
        logger.warn("Access denied: {}", ex.getMessage());
        return new ResponseEntity<>(
                new ApiResponse(false, "Access Denied: You do not have permission to perform this action."),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse> handleUnauthorized(UnauthorizedException ex, WebRequest request) {
        logger.warn("Unauthorized: {}", ex.getMessage());
        return new ResponseEntity<>(new ApiResponse(false, ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
        logger.warn("Invalid argument: {}", ex.getMessage());
        return new ResponseEntity<>(new ApiResponse(false, "Invalid Input: " + ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse> handleIllegalState(IllegalStateException ex, WebRequest request) {
        logger.error("Illegal state: {}", ex.getMessage());
        return new ResponseEntity<>(new ApiResponse(false, ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidation(MethodArgumentNotValidException ex, WebRequest request) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> String.format("'%s': %s", e.getField(), e.getDefaultMessage()))
                .collect(Collectors.joining("; "));
        logger.warn("Validation error: {}", errorMessage);
        return new ResponseEntity<>(new ApiResponse(false, "Validation failed: " + errorMessage),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse> handleMaxUploadSize(MaxUploadSizeExceededException ex, WebRequest request) {
        logger.warn("File upload size exceeded: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(new ApiResponse(false, "File too large! Maximum size allowed is configured by the server."));
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ApiResponse> handleMultipart(MultipartException ex, WebRequest request) {
        logger.error("Multipart request error: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(
                new ApiResponse(false, "Error processing multipart request. Ensure data is correctly formatted."),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGlobal(Exception ex, WebRequest request) {
        logger.error("Unexpected error:", ex);
        return new ResponseEntity<>(new ApiResponse(false, "An unexpected error occurred. Please try again later."),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
