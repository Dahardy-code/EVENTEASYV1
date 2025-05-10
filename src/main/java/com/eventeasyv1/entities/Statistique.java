package com.eventeasyv1.entities;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "statistique")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Statistique {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_statistique", length = 100)
    private String typeStatistique;

    @Column(name = "valeur", precision = 10, scale = 2)
    private BigDecimal valeur;

    @Column(name = "date_statistique")
    private LocalDate dateStatistique;
}