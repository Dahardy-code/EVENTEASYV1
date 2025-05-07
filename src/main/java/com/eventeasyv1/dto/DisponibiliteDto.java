package com.eventeasyv1.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class DisponibiliteDto {
    private Long id;
    private LocalDate dateDispo;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private Long prestataireId; // Pour référence si nécessaire

    public DisponibiliteDto(Long id, LocalDate dateDispo, LocalTime heureDebut, LocalTime heureFin) {
        this.id = id;
        this.dateDispo = dateDispo;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
    }

    public void setDateDebut(LocalDate dateDispo) {
        this.dateDispo = dateDispo;
    }

    public void setDateFin(LocalDate dateDispo) {
        this.dateDispo = dateDispo;
    }
}