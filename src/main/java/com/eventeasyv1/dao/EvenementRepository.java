package com.eventeasyv1.dao;
import com.eventeasyv1.entities.Evenement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvenementRepository extends JpaRepository<Evenement, Long> {
    List<Evenement> findByClientIdOrderByDateEvenementDesc(Long clientId);
}