package com.eventeasyv1.controller;

import com.eventeasyv1.dto.PrestataireDto;
import com.eventeasyv1.dto.ReservationDto;
import com.eventeasyv1.service.PrestataireService;
// import org.apache.tomcat.util.net.openssl.ciphers.Authentication; // MAUVAIS IMPORT - À SUPPRIMER
import org.springframework.security.core.Authentication; // <-- BON IMPORT
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/prestataires")
public class PrestataireController {

    @Autowired
    private PrestataireService prestataireService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('PRESTATAIRE')") // Bonne pratique d'ajouter @PreAuthorize ici aussi
    public ResponseEntity<PrestataireDto> getCurrentPrestataireInfo(Authentication authentication) { // Utiliser l'injection d'Authentication
        try {
            // Le service devrait prendre Authentication ou l'email en paramètre
            // Si getCurrentPrestataireDetails() dans le service utilise SecurityContextHolder,
            // l'argument Authentication n'est pas strictement nécessaire ici, mais c'est plus propre de le passer.
            String email = authentication.getName(); // Récupérer l'email
            PrestataireDto prestataireInfo = prestataireService.getCurrentPrestataireDetails(email); // Passer l'email
            return ResponseEntity.ok(prestataireInfo);
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Unexpected error fetching prestataire info: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur interne du serveur.", e);
        }
    }

    @GetMapping("/me/reservations")
    @PreAuthorize("hasRole('PRESTATAIRE')")
    public ResponseEntity<List<ReservationDto>> getMyServiceReservations(Authentication authentication) { // Le type est maintenant correct
        String prestataireEmail = authentication.getName();
        List<ReservationDto> reservations = prestataireService.getMyServiceReservations(prestataireEmail);
        return ResponseEntity.ok(reservations);
    }

    // --- Add other Prestataire-specific endpoints here ---

    /**
     * Example: Endpoint to get services offered by the current prestataire
     * Accessed via GET /api/prestataires/me/services
     */
    /*
    @GetMapping("/me/services")
    public ResponseEntity<List<ServiceDto>> getCurrentPrestataireServices() {
        try {
            List<ServiceDto> services = prestataireService.getMyServices();
            return ResponseEntity.ok(services);
        } catch (UsernameNotFoundException e) {
             throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Error fetching prestataire services: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur interne.", e);
        }
    }
    */

    /**
     * Example: Endpoint to update prestataire profile
     * Accessed via PUT /api/prestataires/me/profile
     */
    /*
    @PutMapping("/me/profile")
    public ResponseEntity<PrestataireDto> updatePrestataireProfile(@RequestBody PrestataireUpdateDto updateDto) {
       try {
            PrestataireDto updatedPrestataire = prestataireService.updateProfile(updateDto);
            return ResponseEntity.ok(updatedPrestataire);
       } catch (UsernameNotFoundException e) {
             throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
       } catch (Exception e) { // Consider more specific exceptions for validation etc.
           System.err.println("Error updating prestataire profile: " + e.getMessage());
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur lors de la mise à jour.", e);
       }
    }
    */

}