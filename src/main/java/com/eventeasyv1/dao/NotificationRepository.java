package com.eventeasyv1.dao;

import com.eventeasyv1.entities.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Trouver les notifications d'un utilisateur, paginées, les plus récentes en premier
    Page<Notification> findByUtilisateurIdOrderByDateEnvoiDesc(Long utilisateurId, Pageable pageable);

    // Compter les notifications non lues d'un utilisateur
    long countByUtilisateurIdAndEstLueFalse(Long utilisateurId);

    // Marquer toutes les notifications d'un utilisateur comme lues
    @Modifying // Nécessaire pour les requêtes UPDATE ou DELETE
    @Query("UPDATE Notification n SET n.estLue = true WHERE n.utilisateur.id = :utilisateurId AND n.estLue = false")
    int markAllAsReadForUser(@Param("utilisateurId") Long utilisateurId);
}
