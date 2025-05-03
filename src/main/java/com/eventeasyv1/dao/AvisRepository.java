package com.eventeasyv1.dao;

import com.eventeasyv1.entities.Avis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvisRepository extends JpaRepository<Avis, Long> {
    List<Avis> findByReservationId(Long reservationId);
}
