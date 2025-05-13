package com.eventeasyv1.dto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
// import java.util.List; // Décommentez si vous ajoutez la liste d'InvitationDto

@Getter
@Setter
public class EvenementDto {
    private Long id;
    private String nom;
    private String description;
    private LocalDate dateEvenement;
    private String lieu;
    private LocalDateTime dateCreationEnregistrement;
    private Long clientId;
    // private List<InvitationDto> invitations; // Pour afficher les invitations associées
}