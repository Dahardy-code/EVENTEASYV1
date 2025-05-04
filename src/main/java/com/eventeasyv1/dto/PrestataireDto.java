package com.eventeasyv1.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate; // Assurez-vous que c'est le bon type utilisé dans votre Entité

@Getter
@Setter
public class PrestataireDto {

    // Champs hérités de Utilisateur
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String role = "PRESTATAIRE"; // Rôle fixe pour ce DTO

    // Champs spécifiques à Prestataire (vérifiez les noms et types dans Prestataire.java)
    private String nomEntreprise;
    private String categorieService;
    private String adresse;
    private String numeroTel;
    private String description;
    private String siteWeb;
    private LocalDate dateInscription;
    private boolean estVerifie; // Attention au nom du getter généré par Lombok: isEstVerifie()

    // Ajoutez ici d'autres champs si votre entité Prestataire en a plus que vous voulez exposer
    // Exemple: private String urlPhotoProfil;
}