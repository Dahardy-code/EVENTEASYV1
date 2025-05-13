package com.eventeasyv1.service;

import com.eventeasyv1.dto.PaiementDto;
import com.eventeasyv1.dto.StripeClientSecretDto; // Import du DTO pour le client secret
import com.eventeasyv1.dto.input.PaymentIntentDto;
import org.springframework.security.core.Authentication;
// Importez les exceptions Stripe si vous les utilisez
// import com.stripe.exception.StripeException;

public interface PaiementService {

    // Pour Stripe: Créer une intention de paiement et retourner le client_secret
    StripeClientSecretDto createPaymentIntent(PaymentIntentDto paymentIntentDto, Authentication authentication) /* throws StripeException */;

    // Confirmer un paiement (appelé par un webhook Stripe ou après confirmation client)
    PaiementDto confirmStripePayment(String paymentIntentId, Long reservationId); // Ou en recevant l'objet Event de Stripe

    PaiementDto getPaiementByReservationId(Long reservationId, Authentication authentication);

    // Logique pour gérer les webhooks Stripe (plus avancé)
    // void handleStripeWebhook(String payload, String sigHeader);
}