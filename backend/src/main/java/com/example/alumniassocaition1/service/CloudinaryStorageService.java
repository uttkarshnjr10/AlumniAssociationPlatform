package com.example.alumniassocaition1.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.alumniassocaition1.exception.FileStorageException;
import com.example.alumniassocaition1.exception.MyFileNotFoundException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Cloudinary-backed implementation of {@link FileStorageService}.
 *
 * <p>
 * Activated only when the {@code cloudinary.cloud-name} property is set.
 * When active, this bean takes precedence over the local
 * {@link FileStorageServiceImpl} via {@code @Primary}.
 * </p>
 */
@Service
@Primary
@ConditionalOnProperty(name = "cloudinary.cloud-name", matchIfMissing = false)
public class CloudinaryStorageService implements FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(CloudinaryStorageService.class);

    private final Cloudinary cloudinary;

    public CloudinaryStorageService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    @PostConstruct
    public void init() throws FileStorageException {
        logger.info("Cloudinary file storage initialized (cloud: {})",
                cloudinary.config.cloudName);
    }

    @Override
    @SuppressWarnings("unchecked")
    public String storeFile(org.springframework.web.multipart.MultipartFile file) throws FileStorageException {
        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "auto",
                            "folder", "alumni-platform"));
            String secureUrl = (String) uploadResult.get("secure_url");
            logger.info("File uploaded to Cloudinary: {}", secureUrl);
            return secureUrl;
        } catch (IOException ex) {
            throw new FileStorageException("Failed to upload file to Cloudinary: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank())
            return;
        try {
            String publicId = extractPublicId(fileUrl);
            if (publicId != null) {
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                logger.info("Deleted file from Cloudinary: {}", publicId);
            }
        } catch (Exception ex) {
            logger.warn("Could not delete file from Cloudinary '{}': {}", fileUrl, ex.getMessage());
        }
    }

    @Override
    public Stream<Path> loadAll() {
        logger.warn("loadAll() is not supported for Cloudinary storage.");
        return Stream.empty();
    }

    @Override
    public Path load(String filename) {
        logger.warn("load() is not supported for Cloudinary storage.");
        return null;
    }

    @Override
    public Resource loadFileAsResource(String filename) throws MyFileNotFoundException {
        try {
            Resource resource = new UrlResource(filename);
            if (resource.exists()) {
                return resource;
            }
            throw new MyFileNotFoundException("File not found at URL: " + filename);
        } catch (MalformedURLException e) {
            throw new MyFileNotFoundException("Invalid file URL: " + filename, e);
        }
    }

    /**
     * Extracts the Cloudinary public ID from a full URL.
     * Example URL:
     * https://res.cloudinary.com/demo/image/upload/v123/alumni-platform/abc123.jpg
     * Returns: alumni-platform/abc123
     */
    private String extractPublicId(String url) {
        try {
            String path = url.substring(url.indexOf("/upload/") + 8);
            // Remove version prefix (e.g., "v123/")
            if (path.matches("^v\\d+/.*")) {
                path = path.substring(path.indexOf("/") + 1);
            }
            // Remove file extension
            int dotIndex = path.lastIndexOf(".");
            if (dotIndex > 0) {
                path = path.substring(0, dotIndex);
            }
            return path;
        } catch (Exception e) {
            logger.warn("Could not extract public ID from URL: {}", url);
            return null;
        }
    }
}
