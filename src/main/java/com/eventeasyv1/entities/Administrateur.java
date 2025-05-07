package com.eventeasyv1.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "administrateur")
@PrimaryKeyJoinColumn(name = "id") // Lie à utilisateur.id
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Administrateur extends Utilisateur {

    @Column(name = "privileges")
    private String privileges; // Ou une structure plus complexe si nécessaire
}