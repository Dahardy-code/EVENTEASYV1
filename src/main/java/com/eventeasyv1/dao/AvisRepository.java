package com.eventeasyv1.dao;

import com.eventeasyv1.entities.Avis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AvisRepository extends JpaRepository<Avis, Long> {

    // Trouver tous les avis pour un service spécifique, triés par date (les plus récents en premier)
    Page<Avis> findByServiceIdOrderByDateAvisDesc(Long serviceId, Pageable pageable); // Avec pagination

    // Trouver tous les avis laissés par un client spécifique
    List<Avis> findByClientIdOrderByDateAvisDesc(Long clientId);

    // Vérifier si un client a déjà laissé un avis pour un service spécifique
    Optional<Avis> findByClientIdAndServiceId(Long clientId, Long serviceId);

    // Pour un prestataire, récupérer les avis sur ses services
    @Query("SELECT a FROM Avis a JOIN a.service s WHERE s.prestataire.id = :prestataireId ORDER BY a.dateAvis DESC")
    Page<Avis> findByServicePrestataireIdOrderByDateAvisDesc(@Param("prestataireId") Long prestataireId, Pageable pageable);
}