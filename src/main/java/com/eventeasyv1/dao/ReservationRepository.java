package com.eventeasyv1.dao;

import com.eventeasyv1.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByClientIdOrderByDateReservationDesc(Long clientId);
    List<Reservation> findByServiceId(Long serviceId);
    // Trouver les réservations pour les services d'un prestataire
    List<Reservation> findByServicePrestataireIdOrderByDateReservationDesc(Long prestataireId);
}