package com.eventeasyv1.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ServiceDto {
    private Long id;
    private String titre;
    private String description;
    private BigDecimal prix;
    private String categorie;
    private Long prestataireId;
    private String prestataireNomEntreprise; // Pour affichage facile

    public void setNomEntreprisePrestataire(String nomEntreprise) {
        return;
    }
    // Ajouter d'autres infos si nécessaire (ex: note moyenne)
}