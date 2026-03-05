package com.example.alumniassocaition1.service;

import com.example.alumniassocaition1.exception.FileStorageException;
import com.example.alumniassocaition1.exception.MyFileNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Service contract for file storage operations (upload, download, delete).
 *
 * <p>
 * Implementations may store files locally or in a cloud service like
 * Cloudinary.
 * </p>
 */
public interface FileStorageService {

    /** Initializes the storage backend. */
    void init() throws FileStorageException;

    /**
     * Stores a file and returns a public URL or filename.
     *
     * @param file the uploaded file
     * @return the stored file's URL (cloud) or filename (local)
     */
    String storeFile(MultipartFile file) throws FileStorageException;

    /** Loads all stored file paths (local implementation). */
    Stream<Path> loadAll() throws FileStorageException;

    /** Resolves a filename to its local path (local implementation). */
    Path load(String filename);

    /** Loads a file as a Spring Resource for download. */
    Resource loadFileAsResource(String filename) throws MyFileNotFoundException;

    /** Deletes a file by filename or URL. */
    void deleteFile(String filename);
}
