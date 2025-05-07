package com.eventeasyv1.dao;

import com.eventeasyv1.entities.Administrateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Administrateur, Long> {
    // Hérite des méthodes CRUD de base.
    // Ajoutez des méthodes spécifiques si nécessaire, ex: trouver par email
    Optional<Administrateur> findByEmail(String email);

    // Exemple pour AdminController (si la table admin a un champ 'active')
    // long countByActive(boolean active);
}