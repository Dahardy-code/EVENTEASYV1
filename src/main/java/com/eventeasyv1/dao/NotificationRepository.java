package com.eventeasyv1.dao;
import com.eventeasyv1.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUtilisateurIdOrderByDateEnvoiDesc(Long utilisateurId);
    // Ajouter une méthode pour trouver les notifications non lues
    // List<Notification> findByUtilisateurIdAndLueIsFalseOrderByDateEnvoiDesc(Long utilisateurId);
}