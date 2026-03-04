package com.example.alumniassocaition1.security;

import io.jsonwebtoken.*;
<<<<<<< HEAD
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
=======
// Specific import
import io.jsonwebtoken.security.Keys;
// Removed: import io.jsonwebtoken.io.Decoders; // Was unused
>>>>>>> upstream/main

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Date;

<<<<<<< HEAD
/**
 * Utility component for JWT token generation, parsing, and validation.
 *
 * <p>Uses HMAC-SHA512 signing algorithm. The secret key is derived from
 * the {@code app.jwtSecret} property in application.properties.</p>
 */
=======
>>>>>>> upstream/main
@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwtSecret}")
    private String jwtSecretString;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

<<<<<<< HEAD
    private SecretKey signingKey;

    /**
     * Initializes the HMAC signing key from the configured secret.
     * Falls back to a generated key if the configured secret is too short.
     */
    @PostConstruct
    public void init() {
        if (jwtSecretString == null || jwtSecretString.getBytes().length < 64) {
            logger.warn("JWT secret is missing or too short for HS512 (requires >= 64 bytes). "
                    + "Generating a random key. Configure 'app.jwtSecret' for production.");
            this.signingKey = Jwts.SIG.HS512.key().build();
        } else {
            this.signingKey = Keys.hmacShaKeyFor(jwtSecretString.getBytes());
        }
    }

    /**
     * Generates a JWT token for the authenticated user.
     *
     * @param authentication the Spring Security authentication object
     * @return signed JWT token string
     */
=======
    private SecretKey jwtSecretKey;

    @PostConstruct
    public void init() {
        // Ensure the secret is strong enough for HS512 (at least 64 bytes)
        if (jwtSecretString == null || jwtSecretString.getBytes().length < 64) { // Check byte length for more accuracy
            logger.warn("JWT Secret ('app.jwtSecret') is weak, not configured, or too short (requires at least 64 bytes for HS512). " +
                    "Using a default, dynamically generated secure key. PLEASE CONFIGURE a strong 'app.jwtSecret' in your application.properties for production.");
            this.jwtSecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512); // Generates a secure key
        } else {
            // Assuming jwtSecretString is a plain string. If it were Base64 encoded, you'd use Decoders.BASE64.decode()
            this.jwtSecretKey = Keys.hmacShaKeyFor(jwtSecretString.getBytes());
        }
    }

>>>>>>> upstream/main
    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
<<<<<<< HEAD
                .subject(userPrincipal.getUsername())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(signingKey, Jwts.SIG.HS512)
                .compact();
    }

    /**
     * Extracts the username (email) from a JWT token.
     *
     * @param token the JWT token string
     * @return the subject (email) claim
     */
    public String getUsernameFromJWT(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * Validates a JWT token's signature and expiration.
     *
     * @param authToken the JWT token string to validate
     * @return {@code true} if the token is valid, {@code false} otherwise
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (SecurityException ex) {
            logger.error("Invalid JWT signature: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            logger.error("Malformed JWT token: {}", ex.getMessage());
=======
                .setSubject(userPrincipal.getUsername()) // Typically email
                .setIssuedAt(now) // Use the 'now' variable
                .setExpiration(expiryDate)
                .signWith(jwtSecretKey, SignatureAlgorithm.HS512) // Ensure algorithm matches key type
                .compact();
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecretKey)
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token: {}", ex.getMessage());
>>>>>>> upstream/main
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
<<<<<<< HEAD
            logger.error("JWT claims string is empty: {}", ex.getMessage());
=======
            // This can happen if the token string is null or empty, or if claims are malformed.
            logger.error("JWT claims string is empty or argument is invalid: {}", ex.getMessage());
        } catch (io.jsonwebtoken.security.SecurityException ex) { // Catches signature errors
            logger.error("JWT signature validation failed: {}", ex.getMessage());
        } catch (Exception e) { // Catch any other unexpected JWT parsing errors
            logger.error("Could not validate JWT token for an unexpected reason: {}", e.getMessage());
>>>>>>> upstream/main
        }
        return false;
    }
}
