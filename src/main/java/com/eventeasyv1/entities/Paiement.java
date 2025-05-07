package com.eventeasyv1.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "paiement")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "montant", precision = 10, scale = 2) // Correspond à DECIMAL(10,2)
    private BigDecimal montant;

    @Column(name = "date_paiement")
    private LocalDateTime datePaiement;

    @Column(name = "mode_paiement", length = 50)
    private String modePaiement;

    // TODO: Ajouter une référence de transaction externe (Stripe ID, etc.) si nécessaire
    // @Column(name = "reference_externe", unique = true)
    // private String referenceExterne;

    // Relation OneToOne (ou ManyToOne si une résa peut avoir plusieurs paiements partiels)
    // vers Reservation
    @OneToOne(fetch = FetchType.LAZY) // Ou @ManyToOne
    @JoinColumn(name = "reservation_id", unique = true) // unique = true pour OneToOne
    @ToString.Exclude
    private Reservation reservation;
}