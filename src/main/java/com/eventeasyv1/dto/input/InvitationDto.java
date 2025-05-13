package com.eventeasyv1.dto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InvitationDto {
    private Long id;
    private String email;
    private String message;
    private LocalDateTime dateEnvoi;
    private Long evenementId;
    // private StatutInvitation statut; // Si vous avez un statut
}