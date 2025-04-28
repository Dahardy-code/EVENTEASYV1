// src/main/java/com/eventeasyv1/dao/AdminRepository.java
// Or move to com.eventeasyv1.repository if you prefer

package com.eventeasyv1.dao; // Or com.eventeasyv1.repository

import com.eventeasyv1.entities.Administrateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository<Administrateur> extends JpaRepository<Administrateur, Long> {

    // JpaRepository provides standard CRUD methods:
    // - save(Administrateur admin)
    // - findById(Long id)
    // - findAll()
    // - deleteById(Long id)
    // - count()
    // - etc.

    // You can add custom queries specific to Administrators if needed:
    // Example: Find an admin by their email (inherited from Utilisateur)
    Optional<Administrateur> findByEmail(String email);

    long countByActive(boolean b);

    // Example: Find admins by privilege level (if privileges string has structure)
    // List<Administrateur> findByPrivilegesContaining(String privilegeKeyword);

}