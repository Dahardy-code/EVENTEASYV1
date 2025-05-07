package com.eventeasyv1.dao;

import com.eventeasyv1.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    // Si vous avez besoin de méthodes spécifiques pour Client, ajoutez-les ici.
    // Par exemple, pour retrouver un client par email (même si déjà dans UtilisateurRepository) :
    // Optional<Client> findByEmail(String email);
}