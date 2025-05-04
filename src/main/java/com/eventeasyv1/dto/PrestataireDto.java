package com.eventeasyv1.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate; // Ou LocalDateTime si vous stockez l'heure

@Getter
@Setter
public class PrestataireDto {

    // Champs hérités de UtilisateurDto (ou Utilisateur)
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String role; // = "PRESTATAIRE"

    // Champs spécifiques à Prestataire
    private String nomEntreprise;
    private String categorieService; // Assurez-vous que le type correspond à votre entité
    private String adresse;
    private String numeroTel;
    private String description;
    private String siteWeb;
    private LocalDate dateInscription; // Assurez-vous que le type correspond
    private boolean estVerifie;
    // Ajoutez d'autres champs si nécessaire (ex: photo de profil URL, etc.)

    // Lombok gère les getters/setters
}