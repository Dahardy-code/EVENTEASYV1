package com.eventeasyv1.service;

import com.eventeasyv1.dto.NotificationDto;
import com.eventeasyv1.entities.Utilisateur; // Pour passer l'utilisateur cible
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface NotificationService {

    // Créer une notification (appelé par d'autres services)
    void createNotification(Utilisateur utilisateurDestinataire, String message /*, String type, Long lienId */);

    // Récupérer les notifications pour l'utilisateur connecté
    Page<NotificationDto> getMyNotifications(Authentication authentication, Pageable pageable);

    // Marquer une notification spécifique comme lue
    NotificationDto markAsRead(Long notificationId, Authentication authentication);

    // Marquer toutes les notifications comme lues pour l'utilisateur connecté
    int markAllMyNotificationsAsRead(Authentication authentication);

    // Compter les notifications non lues pour l'utilisateur connecté
    long countMyUnreadNotifications(Authentication authentication);

    // Supprimer une notification (optionnel)
    // void deleteNotification(Long notificationId, Authentication authentication);
}
