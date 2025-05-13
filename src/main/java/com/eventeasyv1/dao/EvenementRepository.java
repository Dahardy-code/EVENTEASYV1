package com.eventeasyv1.dao;

import com.eventeasyv1.entities.Evenement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvenementRepository extends JpaRepository<Evenement, Long> {

    // Trouver tous les événements d'un client spécifique, triés par date d'événement
    List<Evenement> findByClientIdOrderByDateEvenementDesc(Long clientId);

    // Trouver un événement spécifique par son ID et l'ID du client (pour vérification de propriété)
    Optional<Evenement> findByIdAndClientId(Long id, Long clientId);
}