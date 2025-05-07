package com.eventeasyv1.dao;

import com.eventeasyv1.entities.Disponibilite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DisponibiliteRepository extends JpaRepository<Disponibilite, Long> {
    List<Disponibilite> findByPrestataireId(Long prestataireId);
    // Pour trier directement par date et heure
    List<Disponibilite> findByPrestataireIdOrderByDateDispoAscHeureDebutAsc(Long prestataireId);
    // Pour vérifier la disponibilité à une date précise
    List<Disponibilite> findByPrestataireIdAndDateDispo(Long prestataireId, LocalDate dateDispo);
}