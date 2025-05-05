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
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Requête de connexion reçue pour l'email: {}", loginRequest.getEmail());
        try {
            AuthResponse authResponse = authService.loginUser(loginRequest);
            log.info("Connexion réussie pour: {}", loginRequest.getEmail());
            return ResponseEntity.ok(authResponse);
        } catch (BadCredentialsException e) {
            log.warn("Échec de la connexion pour {}: {}", loginRequest.getEmail(), e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou mot de passe incorrect.", e);
        } catch (Exception e) {
            log.error("Erreur inattendue lors de la connexion pour {}: {}", loginRequest.getEmail(), e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur interne lors de la connexion.", e);
        }
    }

    @PostMapping("/register/client")
    public ResponseEntity<?> registerClient(@Valid @RequestBody RegisterRequest registerRequest) {
        log.info("Requête d'inscription reçue pour un client: {}", registerRequest.getEmail());
        try {
            AuthResponse authResponse = authService.registerClient(registerRequest);
            log.info("Inscription client réussie pour: {}", registerRequest.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("email est déjà utilisé")) {
                log.warn("Échec de l'inscription client (email existant) pour {}: {}", registerRequest.getEmail(), e.getMessage());
                throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
            } else {
                log.error("Erreur Runtime lors de l'inscription client pour {}: {}", registerRequest.getEmail(), e.getMessage(), e);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur interne lors de l'inscription.", e);
            }
        } catch (Exception e) {
            log.error("Erreur Exception générale lors de l'inscription client pour {}: {}", registerRequest.getEmail(), e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur interne générale lors de l'inscription.", e);
        }
    }

    @PostMapping("/register/prestataire")
    public ResponseEntity<?> registerPrestataire(@Valid @RequestBody RegisterRequest registerRequest) {
        log.info("Requête d'inscription reçue pour un prestataire: {}", registerRequest.getEmail());
        try {
            AuthResponse authResponse = authService.registerPrestataire(registerRequest);
            log.info("Inscription prestataire réussie pour: {}", registerRequest.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("email est déjà utilisé")) {
                log.warn("Échec de l'inscription prestataire (email existant) pour {}: {}", registerRequest.getEmail(), e.getMessage());
                throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
            } else {
                log.error("Erreur Runtime lors de l'inscription prestataire pour {}: {}", registerRequest.getEmail(), e.getMessage(), e);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur interne lors de l'inscription.", e);
            }
        } catch (Exception e) {
            log.error("Erreur Exception générale lors de l'inscription prestataire pour {}: {}", registerRequest.getEmail(), e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur interne générale lors de l'inscription.", e);
        }
    }
}