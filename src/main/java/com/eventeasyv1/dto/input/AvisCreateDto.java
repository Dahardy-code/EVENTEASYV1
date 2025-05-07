package com.eventeasyv1.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AvisCreateDto {
    @NotBlank(message = "Commentaire est requis")
    private String commentaire;

    @NotNull(message = "Note est requise")
    private Integer note;

    // Getters and Setters
    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Integer getNote() {
        return note;
    }

    public void setNote(Integer note) {
        this.note = note;
    }
}