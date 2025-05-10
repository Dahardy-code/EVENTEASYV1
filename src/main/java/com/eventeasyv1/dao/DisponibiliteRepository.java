package com.eventeasyv1.dao;
import com.eventeasyv1.entities.Disponibilite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DisponibiliteRepository extends JpaRepository<Disponibilite, Long> {
    List<Disponibilite> findByPrestataireIdOrderByDateDispoAscHeureDebutAsc(Long prestataireId);
    List<Disponibilite> findByPrestataireIdAndDateDispo(Long prestataireId, LocalDate dateDispo);
    // Vous pourriez avoir besoin de requêtes plus complexes pour vérifier les chevauchements
}