// src/main/java/com/eventeasyv1/utils/JwtUtil.java
package com.eventeasyv1.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
// import io.jsonwebtoken.io.Decoders; // This import is present but not used in the provided code snippet. Remove if not needed elsewhere.
import org.slf4j.Logger; // Added for potential logging
import org.slf4j.LoggerFactory; // Added for potential logging
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant; // <<< --- ADDED IMPORT
import java.time.temporal.ChronoUnit; // <<< --- ADDED IMPORT
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class); // Optional: For logging errors

    // IMPORTANT: This generates a NEW key every time the app starts.
    // Tokens issued before a restart will become invalid.
    // For production, use a stable, securely stored key.
    // See notes below on how to use a configured key.
    private final SecretKey SECRET_KEY = Keys.secretKeyFor(Jwts.SIG.HS256);

    /*
    // --- Example: Using a configured Base64 encoded key (Recommended for stability) ---
    @Value("${jwt.secret}") // Add jwt.secret=your_base64_encoded_strong_secret in application.properties
    private String jwtSecretString;

    private SecretKey configuredSecretKey;

    @jakarta.annotation.PostConstruct // Or javax.annotation.PostConstruct if using older Jakarta EE specs
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(this.jwtSecretString);
        this.configuredSecretKey = Keys.hmacShaKeyFor(keyBytes);
        log.info("JWT Secret Key initialized.");
    }
    // Remember to replace SECRET_KEY with configuredSecretKey in verifyWith() and signWith() if using this approach.
    // --- End Example ---
    */


    @Value("${jwt.expiration.ms:3600000}") // 1 hour default (3,600,000 ms)
    private long jwtExpirationMs;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        // Use the dynamically generated key (replace with configuredSecretKey if using the example above)
        return Jwts.parser()
                .verifyWith(SECRET_KEY) // Use SECRET_KEY or configuredSecretKey
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (Exception e) {
            // Log the specific exception for better debugging if needed
            log.warn("Failed to check token expiration, considering expired: {}", e.getMessage());
            // If extraction fails (malformed token, signature mismatch, etc.), consider it invalid/expired.
            return true;
        }
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // You can add custom claims here if needed, e.g., roles:
        // List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        // claims.put("roles", roles);
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        Instant now = Instant.now();
        Date issuedAt = Date.from(now);
        Date expirationDate = Date.from(now.plusMillis(jwtExpirationMs)); // Use plusMillis directly

        log.debug("Creating token for subject: {}, IssuedAt: {}, ExpiresAt: {}", subject, issuedAt, expirationDate);

        // Use the dynamically generated key (replace with configuredSecretKey if using the example above)
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(issuedAt)
                .expiration(expirationDate)
                .signWith(SECRET_KEY) // Use SECRET_KEY or configuredSecretKey
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            // Check username match and if the token is expired
            boolean isValid = username.equals(userDetails.getUsername()) && !isTokenExpired(token);
            if (!isValid) {
                log.warn("Token validation failed for user {}. Username match: {}, Token expired: {}",
                        userDetails.getUsername(), username.equals(userDetails.getUsername()), isTokenExpired(token));
            }
            return isValid;
        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException e) {
            // Catch specific JWT exceptions (SignatureException, MalformedJwtException, ExpiredJwtException, etc.)
            log.error("JWT validation error for token [{}...]: {}", token.substring(0, Math.min(token.length(), 10)), e.getMessage());
            // Optionally log the full stack trace for detailed debugging: log.error("JWT validation error", e);
            return false;
        } catch (Exception e) {
            // Catch unexpected errors during validation
            log.error("Unexpected error during JWT validation: {}", e.getMessage(), e);
            return false;
        }
    }
}