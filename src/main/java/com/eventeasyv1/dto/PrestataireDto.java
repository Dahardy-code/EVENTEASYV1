package com.eventeasyv1.dto;

import lombok.Data;

// DTO (Data Transfer Object) for Prestataire information sent to the frontend
@Data
public class PrestataireDto {
    // Inherited fields from Utilisateur that we want to expose
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String role = "PRESTATAIRE"; // Explicitly set role for clarity if needed

    // Prestataire specific fields
    private String nomEntreprise;
    private String categorieService;
    private String adresse;
    private String numeroTel;

    // We do NOT include the password or sensitive internal data
}