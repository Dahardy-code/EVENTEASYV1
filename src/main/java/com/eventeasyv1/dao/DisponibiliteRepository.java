package com.eventeasyv1.dao;

import com.eventeasyv1.entities.Disponibilite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DisponibiliteRepository extends JpaRepository<Disponibilite, Long> {

    // Find all availabilities for a specific provider, ordered by start date
    List<Disponibilite> findByPrestataireIdOrderByDateDebutAsc(Long prestataireId);

    // Find availabilities for a provider that overlap with a given time range
    // (useful for checking conflicts before adding a new one)
    // An overlap exists if: (ExistingStart < NewEnd) AND (ExistingEnd > NewStart)
    List<Disponibilite> findByPrestataireIdAndDateDebutLessThanAndDateFinGreaterThan(
            Long prestataireId, LocalDateTime newEnd, LocalDateTime newStart);

    // Find availability by ID and Prestataire ID (useful for ownership check before delete)
    Optional<Disponibilite> findByIdAndPrestataireId(Long id, Long prestataireId);

}