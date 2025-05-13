package com.eventeasyv1.dao;

import com.eventeasyv1.entities.Promo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromoRepository extends JpaRepository<Promo, Long> {

    // --- MÉTHODE À AJOUTER ---
    Optional<Promo> findByCodePromo(String codePromo);
    // --- FIN MÉTHODE À AJOUTER ---

    Optional<Promo> findByCodePromoAndEstActiveTrueAndDateFinAfter(String codePromo, LocalDate currentDate);
    List<Promo> findByEstActiveTrueAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(LocalDate dateDebut, LocalDate dateFin);
    List<Promo> findByEstActiveTrueAndDateFinBefore(LocalDate currentDate);
}