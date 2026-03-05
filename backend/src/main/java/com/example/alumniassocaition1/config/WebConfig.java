package com.example.alumniassocaition1.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

/**
 * Web MVC configuration for serving static resources.
 *
 * <p>
 * Maps the local {@code uploads/} directory to the
 * {@code /api/posts/uploads/**} and {@code /api/events/uploads/**}
 * URL paths so that uploaded images can be served directly.
 * </p>
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir:./uploads}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String resolved = Paths.get(uploadDir).toAbsolutePath().normalize().toString();
        if (!resolved.endsWith(java.io.File.separator)) {
            resolved += java.io.File.separator;
        }

        registry.addResourceHandler("/api/posts/uploads/**", "/api/events/uploads/**")
                .addResourceLocations("file:" + resolved);
    }
}
