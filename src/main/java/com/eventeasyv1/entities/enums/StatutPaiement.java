package com.eventeasyv1.entities.enums;

public enum StatutPaiement {
    EN_ATTENTE,    // Paiement initié mais pas encore confirmé
    REUSSI,        // Paiement réussi
    ECHOUE,        // Paiement échoué
    REMBOURSE      // Paiement remboursé
}