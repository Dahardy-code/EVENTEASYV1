package com.eventeasyv1.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrestataireStatsDto {
    private Long totalReservations;
    private Long completedReservations; // Ou completedEvents si pertinent
    private Long pendingReservations;
    private Double averageRating; // Note moyenne sur ses services
    private BigDecimal totalRevenue; // Revenu généré (si géré)
    // Ajoutez d'autres statistiques pertinentes pour le prestataire
}