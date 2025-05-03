package com.eventeasyv1.dao;

import com.eventeasyv1.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public class ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByClientId(Long clientId);
}
