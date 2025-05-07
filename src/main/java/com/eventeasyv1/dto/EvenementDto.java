package com.eventeasyv1.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class EvenementDto {
    private Long id;
    private String nom;
    private String description;
    private LocalDate dateEvenement;
    private String lieu;
    private Long clientId;
}