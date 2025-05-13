package com.eventeasyv1.dto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AvisDto {
    private Long id;
    private String commentaire;
    private int note;
    private LocalDateTime dateAvis;

    // Informations sur le client qui a laissé l'avis
    private Long clientId;
    private String clientNomComplet; // Ex: "Prenom Nom"

    // Informations sur le service concerné
    private Long serviceId;
    private String serviceNom;
}