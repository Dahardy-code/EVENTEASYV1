package com.eventeasyv1.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AvisDto {
    private Long id;
    private String commentaire;
    private Integer note;
    private LocalDateTime dateAvis;
    private Long clientId;
    private String clientNom;
    private Long serviceId;
    private String serviceTitre;
}