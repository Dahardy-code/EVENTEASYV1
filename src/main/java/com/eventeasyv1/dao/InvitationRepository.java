package com.eventeasyv1.dao;

import com.eventeasyv1.entities.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    List<Invitation> findByEvenementIdOrderByDateEnvoiDesc(Long evenementId);
    List<Invitation> findByEmail(String email);
    // Trouver une invitation spécifique par son ID et l'ID de l'événement (pour vérification)
    Optional<Invitation> findByIdAndEvenementId(Long id, Long evenementId);
}