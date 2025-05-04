package com.eventeasyv1.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DisponibiliteDto {
    private Long id;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private Long prestataireId; // Keep prestataire ID for reference
}
