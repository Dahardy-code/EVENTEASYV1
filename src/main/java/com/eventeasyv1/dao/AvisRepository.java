package com.eventeasyv1.dao;

import com.eventeasyv1.entities.Avis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvisRepository extends JpaRepository<Avis, Long> {
    List<Avis> findByServiceId(Long serviceId);
    List<Avis> findByClientId(Long clientId);
    List<Avis> findByServicePrestataireId(Long prestataireId);
}