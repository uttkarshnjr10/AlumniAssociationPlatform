package com.example.alumniassocaition1.exception;

/**
 * Thrown when a file storage operation (save, delete) fails.
 */
public class FileStorageException extends RuntimeException {
    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}