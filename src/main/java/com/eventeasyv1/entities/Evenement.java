package com.eventeasyv1.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList; // Pour initialiser la liste
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "evenements")
public class Evenement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nom;

    @Lob // Pour des descriptions potentiellement longues
    private String description;

    @Column(name = "date_evenement", nullable = false)
    private LocalDate dateEvenement; // Uniquement la date, pas l'heure pour l'instant

    @Column(length = 255)
    private String lieu;

    @CreationTimestamp
    @Column(name = "date_creation_enregistrement", nullable = false, updatable = false)
    private LocalDateTime dateCreationEnregistrement; // Date de création de l'enregistrement en BDD

    // Relation : Un événement appartient à un Client
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    // Relation : Un événement peut avoir plusieurs invitations
    @OneToMany(mappedBy = "evenement", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Invitation> invitations = new ArrayList<>(); // Initialiser pour éviter NullPointerException
}