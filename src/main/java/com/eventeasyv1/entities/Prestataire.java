package com.eventeasyv1.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
// Assurez-vous que cet import est bien présent
import com.eventeasyv1.entities.Service;
// Importez d'autres entités liées si vous décommentez leurs relations
// import com.eventeasyv1.entities.Disponibilite;

@Entity
@Table(name = "prestataire")
@PrimaryKeyJoinColumn(name = "id")
@Data
// Exclure les collections des méthodes générées par Lombok pour éviter les problèmes
@EqualsAndHashCode(callSuper = true, exclude = {"services", "disponibilites"})
@NoArgsConstructor
@AllArgsConstructor
public class Prestataire extends Utilisateur {

    @Column(name = "nom_entreprise")
    private String nomEntreprise;

    @Column(name = "categorie_service")
    private String categorieService;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "numero_tel")
    private String numeroTel;

    // --- Relations ---
    @OneToMany(mappedBy = "prestataire", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @ToString.Exclude // Exclure de toString pour éviter les boucles
    private List<Service> services = new ArrayList<>();

    // @OneToMany(mappedBy = "prestataire", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    // @ToString.Exclude
    // private List<Disponibilite> disponibilites = new ArrayList<>();

    @Override
    public String toString() {
        return "Prestataire{" +
                "id=" + getId() +
                ", nomEntreprise='" + nomEntreprise + '\'' +
                ", categorieService='" + categorieService + '\'' +
                ", servicesCount=" + (services != null ? services.size() : 0) +
                // ", disponibilitesCount=" + (disponibilites != null ? disponibilites.size() : 0) +
                "} " + super.toString();
    }
}