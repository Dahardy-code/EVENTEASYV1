package com.eventeasyv1.dao;
import com.eventeasyv1.entities.Promo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromoRepository extends JpaRepository<Promo, Long> {
    Optional<Promo> findByCodePromo(String codePromo);
    List<Promo> findByDateFinAfterAndDateDebutBefore(LocalDate currentDate, LocalDate alsoCurrentDate);
}