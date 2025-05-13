package com.eventeasyv1.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal; // Si la valeur peut être décimale
import java.time.LocalDate; // Ou LocalDateTime si l'heure est pertinente

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "statistiques")
public class Statistique {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_statistique", nullable = false, length = 100)
    private String typeStatistique; // Ex: "NOMBRE_UTILISATEURS", "REVENU_MENSUEL", "SERVICES_PLUS_RESERVES"

    @Column(name = "valeur_statistique", nullable = false, length = 255) // Stocker comme String pour flexibilité
    private String valeur; // Peut être un nombre, un JSON de plusieurs valeurs, etc.

    // Optionnel: Si la valeur est toujours numérique, vous pourriez préférer :
    // @Column(name = "valeur_numerique", precision = 15, scale = 2)
    // private BigDecimal valeurNumerique;

    @Column(name = "periode_debut") // Début de la période pour laquelle la stat est calculée (optionnel)
    private LocalDate periodeDebut;

    @Column(name = "periode_fin") // Fin de la période (optionnel)
    private LocalDate periodeFin;

    @Column(name = "date_calcul", nullable = false) // Date à laquelle cette statistique a été calculée/enregistrée
    private LocalDate dateCalcul;

    @Lob // Pour des détails ou contexte supplémentaires
    private String details;
}