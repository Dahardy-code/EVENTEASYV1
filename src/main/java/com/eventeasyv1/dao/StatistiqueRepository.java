package com.eventeasyv1.dao;

import com.eventeasyv1.entities.Statistique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StatistiqueRepository extends JpaRepository<Statistique, Long> {
    List<Statistique> findByTypeStatistique(String type);
    List<Statistique> findByDateStatistiqueBetween(LocalDate start, LocalDate end);
}