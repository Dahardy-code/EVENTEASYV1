package com.eventeasyv1.dao;

import com.eventeasyv1.entities.Paiement;
import com.eventeasyv1.entities.enums.StatutPaiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {

    Optional<Paiement> findByReservationId(Long reservationId);
    Optional<Paiement> findByIdTransactionExterne(String idTransactionExterne);
    List<Paiement> findByReservationClientId(Long clientId);
    List<Paiement> findByReservationServicePrestataireId(Long prestataireId);
    List<Paiement> findByStatut(StatutPaiement statut);
}