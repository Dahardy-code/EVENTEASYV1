package com.eventeasyv1.controller;

import com.eventeasyv1.dto.NotificationDto;
import com.eventeasyv1.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()") // Tout utilisateur authentifié peut voir ses notifications
    public ResponseEntity<Page<NotificationDto>> getMyNotifications(
            Authentication authentication,
            @PageableDefault(size = 10, sort = "dateEnvoi,desc") Pageable pageable) {
        Page<NotificationDto> notifications = notificationService.getMyNotifications(authentication, pageable);
        return ResponseEntity.ok(notifications);
    }

    @PatchMapping("/me/{notificationId}/read") // PATCH est sémantiquement correct pour une mise à jour partielle
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<NotificationDto> markNotificationAsRead(
            @PathVariable Long notificationId,
            Authentication authentication) {
        NotificationDto notification = notificationService.markAsRead(notificationId, authentication);
        return ResponseEntity.ok(notification);
    }

    @PostMapping("/me/mark-all-read")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Object>> markAllMyNotificationsAsRead(Authentication authentication) {
        int count = notificationService.markAllMyNotificationsAsRead(authentication);
        return ResponseEntity.ok(Map.of("message", count + " notifications marquées comme lues.", "count", count));
    }

    @GetMapping("/me/unread-count")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Long>> getMyUnreadNotificationCount(Authentication authentication) {
        long count = notificationService.countMyUnreadNotifications(authentication);
        return ResponseEntity.ok(Map.of("unreadCount", count));
    }
}