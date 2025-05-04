package com.eventeasyv1.controller;

// Imports DTOs
import com.eventeasyv1.dto.DisponibiliteDto;
import com.eventeasyv1.dto.PrestataireDto; // <-- AJOUTÉ : Import manquant
import com.eventeasyv1.dto.ServiceDto;
import com.eventeasyv1.dto.input.DisponibiliteCreateDto;
import com.eventeasyv1.dto.input.ServiceCreateUpdateDto;

// Imports Service
import com.eventeasyv1.service.PrestataireService;

// Imports Spring & Validation & Security
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication; // <-- Utilisé dans toutes les méthodes
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestataires")
public class PrestataireController {

    @Autowired
    private PrestataireService prestataireService;

    // == Endpoint pour les Informations du Prestataire Connecté (Étape 5 - Finalisé) ==
    @GetMapping("/me")
    @PreAuthorize("hasRole('PRESTATAIRE')")
    public ResponseEntity<PrestataireDto> getCurrentPrestataireInfo(Authentication authentication) { // Injection Authentication
        String email = authentication.getName(); // Récupération propre de l'email
        PrestataireDto prestataireDto = prestataireService.getCurrentPrestataireDetails(email); // Appel de la méthode corrigée dans le service
        return ResponseEntity.ok(prestataireDto);
    }


    // == Endpoints pour la Gestion des Services du Prestataire Connecté (Étape 6 - Standardisé) ==

    @PostMapping("/me/services")
    @PreAuthorize("hasRole('PRESTATAIRE')")
    public ResponseEntity<ServiceDto> createService(@Valid @RequestBody ServiceCreateUpdateDto serviceDto, Authentication authentication) { // Injection Authentication
        String email = authentication.getName(); // Récupération propre de l'email
        ServiceDto createdService = prestataireService.addService(serviceDto, email);
        return new ResponseEntity<>(createdService, HttpStatus.CREATED);
    }

    @GetMapping("/me/services")
    @PreAuthorize("hasRole('PRESTATAIRE')")
    public ResponseEntity<List<ServiceDto>> getMyServices(Authentication authentication) { // Injection Authentication
        String email = authentication.getName(); // Récupération propre de l'email
        List<ServiceDto> services = prestataireService.getMyServices(email);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/me/services/{id}")
    @PreAuthorize("hasRole('PRESTATAIRE')")
    public ResponseEntity<ServiceDto> getMyServiceById(@PathVariable Long id, Authentication authentication) { // Injection Authentication
        String email = authentication.getName(); // Récupération propre de l'email
        ServiceDto serviceDto = prestataireService.getMyServiceById(id, email);
        return ResponseEntity.ok(serviceDto);
    }

    @PutMapping("/me/services/{id}")
    @PreAuthorize("hasRole('PRESTATAIRE')")
    public ResponseEntity<ServiceDto> updateService(@PathVariable Long id, @Valid @RequestBody ServiceCreateUpdateDto serviceDto, Authentication authentication) { // Injection Authentication
        String email = authentication.getName(); // Récupération propre de l'email
        ServiceDto updatedService = prestataireService.updateService(id, serviceDto, email);
        return ResponseEntity.ok(updatedService);
    }

    @DeleteMapping("/me/services/{id}")
    @PreAuthorize("hasRole('PRESTATAIRE')")
    public ResponseEntity<Void> deleteService(@PathVariable Long id, Authentication authentication) { // Injection Authentication
        String email = authentication.getName(); // Récupération propre de l'email
        prestataireService.deleteService(id, email);
        return ResponseEntity.noContent().build();
    }


    // == Endpoints pour la Gestion des Disponibilités du Prestataire Connecté (Étape 7 - OK) ==

    @PostMapping("/me/disponibilites")
    @PreAuthorize("hasRole('PRESTATAIRE')")
    public ResponseEntity<DisponibiliteDto> createDisponibilite(@Valid @RequestBody DisponibiliteCreateDto dispoDto, Authentication authentication) {
        String email = authentication.getName();
        DisponibiliteDto createdDisponibilite = prestataireService.addDisponibilite(dispoDto, email);
        return new ResponseEntity<>(createdDisponibilite, HttpStatus.CREATED);
    }

    @GetMapping("/me/disponibilites")
    @PreAuthorize("hasRole('PRESTATAIRE')")
    public ResponseEntity<List<DisponibiliteDto>> getMyDisponibilites(Authentication authentication) {
        String email = authentication.getName();
        List<DisponibiliteDto> disponibilites = prestataireService.getMyDisponibilites(email);
        return ResponseEntity.ok(disponibilites);
    }

    @DeleteMapping("/me/disponibilites/{id}")
    @PreAuthorize("hasRole('PRESTATAIRE')")
    public ResponseEntity<Void> deleteDisponibilite(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        prestataireService.deleteDisponibilite(id, email);
        return ResponseEntity.noContent().build();
    }

    // La méthode privée getCurrentUsername() n'est plus nécessaire et a été supprimée
    // car nous utilisons l'injection directe de Authentication.
}