package com.eventeasyv1.service;

import com.eventeasyv1.dao.PrestataireRepository; // Use PrestataireRepository
import com.eventeasyv1.dto.PrestataireDto;      // Use PrestataireDto
import com.eventeasyv1.entities.Prestataire;    // Use Prestataire entity
import com.eventeasyv1.entities.Utilisateur;    // May need Utilisateur if fetching from base repo
import com.eventeasyv1.dao.UtilisateurRepository; // Optional: If fetching from base repository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Good practice for service methods reading data

@Service
public class PrestataireService {

    @Autowired
    private PrestataireRepository prestataireRepository;

    // Optional: Inject UtilisateurRepository if needed for a more robust findByEmail approach
    // @Autowired
    // private UtilisateurRepository utilisateurRepository;

    /**
     * Retrieves the details of the currently authenticated Prestataire.
     *
     * @return PrestataireDto containing the details.
     * @throws UsernameNotFoundException if the authenticated user is not found or is not a Prestataire.
     */
    @Transactional(readOnly = true) // Mark as transactional and read-only
    public PrestataireDto getCurrentPrestataireDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Aucun utilisateur authentifié trouvé.");
        }
        String userEmail = authentication.getName(); // Gets email from authenticated principal

        // --- Option 1: Using specific PrestataireRepository (simpler if Security principal is Prestataire) ---
        Prestataire prestataire = prestataireRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Prestataire non trouvé avec l'email: " + userEmail));

        // --- Option 2: Using base UtilisateurRepository (more robust if principal is just Utilisateur) ---
        /*
        Utilisateur utilisateur = utilisateurRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email: " + userEmail));

        if (!(utilisateur instanceof Prestataire)) {
             throw new UsernameNotFoundException("L'utilisateur connecté n'est pas un Prestataire: " + userEmail);
        }
        Prestataire prestataire = (Prestataire) utilisateur;
        */
        // --- Choose Option 1 or 2 based on your UserDetailsService implementation ---


        return mapToDto(prestataire); // Convert entity to DTO
    }


    /**
     * Maps a Prestataire entity to a PrestataireDto.
     *
     * @param prestataire The Prestataire entity.
     * @return The corresponding PrestataireDto.
     */
    private PrestataireDto mapToDto(Prestataire prestataire) {
        PrestataireDto dto = new PrestataireDto();
        // Map inherited fields
        dto.setId(prestataire.getId());
        dto.setNom(prestataire.getNom());
        dto.setPrenom(prestataire.getPrenom());
        dto.setEmail(prestataire.getEmail());
        // Map specific fields
        dto.setNomEntreprise(prestataire.getNomEntreprise());
        dto.setCategorieService(prestataire.getCategorieService());
        dto.setAdresse(prestataire.getAdresse());
        dto.setNumeroTel(prestataire.getNumeroTel());
        // Add other fields if needed in the DTO
        return dto;
    }

    // --- Add other Prestataire-specific service methods ---
    // Example: Method to get services offered by the current prestataire
    /*
    @Transactional(readOnly = true)
    public List<ServiceDto> getMyServices() {
        PrestataireDto currentPrestataire = getCurrentPrestataireDetails(); // Reuse to get ID
        // Fetch services using prestataireRepository or a dedicated ServiceRepository
        // List<Service> services = serviceRepository.findByPrestataireId(currentPrestataire.getId());
        // return services.stream().map(this::mapServiceToDto).collect(Collectors.toList());
        return Collections.emptyList(); // Placeholder
    }
    */

    // Example: Method to update prestataire profile (requires PrestataireUpdateDto)
     /*
     @Transactional
     public PrestataireDto updateProfile(PrestataireUpdateDto updateDto) {
         PrestataireDto current = getCurrentPrestataireDetails();
         Prestataire prestataire = prestataireRepository.findById(current.getId())
              .orElseThrow(() -> new UsernameNotFoundException("Prestataire non trouvé pour mise à jour"));

         // Update fields from updateDto
         prestataire.setNomEntreprise(updateDto.getNomEntreprise());
         // ... other fields ...

         Prestataire saved = prestataireRepository.save(prestataire);
         return mapToDto(saved);
     }
     */

}