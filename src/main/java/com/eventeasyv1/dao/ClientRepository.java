// src/main/java/com/eventeasyv1/dao/ClientRepository.java
package com.eventeasyv1.dao;

import com.eventeasyv1.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // Use Optional for findBy methods

// Removed incorrect import: import java.lang.ScopedValue;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    // Correct method signature for finding by email
    Optional<Client> findByEmail(String userEmail);
}