package com.eventeasyv1.dao;

import com.eventeasyv1.entities.Administrateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Administrateur, Long> {

    // Méthode pour trouver un administrateur par son email
    Administrateur findByEmail(String email);

    long countByActive(boolean b);
}
