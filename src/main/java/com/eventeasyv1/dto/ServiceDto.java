package com.eventeasyv1.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceDto {

    private Long id;
    private String nom;
    private String description;
    private double prix;
    private Long prestataireId; // ID of the owner
    private String prestataireNom; // Name of the owner (optional but helpful)

    // Constructeur, Getters, Setters sont gérés par Lombok si vous l'ajoutez
    // ou à ajouter manuellement si vous n'utilisez pas Lombok ici
}