package com.eventeasyv1.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Disponibilite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateDisponible;
    private boolean estDisponible;

    @ManyToOne
    @JoinColumn(name = "prestataire_id")
    private Prestataire prestataire;
}
