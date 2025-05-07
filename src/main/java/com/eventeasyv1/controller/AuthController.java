package com.eventeasyv1.controller;

import com.eventeasyv1.dto.AuthResponse;
import com.eventeasyv1.dto.LoginRequest;
import com.eventeasyv1.dto.RegisterRequest;
import com.eventeasyv1.service.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) { this.authService = authService; }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Requête login: {}", loginRequest.getEmail());
        try {
            AuthResponse res = authService.loginUser(loginRequest);
            log.info("Login réussi: {}", loginRequest.getEmail());
            return ResponseEntity.ok(res);
        } catch (BadCredentialsException e) {
            log.warn("Échec login: {}", loginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou mot de passe incorrect.", e);
        } catch (Exception e) {
            log.error("Erreur login: {}", loginRequest.getEmail(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur interne.", e);
        }
    }

    @PostMapping("/register/client")
    public ResponseEntity<?> registerClient(@Valid @RequestBody RegisterRequest req) {
        log.info("Requête register client: {}", req.getEmail());
        try {
            AuthResponse res = authService.registerClient(req);
            log.info("Register client réussi: {}", req.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("email est déjà utilisé")) {
                log.warn("Échec register client (email): {}", req.getEmail());
                throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
            } else {
                log.error("Erreur runtime register client: {}", req.getEmail(), e);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur interne inscription.", e);
            }
        } catch (Exception e) {
            log.error("Erreur générale register client: {}", req.getEmail(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur interne générale.", e);
        }
    }

    @PostMapping("/register/prestataire")
    public ResponseEntity<?> registerPrestataire(@Valid @RequestBody RegisterRequest req) {
        log.info("Requête register prestataire: {}", req.getEmail());
        try {
            AuthResponse res = authService.registerPrestataire(req);
            log.info("Register prestataire réussi: {}", req.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("email est déjà utilisé")) {
                log.warn("Échec register prestataire (email): {}", req.getEmail());
                throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
            } else {
                log.error("Erreur runtime register prestataire: {}", req.getEmail(), e);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur interne inscription.", e);
            }
        } catch (Exception e) {
            log.error("Erreur générale register prestataire: {}", req.getEmail(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur interne générale.", e);
        }
    }
}