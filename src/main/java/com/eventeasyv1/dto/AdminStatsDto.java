package com.eventeasyv1.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminStatsDto {
    private Long totalUsers;
    private Long totalClients;
    private Long totalPrestataires;
    private Long totalReservations;
    private Long totalServices;
    // Ajoutez d'autres statistiques pertinentes pour l'admin
    // private BigDecimal totalRevenue;
    // private String popularServices; // Ou une List<String> ou List<ServiceSimpleDto>
}