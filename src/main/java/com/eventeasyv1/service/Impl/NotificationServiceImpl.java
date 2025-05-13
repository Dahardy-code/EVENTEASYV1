package com.eventeasyv1.service.Impl;

import com.eventeasyv1.dao.NotificationRepository;
import com.eventeasyv1.dao.UtilisateurRepository; // Pour récupérer l'utilisateur par email
import com.eventeasyv1.dto.NotificationDto;
import com.eventeasyv1.entities.Notification;
import com.eventeasyv1.entities.Utilisateur;
import com.eventeasyv1.exception.ResourceNotFoundException;
import com.eventeasyv1.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final NotificationRepository notificationRepository;
    private final UtilisateurRepository utilisateurRepository; // Pour récupérer l'utilisateur

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   UtilisateurRepository utilisateurRepository) {
        this.notificationRepository = notificationRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    @Transactional
    public void createNotification(Utilisateur utilisateurDestinataire, String message /*, String type, Long lienId */) {
        if (utilisateurDestinataire == null) {
            log.warn("Tentative de création de notification pour un utilisateur null.");
            return; // Ou lever une exception
        }
        Notification notification = new Notification();
        notification.setUtilisateur(utilisateurDestinataire);
        notification.setMessage(message);
        notification.setEstLue(false);
        // notification.setTypeNotification(type);
        // notification.setLienEntiteId(lienId);
        notificationRepository.save(notification);
        log.info("Notification créée pour l'utilisateur ID {}: {}", utilisateurDestinataire.getId(), message);
        // TODO: Envoyer une notification push/websocket si nécessaire
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationDto> getMyNotifications(Authentication authentication, Pageable pageable) {
        Utilisateur utilisateur = getUtilisateurFromAuth(authentication);
        Page<Notification> notifications = notificationRepository.findByUtilisateurIdOrderByDateEnvoiDesc(utilisateur.getId(), pageable);
        return notifications.map(this::mapToDto);
    }

    @Override
    @Transactional
    public NotificationDto markAsRead(Long notificationId, Authentication authentication) {
        Utilisateur utilisateur = getUtilisateurFromAuth(authentication);
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", notificationId));

        if (!notification.getUtilisateur().getId().equals(utilisateur.getId())) {
            throw new AccessDeniedException("Vous n'êtes pas autorisé à modifier cette notification.");
        }
        notification.setEstLue(true);
        Notification updatedNotification = notificationRepository.save(notification);
        log.info("Notification ID {} marquée comme lue pour l'utilisateur ID {}", notificationId, utilisateur.getId());
        return mapToDto(updatedNotification);
    }

    @Override
    @Transactional
    public int markAllMyNotificationsAsRead(Authentication authentication) {
        Utilisateur utilisateur = getUtilisateurFromAuth(authentication);
        int updatedCount = notificationRepository.markAllAsReadForUser(utilisateur.getId());
        log.info("{} notifications marquées comme lues pour l'utilisateur ID {}", updatedCount, utilisateur.getId());
        return updatedCount;
    }

    @Override
    @Transactional(readOnly = true)
    public long countMyUnreadNotifications(Authentication authentication) {
        Utilisateur utilisateur = getUtilisateurFromAuth(authentication);
        return notificationRepository.countByUtilisateurIdAndEstLueFalse(utilisateur.getId());
    }

    private Utilisateur getUtilisateurFromAuth(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new IllegalStateException("Aucun utilisateur authentifié valide.");
        }
        String userEmail = authentication.getName();
        return utilisateurRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "email", userEmail));
    }

    private NotificationDto mapToDto(Notification notification) {
        NotificationDto dto = new NotificationDto();
        dto.setId(notification.getId());
        dto.setMessage(notification.getMessage());
        dto.setDateEnvoi(notification.getDateEnvoi());
        dto.setEstLue(notification.isEstLue());
        if (notification.getUtilisateur() != null) {
            dto.setUtilisateurId(notification.getUtilisateur().getId());
        }
        // dto.setTypeNotification(notification.getTypeNotification());
        // Construire le lien si vous avez lienEntiteId et typeEntiteLiee
        return dto;
    }
}
