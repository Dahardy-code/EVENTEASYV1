package com.eventeasyv1.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationDto {
    private Long id;
    private String message;
    private LocalDateTime dateEnvoi;
    private Long utilisateurId;
    private boolean lue; // Ajouter un champ 'lu' si vous voulez suivre les notifications lues
}