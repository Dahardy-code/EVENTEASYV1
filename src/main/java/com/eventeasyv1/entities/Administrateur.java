// src/main/java/com/eventeasyv1/entities/Utilisateur.java
package com.eventeasyv1.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "utilisateur")
// Strategy JOINED means each subclass has its own table with a FK to this base table's ID
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Administrateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom", length = 100)
    private String nom;

    @Column(name = "prenom", length = 100)
    private String prenom;

    @Column(name = "email", length = 100, unique = true, nullable = false) // Assuming email is mandatory
    private String email;

    @Column(name = "mot_de_passe", length = 255, nullable = false) // Assuming password is mandatory
    private String motDePasse; // Store hashed password

    @Column(name = "role", length = 50)
    private String role; // e.g., "CLIENT", "PRESTATAIRE", "ADMINISTRATEUR"

    // Note: Lombok's @Data provides basic equals/hashCode.
    // For JPA entities, especially with inheritance, overriding based on ID is often safer.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Administrateur that = (Administrateur) o;
        // Use ID for equality check if available, otherwise rely on default object equality
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        // Use ID for hash code if available, otherwise use a constant or default hashcode
        return id != null ? id.hashCode() : getClass().hashCode();
    }
}