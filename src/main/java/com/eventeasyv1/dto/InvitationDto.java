package com.eventeasyv1.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class InvitationDto {
    private Long id;
    private String email; // Email de l'invité
    private String message;
    private LocalDateTime dateEnvoi;
    private Long clientId; // Client qui a envoyé
    private Long evenementId;
    private String evenementNom; // Pour affichage
}