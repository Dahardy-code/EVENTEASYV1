package com.eventeasyv1.dao;

import com.eventeasyv1.entities.Disponibilite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime; // Changer LocalDate en LocalDateTime
import java.util.List;
import java.util.Optional; // Ajouté pour la méthode de l'étape précédente

@Repository
public interface DisponibiliteRepository extends JpaRepository<Disponibilite, Long> {
    // Corrigé pour utiliser dateDebut et heureDebut (supposant que heureDebut est un champ si vous voulez trier dessus)
    // Si vous n'avez pas heureDebut séparé, vous triez juste sur dateDebut
    List<Disponibilite> findByPrestataireIdOrderByDateDebutAsc(Long prestataireId); // Simplifié si pas de champ heureDebut

    // Corrigé pour chercher par dateDebut (ou une partie de celle-ci)
    // Note: Chercher une LocalDateTime exacte avec une LocalDate est délicat.
    // Il faudrait soit un intervalle, soit changer le type du paramètre ou du champ.
    // Supposons que vous vouliez les disponibilités qui commencent un certain jour :
    List<Disponibilite> findByPrestataireIdAndDateDebutBetween(Long prestataireId, LocalDateTime startOfDay, LocalDateTime endOfDay);

    // Cette méthode est correcte et utile pour la validation des chevauchements
    List<Disponibilite> findByPrestataireIdAndDateDebutLessThanAndDateFinGreaterThan(
            Long prestataireId, LocalDateTime newEnd, LocalDateTime newStart);

    // Ajouté lors d'une étape précédente, assurez-vous qu'il est toujours là si besoin
    Optional<Disponibilite> findByIdAndPrestataireId(Long id, Long prestataireId);
}