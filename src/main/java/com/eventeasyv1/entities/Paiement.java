package com.eventeasyv1.entities;

import com.eventeasyv1.entities.enums.StatutPaiement;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "paiements")
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montant;

    @Column(name = "date_paiement") // Date effective du paiement (peut être différente de date_creation)
    private LocalDateTime datePaiement;

    @Column(name = "mode_paiement", length = 50)
    private String modePaiement; // Ex: "STRIPE", "PAYPAL", "CARTE_BANCAIRE_VIA_STRIPE"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private StatutPaiement statut;

    @Column(name = "id_transaction_externe", length = 255, unique = true) // ID de la transaction chez Stripe/Paypal
    private String idTransactionExterne;

    @Column(name = "details_paiement", columnDefinition = "TEXT") // Pour stocker des infos JSON supplémentaires du PSP
    private String detailsPaiement;

    @CreationTimestamp
    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @UpdateTimestamp
    @Column(name = "date_mise_a_jour", nullable = false)
    private LocalDateTime dateMiseAJour;

    // Un paiement est généralement lié à une et une seule réservation
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", referencedColumnName = "id", nullable = false, unique = true)
    private Reservation reservation;
}