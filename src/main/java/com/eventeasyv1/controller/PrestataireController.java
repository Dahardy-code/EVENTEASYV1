package com.eventeasyv1.controller;

import com.eventeasyv1.dto.ServiceDto;
import com.eventeasyv1.dto.input.ServiceCreateUpdateDto;
import com.eventeasyv1.service.PrestataireService; // Assurez-vous que le chemin est correct
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Pour la sécurité au niveau méthode
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*; // Import pour les annotations Spring Web


import java.util.List;

@RestController
@RequestMapping("/api/prestataires") // Base path for prestataire related endpoints
public class PrestataireController {

    @Autowired
    private PrestataireService prestataireService;

    // Endpoint pour récupérer les infos du prestataire connecté (Étape 5 - déjà faite par B1 ?)
    // @GetMapping("/me")
    // @PreAuthorize("hasRole('PRESTATAIRE')")
    // public ResponseEntity<PrestataireDto> getCurrentPrestataireInfo() { ... }


    // == Endpoints pour la Gestion des Services du Prestataire Connecté ==

    @PostMapping("/me/services")
    @PreAuthorize("hasRole('PRESTATAIRE')")
    public ResponseEntity<ServiceDto> createService(@Valid @RequestBody ServiceCreateUpdateDto serviceDto) {
        String email = getCurrentUsername();
        ServiceDto createdService = prestataireService.addService(serviceDto, email);
        return new ResponseEntity<>(createdService, HttpStatus.CREATED);
    }

    @GetMapping("/me/services")
    @PreAuthorize("hasRole('PRESTATAIRE')")
    public ResponseEntity<List<ServiceDto>> getMyServices() {
        String email = getCurrentUsername();
        List<ServiceDto> services = prestataireService.getMyServices(email);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/me/services/{id}")
    @PreAuthorize("hasRole('PRESTATAIRE')")
    public ResponseEntity<ServiceDto> getMyServiceById(@PathVariable Long id) {
        String email = getCurrentUsername();
        ServiceDto serviceDto = prestataireService.getMyServiceById(id, email);
        return ResponseEntity.ok(serviceDto);
    }


    @PutMapping("/me/services/{id}")
    @PreAuthorize("hasRole('PRESTATAIRE')")
    public ResponseEntity<ServiceDto> updateService(@PathVariable Long id, @Valid @RequestBody ServiceCreateUpdateDto serviceDto) {
        String email = getCurrentUsername();
        ServiceDto updatedService = prestataireService.updateService(id, serviceDto, email);
        return ResponseEntity.ok(updatedService);
    }

    @DeleteMapping("/me/services/{id}")
    @PreAuthorize("hasRole('PRESTATAIRE')")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        String email = getCurrentUsername();
        prestataireService.deleteService(id, email);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }


    // Helper method to get the username (email) of the logged-in user
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            // This should ideally not happen if the endpoint is secured
            throw new IllegalStateException("User not authenticated");
        }
        return authentication.getName(); // Spring Security typically stores the username (email in our case) here
    }

    // Ajoutez ici les endpoints pour les disponibilités quand vous ferez l'Étape 7
    // ex: POST /me/disponibilites, GET /me/disponibilites, etc.

}