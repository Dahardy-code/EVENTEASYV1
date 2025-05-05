package com.eventeasyv1.config; // Verify this is the correct package

// Correct import for the interface
import com.eventeasyv1.utils.JwtUtil; // Verify this path is correct
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
// SignatureException might be deprecated in newer jjwt, consider SecurityException
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired; // Keep for constructor
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService; // Correctly import the interface
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    // Use final fields for constructor injection
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService; // Inject the interface

    // Constructor Injection (Preferred)
    @Autowired // Optional if only one constructor
    public JwtFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");
        final String bearerPrefix = "Bearer ";

        String username = null; // Extracted username (email)
        String jwt = null;      // Extracted JWT

        // 1. Extract JWT from header
        if (authorizationHeader != null && authorizationHeader.startsWith(bearerPrefix)) {
            jwt = authorizationHeader.substring(bearerPrefix.length());
            try {
                username = jwtUtil.extractUsername(jwt);
                log.trace("JWT Filter: Extracted username '{}' from token.", username);
            } catch (ExpiredJwtException e) {
                log.warn("JWT Filter: Token expired: {}", e.getMessage());
            } catch (UnsupportedJwtException e) {
                log.warn("JWT Filter: Token format unsupported: {}", e.getMessage());
            } catch (MalformedJwtException e) {
                log.warn("JWT Filter: Token malformed: {}", e.getMessage());
            } catch (SignatureException e) {
                log.warn("JWT Filter: Token signature validation failed: {}", e.getMessage());
            } catch (IllegalArgumentException e) {
                log.warn("JWT Filter: Token claims invalid: {}", e.getMessage());
            } catch (Exception e) {
                log.error("JWT Filter: Unexpected error extracting username from token", e);
            }
        } else {
            log.trace("JWT Filter: Authorization header missing or not Bearer for URI: {}", request.getRequestURI());
        }

        // 2. Validate token and set SecurityContext if username extracted and no current auth
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.debug("JWT Filter: Attempting validation for '{}'.", username);

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwt, userDetails)) {
                log.info("JWT Filter: Token validated for '{}'. Setting SecurityContext.", username);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            } else {
                log.warn("JWT Filter: Token validation failed for user '{}'.", username);
            }
        } else if (username != null) {
            log.trace("JWT Filter: SecurityContext already populated for '{}'.", username);
        }

        // 3. Continue filter chain
        filterChain.doFilter(request, response);
    }
}