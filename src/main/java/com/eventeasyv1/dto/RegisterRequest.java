package com.eventeasyv1.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

// Ce DTO est utilisé pour les deux types d'inscription pour l'instant
@Data
public class RegisterRequest {

    // Champs communs (Utilisateur)
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String password;

    // --- Champs spécifiques au Prestataire ---
    // Ces champs seront null/vides lors de l'inscription d'un Client
    private String nomEntreprise;
    private String categorieService;
    private String adresse;
    private String numeroTel;

}