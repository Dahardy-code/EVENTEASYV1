package com.eventeasyv1.dao;

import com.eventeasyv1.entities.Prestataire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrestataireRepository extends JpaRepository<Prestataire, Long> {

    /**
     * Finds a Prestataire by their email address.
     * Assumes email is unique across all Utilisateur types.
     *
     * @param email The email address to search for.
     * @return An Optional containing the Prestataire if found, otherwise empty.
     */
    // This might return any Utilisateur if email is unique constraint is on Utilisateur table.
    // The service layer should handle casting or checking the role.
    // Or, define this in a base UtilisateurRepository. Keeping it here for consistency with Client example.
    Optional<Prestataire> findByEmail(String email);

    // Add other specific query methods for Prestataire if needed later
    // Example: List<Prestataire> findByCategorieService(String categorie);

}