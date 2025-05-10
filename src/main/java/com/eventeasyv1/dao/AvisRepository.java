package com.eventeasyv1.dao;
import com.eventeasyv1.entities.Avis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvisRepository extends JpaRepository<Avis, Long> {
    List<Avis> findByServiceIdOrderByDateAvisDesc(Long serviceId);
    List<Avis> findByClientIdOrderByDateAvisDesc(Long clientId);
    // Pour un prestataire, pour voir les avis sur tous ses services
    List<Avis> findByServicePrestataireIdOrderByDateAvisDesc(Long prestataireId);
}