package com.eventeasyv1.entities;

import jakarta.persistence.*; // Import JPA annotations
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString; // Import pour personnaliser toString

import java.time.LocalDateTime; // Utiliser LocalDateTime pour DATETIME

/**
 * Entité représentant une réservation d'un service par un client.
 */
@Entity // Indique que c'est une entité JPA
@Table(name = "reservation") // Lie cette classe à la table 'reservation'
@Data // Génère automatiquement getters, setters, equals, hashCode, toString (par Lombok)
@NoArgsConstructor // Génère un constructeur sans arguments (requis par JPA)
@AllArgsConstructor // Génère un constructeur avec tous les arguments (pratique)
public class Reservation {

    /**
     * Identifiant unique de la réservation, auto-incrémenté.
     */
    @Id // Marque ce champ comme clé primaire
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Indique que la DB gère l'auto-incrémentation
    private Long id;

    /**
     * Date et heure auxquelles la réservation a été effectuée.
     * Mappé à une colonne DATETIME dans la base de données.
     * Utilisation de LocalDateTime pour correspondre au type DATETIME de SQL.
     */
    @Column(name = "date_reservation") // Mappe au nom de colonne exact dans la DB
    private LocalDateTime dateReservation;

    /**
     * Statut actuel de la réservation (ex: "CONFIRMEE", "EN_ATTENTE", "ANNULEE").
     * La longueur correspond au VARCHAR(50) du schéma.
     * Pourrait être remplacé par un Enum pour plus de robustesse.
     */
    @Column(name = "statut", length = 50)
    private String statut;

    // --- Relations ---

    /**
     * Le Client qui a effectué cette réservation.
     * C'est le côté "plusieurs" de la relation Client (1) <-> Reservation (*).
     * FetchType.LAZY est utilisé pour la performance (on ne charge le Client que si nécessaire).
     * JoinColumn spécifie la clé étrangère dans *cette* table ('reservation').
     * nullable = false indique que chaque réservation DOIT avoir un client associé.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false) // Référence la colonne client_id
    @ToString.Exclude // Important pour éviter les boucles infinies dans toString() si Client a une List<Reservation>
    private Client client; // Le nom 'client' correspondra au 'mappedBy' dans l'entité Client

    /**
     * Le Service qui est réservé.
     * C'est le côté "plusieurs" de la relation Service (1) <-> Reservation (*).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false) // Référence la colonne service_id
    @ToString.Exclude // Évite les boucles infinies dans toString() si Service a une List<Reservation>
    private Service service; // Le nom 'service' correspondra au 'mappedBy' potentiel dans Service

    /* --- Relation Optionnelle vers Paiement (décommentez si nécessaire) ---
       Suppose qu'une réservation peut avoir au maximum un paiement associé.
       La relation est gérée par le champ 'reservation' dans l'entité Paiement.

    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @ToString.Exclude
    private Paiement paiement;
    */


    // Pas besoin d'écrire manuellement les getters/setters/etc. grâce à Lombok @Data.
    // Le toString généré par @Data peut être problématique avec les relations bidirectionnelles
    // pour éviter les StackOverflowError (déjà fait avec @ToString.Exclude et
    // @EqualsAndHashCode dans Client/Prestataire).
    // Vous pouvez fournir une implémentation toString personnalisée si le besoin s'en fait sentir.
}