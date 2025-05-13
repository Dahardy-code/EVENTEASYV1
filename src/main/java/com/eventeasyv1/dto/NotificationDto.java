package com.eventeasyv1.dto;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class NotificationDto {
    private Long id;
    private String message;
    private LocalDateTime dateEnvoi;
    private boolean estLue;
    private Long utilisateurId; // Juste l'ID, le client sait que c'est pour lui
    // private String typeNotification; // Si vous ajoutez ce champ à l'entité
    // private String lien; // Pour construire un lien cliquable côté frontend
}