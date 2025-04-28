// src/main/java/com/eventeasyv1/controller/AuthController.java
package com.eventeasyv1.controller;

import com.eventeasyv1.dto.AuthResponse;
import com.eventeasyv1.dto.LoginRequest;
import com.eventeasyv1.dto.RegisterRequest;
import com.eventeasyv1.service.AuthService;
import jakarta.validation.Valid; // Correct import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // Use HttpStatus for responses
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register/client")
    public ResponseEntity<?> registerClient(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            AuthResponse authResponse = authService.registerClient(registerRequest);
            return ResponseEntity.ok(authResponse);
        } catch (RuntimeException e) {
            // Consider more specific exception handling
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage())); // Return error details
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse authResponse = authService.loginUser(loginRequest);
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) { // Catch specific exceptions like BadCredentialsException
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Identifiants invalides"));
        }
    }
}