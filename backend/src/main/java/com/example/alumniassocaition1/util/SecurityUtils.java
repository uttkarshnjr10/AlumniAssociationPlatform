package main.java.com.example.alumniassocaition1.util;

import com.example.alumniassocaition1.entity.User;
import com.example.alumniassocaition1.exception.ResourceNotFoundException;
import com.example.alumniassocaition1.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Centralised helper for extracting the currently authenticated user.
 *
 * <p>
 * Replaces the duplicated {@code getCurrentUser()} /
 * {@code getCurrentAdminUser()}
 * patterns that were scattered across service implementations.
 * </p>
 */
@Component
public class SecurityUtils {

    private final UserRepository userRepository;

    public SecurityUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Returns the authenticated {@link User} entity.
     *
     * @throws AccessDeniedException     if no valid authentication is present
     * @throws ResourceNotFoundException if the authenticated email has no matching
     *                                   user
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new AccessDeniedException("User not authenticated.");
        }

        String email = extractEmail(authentication);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    /**
     * Returns the authenticated user only if they hold the {@code admin} role.
     *
     * @throws AccessDeniedException if the user is not an admin
     */
    public User getCurrentAdminUser() {
        User user = getCurrentUser();
        if (!"admin".equalsIgnoreCase(user.getRole())) {
            throw new AccessDeniedException("User is not an admin.");
        }
        return user;
    }

    /**
     * Returns the authenticated user, or {@code null} if nobody is logged in.
     * Useful for read-only endpoints that behave differently for guests.
     */
    public User getCurrentUserOrNull() {
        try {
            return getCurrentUser();
        } catch (AccessDeniedException | ResourceNotFoundException e) {
            return null;
        }
    }

    private String extractEmail(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }
        return principal.toString();
    }
}
