package com.eventeasyv1.controller;

import com.eventeasyv1.dto.PrestataireDto;
import com.eventeasyv1.dto.ServiceDto;
import com.eventeasyv1.dto.DisponibiliteDto;
import com.eventeasyv1.dto.input.ServiceCreateUpdateDto;
import com.eventeasyv1.dto.input.DisponibiliteCreateDto;
import com.eventeasyv1.service.PrestataireService;
import jakarta.validation.Valid; // Importer @Valid
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize; // Peut être utilisé ici
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/prestataires")
// Optionnel: @PreAuthorize("hasRole('PRESTATAIRE')") pour sécuriser toute la classe
// si toutes les méthodes requièrent ce rôle (sauf si une méthode doit être accessible autrement)
public class PrestataireController {

    @Autowired
    private PrestataireService prestataireService;

    /** GET /api/prestataires/me */
    @GetMapping("/me")
    @PreAuthorize("hasRole('PRESTATAIRE')") // Sécuriser spécifiquement la méthode
    public ResponseEntity<PrestataireDto> getCurrentPrestataireInfo() {
        try {
            PrestataireDto info = prestataireService.getCurrentPrestataireDetails();
            return ResponseEntity.ok(info);
        } catch (UsernameNotFoundException | IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur interne.", e);
        }
    }

    // --- Service Management ---
    /** GET /api/prestataires/me/services */
    @GetMapping("/me/services")
    @PreAuthorize("hasRole('PRESTATAIRE')")
    public ResponseEntity<List<ServiceDto>> getCurrentPrestataireServices() {
        try {
            List<ServiceDto> services = prestataireService.getMyServices();
            return ResponseEntity.ok(services);
        } catch (UsernameNotFoundException | IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur interne.", e);
        }
    }

    /** POST /api/prestataires/me/services */
    @PostMapping("/me/services")
    @PreAuthorize("hasRole('PRESTATAIRE')")
    public ResponseEntity<ServiceDto> addService(@Valid @RequestBody ServiceCreateUpdateDto serviceDto) {
        try {
            ServiceDto created = prestataireService.addService(serviceDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (UsernameNotFoundException | IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur ajout service.", e);
        }
    }

    /** PUT /api/prestataires/me/services/{serviceId} */
    @PutMapping("/me/services/{serviceId}")
    @PreAuthorize("hasRole('PRESTATAIRE')")
    public ResponseEntity<ServiceDto> updateService(@PathVariable Long serviceId, @Valid @RequestBody ServiceCreateUpdateDto serviceDto) {
        try {
            ServiceDto updated = prestataireService.updateService(serviceId, serviceDto);
            return ResponseEntity.ok(updated);
        } catch (UsernameNotFoundException | IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        } catch (AccessDeniedException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (RuntimeException e) { // ex: Service non trouvé
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur mise à jour.", e);
        }
    }

    /** DELETE /api/prestataires/me/services/{serviceId} */
    @DeleteMapping("/me/services/{serviceId}")
    @PreAuthorize("hasRole('PRESTATAIRE')")
    public ResponseEntity<Void> deleteService(@PathVariable Long serviceId) {
        try {
            prestataireService.deleteService(serviceId);
            return ResponseEntity.noContent().build();
        } catch (UsernameNotFoundException | IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        } catch (AccessDeniedException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (RuntimeException e) { // ex: Service non trouvé
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur interne.", e);
        }
    }

    // --- Availability Management ---
    /** GET /api/prestataires/me/disponibilites */
    @GetMapping("/me/disponibilites")
    @PreAuthorize("hasRole('PRESTATAIRE')")
    public ResponseEntity<List<Object>> getCurrentPrestataireDisponibilites() {
        try {
            List<Object> dispos = prestataireService.getMyDisponibilites();
            return ResponseEntity.ok(dispos);
        } catch (UsernameNotFoundException | IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur interne.", e);
        }
    }

    /** POST /api/prestataires/me/disponibilites */
    @PostMapping("/me/disponibilites")
    @PreAuthorize("hasRole('PRESTATAIRE')")
    public ResponseEntity<DisponibiliteDto> addDisponibilite(@Valid @RequestBody DisponibiliteCreateDto dispoDto) {
        try {
            DisponibiliteDto created = prestataireService.addDisponibilite(dispoDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (UsernameNotFoundException | IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur ajout disponibilité.", e);
        }
    }

    /** DELETE /api/prestataires/me/disponibilites/{dispoId} */
    @DeleteMapping("/me/disponibilites/{dispoId}")
    @PreAuthorize("hasRole('PRESTATAIRE')")
    public ResponseEntity<Void> deleteDisponibilite(@PathVariable Long dispoId) {
        try {
            prestataireService.deleteDisponibilite(dispoId);
            return ResponseEntity.noContent().build();
        } catch (UsernameNotFoundException | IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        } catch (AccessDeniedException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (RuntimeException e) { // ex: Dispo non trouvée
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur interne.", e);
        }
    }
}