package com.eventeasyv1.controller;

import com.eventeasyv1.dto.PaiementDto;
import com.eventeasyv1.dto.StripeClientSecretDto;
import com.eventeasyv1.dto.input.PaymentIntentDto;
import com.eventeasyv1.service.PaiementService;
// import com.stripe.exception.StripeException; // Si vous propagez l'exception
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/paiements")
public class PaiementController {

    private static final Logger log = LoggerFactory.getLogger(PaiementController.class);

    @Autowired
    private PaiementService paiementService;

    // Endpoint pour créer une intention de paiement (ex: avec Stripe)
    @PostMapping("/create-payment-intent")
    @PreAuthorize("hasRole('CLIENT')") // Seul un client peut initier un paiement pour sa réservation
    public ResponseEntity<StripeClientSecretDto> createPaymentIntent(
            @Valid @RequestBody PaymentIntentDto paymentIntentDto,
            Authentication authentication) {
        log.info("Requête de création de PaymentIntent reçue pour la réservation ID: {}", paymentIntentDto.getReservationId());
        try {
            StripeClientSecretDto response = paiementService.createPaymentIntent(paymentIntentDto, authentication);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) { // Attraper l'exception RuntimeException de StripeException
            log.error("Erreur lors de la création du PaymentIntent: {}", e.getMessage(), e);
            // Renvoyer une erreur plus générique au client
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Endpoint pour récupérer les détails d'un paiement lié à une réservation
    @GetMapping("/reservation/{reservationId}")
    @PreAuthorize("isAuthenticated()") // Le service vérifiera si l'utilisateur est le client ou le prestataire
    public ResponseEntity<PaiementDto> getPaiementByReservationId(
            @PathVariable Long reservationId,
            Authentication authentication) {
        PaiementDto paiementDto = paiementService.getPaiementByReservationId(reservationId, authentication);
        return ResponseEntity.ok(paiementDto);
    }


    // --- WEBHOOK STRIPE (Plus Avancé) ---
    // Cet endpoint ne serait PAS sécurisé par Spring Security de la même manière,
    // car c'est Stripe qui l'appelle. Il nécessite une vérification de signature de webhook.
    /*
    @PostMapping("/stripe/webhook")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        log.info("Webhook Stripe reçu.");
        try {
            paiementService.handleStripeWebhook(payload, sigHeader);
            return ResponseEntity.ok("Webhook traité");
        } catch (Exception e) {
            log.error("Erreur lors du traitement du webhook Stripe: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    */
}