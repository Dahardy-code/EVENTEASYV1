package com.eventeasyv1.dto;

import lombok.Data;
import java.util.Date;

@Data
public class ClientDto {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private Date dateInscription;
    // N'incluez PAS le mot de passe
}