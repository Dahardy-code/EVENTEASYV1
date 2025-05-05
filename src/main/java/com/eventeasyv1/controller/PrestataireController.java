package com.eventeasyv1.controller;

import com.eventeasyv1.dto.PrestataireDto;        // Use PrestataireDto
import com.eventeasyv1.service.PrestataireService; // Use PrestataireService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException; // For cleaner error responses

@RestController
@RequestMapping("/api/prestataires") // Base path for prestataire related endpoints
// Consider CORS configuration in a central SecurityConfig class instead of per-controller
// @CrossOrigin(origins = "*")
public class PrestataireController {

    @Autowired
    private PrestataireService prestataireService;

    /**
     * Endpoint to get the information of the currently logged-in Prestataire.
     * Accessed via GET /api/prestataires/me
     *
     * @return ResponseEntity containing PrestataireDto or an error status.
     */
    @GetMapping("/me")
    public ResponseEntity<PrestataireDto> getCurrentPrestataireInfo() {
        try {
            // Logic to retrieve authenticated user is handled by the service via SecurityContextHolder
            PrestataireDto prestataireInfo = prestataireService.getCurrentPrestataireDetails();
            return ResponseEntity.ok(prestataireInfo);
        } catch (UsernameNotFoundException e) {
            // User associated with token not found or is not a prestataire
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (IllegalStateException e) {
            // No user authenticated
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage(), e);
        } catch (Exception e) {
            // Catch unexpected errors
            // Log the error server-side
            System.err.println("Unexpected error fetching prestataire info: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur interne du serveur.", e);
        }
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