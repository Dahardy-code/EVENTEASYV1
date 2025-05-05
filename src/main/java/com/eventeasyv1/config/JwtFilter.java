package com.eventeasyv1.config;

import com.eventeasyv1.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Autowired
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

        final String requestUri = request.getRequestURI(); // Obtenir l'URI pour les logs
        final String authorizationHeader = request.getHeader("Authorization");
        final String bearerPrefix = "Bearer ";

        String username = null;
        String jwt = null;

        // Log entrée filtre
        log.trace("JwtFilter processing request for: {}", requestUri);

        // 1. Extraire le JWT
        if (authorizationHeader != null && authorizationHeader.startsWith(bearerPrefix)) {
            jwt = authorizationHeader.substring(bearerPrefix.length());
            try {
                username = jwtUtil.extractUsername(jwt);
                log.trace("Extracted username '{}' from token for URI: {}", username, requestUri);
            } catch (ExpiredJwtException e) {
                log.warn("JWT expired for URI {}: {}", requestUri, e.getMessage());
            } catch (UnsupportedJwtException e) {
                log.warn("JWT format unsupported for URI {}: {}", requestUri, e.getMessage());
            } catch (MalformedJwtException e) {
                log.warn("JWT malformed for URI {}: {}", requestUri, e.getMessage());
            } catch (SignatureException e) {
                log.warn("JWT signature validation failed for URI {}: {}", requestUri, e.getMessage());
            } catch (IllegalArgumentException e) {
                log.warn("JWT claims invalid for URI {}: {}", requestUri, e.getMessage());
            } catch (Exception e) {
                log.error("Unexpected error extracting username from token for URI {}", requestUri, e);
            }
        } else {
            log.trace("No Authorization Bearer header found for URI: {}", requestUri);
        }

        // 2. Valider et mettre en place l'authentification SI username trouvé ET pas déjà authentifié
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.debug("Attempting token validation for user '{}', URI: {}", username, requestUri);
            try {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if (jwtUtil.validateToken(jwt, userDetails)) {
                    log.info("Token validated for user '{}'. Setting SecurityContext for URI: {}", username, requestUri);
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } else {
                    log.warn("Token validation failed for user '{}', URI: {}", username, requestUri);
                }
            } catch (UsernameNotFoundException e) {
                log.warn("User '{}' found in token but not found by UserDetailsService for URI: {}", username, requestUri);
                // Ne pas définir d'authentification si l'utilisateur n'est pas trouvé
            } catch (Exception e) {
                log.error("Error during UserDetailsService call or token validation for user '{}', URI: {}", username, requestUri, e);
            }
        } else if (username != null) {
            log.trace("SecurityContext already populated for user '{}', URI: {}", username, requestUri);
        } else {
            log.trace("No username extracted or SecurityContext already populated, proceeding without setting auth for URI: {}", requestUri);
        }

        // 3. Continuer la chaîne de filtres
        log.trace("Proceeding with filter chain for URI: {}", requestUri);
        filterChain.doFilter(request, response);
    }
}