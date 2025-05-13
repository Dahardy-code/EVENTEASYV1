package com.eventeasyv1.dao;

import com.eventeasyv1.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Pour un client, récupérer ses réservations
    List<Reservation> findByClientIdOrderByDateReservationDesc(Long clientId); // Trié par date de réservation la plus récente en premier

    // Pour un client, récupérer une réservation spécifique (vérification de propriété)
    Optional<Reservation> findByIdAndClientId(Long id, Long clientId);

    // Pour un prestataire, récupérer les réservations de ses services
    @Query("SELECT r FROM Reservation r JOIN r.service s WHERE s.prestataire.id = :prestataireId ORDER BY r.dateReservation DESC")
    List<Reservation> findByServicePrestataireIdOrderByDateReservationDesc(@Param("prestataireId") Long prestataireId);

    // Pour un prestataire, récupérer une réservation spécifique liée à ses services (vérification de propriété)
    @Query("SELECT r FROM Reservation r WHERE r.id = :reservationId AND r.service.prestataire.id = :prestataireId")
    Optional<Reservation> findByIdAndServicePrestataireId(@Param("reservationId") Long reservationId, @Param("prestataireId") Long prestataireId);
}