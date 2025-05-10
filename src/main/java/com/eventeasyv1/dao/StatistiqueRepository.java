package com.eventeasyv1.dao;
import com.eventeasyv1.entities.Statistique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Ce repository pourrait ne pas être beaucoup utilisé si les stats sont calculées
// à la volée ou via des requêtes agrégées.
@Repository
public interface StatistiqueRepository extends JpaRepository<Statistique, Long> {
    // Méthodes spécifiques si vous stockez des stats pré-calculées
}