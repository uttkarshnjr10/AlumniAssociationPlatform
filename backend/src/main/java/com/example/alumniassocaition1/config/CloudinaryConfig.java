package main.java.com.example.alumniassocaition1.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cloudinary SDK configuration.
 *
 * <p>
 * Creates a {@link Cloudinary} bean from environment-backed properties.
 * Set the following environment variables before deploying:
 * </p>
 * <ul>
 * <li>{@code CLOUDINARY_CLOUD_NAME}</li>
 * <li>{@code CLOUDINARY_API_KEY}</li>
 * <li>{@code CLOUDINARY_API_SECRET}</li>
 * </ul>
 */
@Configuration
public class CloudinaryConfig {

    @Value("${cloudinary.cloud-name:}")
    private String cloudName;

    @Value("${cloudinary.api-key:}")
    private String apiKey;

    @Value("${cloudinary.api-secret:}")
    private String apiSecret;

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true));
    }
}
