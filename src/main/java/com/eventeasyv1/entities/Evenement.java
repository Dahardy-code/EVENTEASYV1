package com.eventeasyv1.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "evenement")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Evenement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom", length = 150)
    private String nom;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "date_evenement")
    private LocalDate dateEvenement;

    @Column(name = "lieu")
    private String lieu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false) // Un événement est créé par un client
    @ToString.Exclude
    private Client client;

    // Relation inverse vers Invitation (si nécessaire de naviguer dans ce sens)
    @OneToMany(mappedBy = "evenement", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @ToString.Exclude
    private List<Invitation> invitations = new ArrayList<>();
}