package com.eventeasyv1.dao;
import com.eventeasyv1.entities.Administrateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Administrateur, Long> {
    Optional<Administrateur> findByEmail(String email);
    // Ajoutez d'autres méthodes spécifiques si nécessaire
}