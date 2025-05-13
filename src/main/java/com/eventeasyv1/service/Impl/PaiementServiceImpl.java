package com.eventeasyv1.service.Impl;

import com.eventeasyv1.dao.ClientRepository;
import com.eventeasyv1.dao.PaiementRepository;
import com.eventeasyv1.dao.ReservationRepository;
import com.eventeasyv1.dto.PaiementDto;
import com.eventeasyv1.dto.StripeClientSecretDto;
import com.eventeasyv1.dto.input.PaymentIntentDto;
import com.eventeasyv1.entities.Client;
import com.eventeasyv1.entities.Paiement;
import com.eventeasyv1.entities.Reservation;
import com.eventeasyv1.entities.enums.StatutPaiement;
import com.eventeasyv1.entities.enums.StatutReservation;
import com.eventeasyv1.exception.BadRequestException;
import com.eventeasyv1.exception.ResourceNotFoundException;
import com.eventeasyv1.service.PaiementService;
import com.stripe.Stripe; // Import Stripe
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PaiementServiceImpl implements PaiementService {

    private static final Logger log = LoggerFactory.getLogger(PaiementServiceImpl.class);

    @Value("${stripe.secret.key}") // À définir dans application.properties
    private String stripeSecretKey;

    @Autowired private PaiementRepository paiementRepository;
    @Autowired private ReservationRepository reservationRepository;
    @Autowired private ClientRepository clientRepository;
    @PostConstruct
    public void initStripe() {
        Stripe.apiKey = stripeSecretKey;
    }

    @Override
    @Transactional
    public StripeClientSecretDto createPaymentIntent(PaymentIntentDto paymentIntentDto, Authentication authentication) {
        // Valider que la réservation appartient au client connecté, etc.
        Reservation reservation = reservationRepository.findByIdAndClientId(
                paymentIntentDto.getReservationId(),
                // Ici, il faudrait récupérer l'ID du client depuis 'authentication'
                // Supposons que vous avez une méthode pour cela ou que vous le faites ici
                getClientIdFromAuth(authentication)
        ).orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", paymentIntentDto.getReservationId()));

        if (reservation.getStatut() == StatutReservation.CONFIRMEE || reservation.getStatut() == StatutReservation.TERMINEE) {
            throw new BadRequestException("La réservation est déjà confirmée ou terminée.");
        }

        // Montant en centimes pour Stripe
        long amountInCents = paymentIntentDto.getMontant().multiply(new BigDecimal("100")).longValue();

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amountInCents)
                .setCurrency(paymentIntentDto.getDevise().toLowerCase())
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .build()
                )
                // Optionnel: Ajouter des metadata
                .putMetadata("reservation_id", reservation.getId().toString())
                .putMetadata("client_id", reservation.getClient().getId().toString())
                .build();
        try {
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            log.info("PaymentIntent Stripe créé : {}", paymentIntent.getId());

            // Optionnel: Créer une entrée Paiement en base avec statut EN_ATTENTE
            // Paiement paiement = new Paiement();
            // paiement.setReservation(reservation);
            // paiement.setMontant(paymentIntentDto.getMontant());
            // paiement.setStatut(StatutPaiement.EN_ATTENTE);
            // paiement.setIdTransactionExterne(paymentIntent.getId());
            // paiement.setModePaiement("STRIPE");
            // paiementRepository.save(paiement);

            return new StripeClientSecretDto(paymentIntent.getClientSecret());
        } catch (StripeException e) {
            log.error("Erreur Stripe lors de la création du PaymentIntent: {}", e.getMessage());
            throw new RuntimeException("Erreur lors de l'initiation du paiement : " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public PaiementDto confirmStripePayment(String paymentIntentId, Long reservationId) {
        // Cette méthode serait typiquement appelée par un webhook Stripe
        // Ou après que le client ait complété le paiement côté frontend
        log.info("Tentative de confirmation de paiement pour PaymentIntent ID: {} et Reservation ID: {}", paymentIntentId, reservationId);
        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

            if ("succeeded".equals(paymentIntent.getStatus())) {
                Reservation reservation = reservationRepository.findById(reservationId)
                        .orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", reservationId));

                // Vérifier si un paiement existe déjà pour éviter les doublons
                Paiement paiement = paiementRepository.findByIdTransactionExterne(paymentIntentId)
                        .orElseGet(() -> {
                            Paiement newPaiement = new Paiement();
                            newPaiement.setReservation(reservation);
                            newPaiement.setMontant(BigDecimal.valueOf(paymentIntent.getAmountReceived()).divide(new BigDecimal("100")));
                            newPaiement.setModePaiement("STRIPE");
                            return newPaiement;
                        });

                paiement.setStatut(StatutPaiement.REUSSI);
                paiement.setIdTransactionExterne(paymentIntent.getId()); // S'assurer qu'il est bien là
                paiement.setDatePaiement(LocalDateTime.now()); // Ou récupérer la date de Stripe
                // paiement.setDetailsPaiement(paymentIntent.toJson()); // Stocker tous les détails si besoin

                Paiement savedPaiement = paiementRepository.save(paiement);

                // Mettre à jour le statut de la réservation
                reservation.setStatut(StatutReservation.CONFIRMEE);
                reservationRepository.save(reservation);

                log.info("Paiement ID {} confirmé avec succès pour Reservation ID {}. Statut de réservation mis à jour.", savedPaiement.getId(), reservationId);
                return mapToDto(savedPaiement);
            } else {
                log.warn("Statut du PaymentIntent Stripe non 'succeeded': {}", paymentIntent.getStatus());
                // Gérer les autres statuts (processing, requires_payment_method, etc.)
                // Peut-être mettre à jour un paiement existant en statut ECHOUE
                Paiement paiement = paiementRepository.findByIdTransactionExterne(paymentIntentId).orElse(null);
                if(paiement != null) {
                    paiement.setStatut(StatutPaiement.ECHOUE);
                    paiementRepository.save(paiement);
                }
                throw new BadRequestException("Le paiement n'a pas réussi. Statut: " + paymentIntent.getStatus());
            }
        } catch (StripeException e) {
            log.error("Erreur Stripe lors de la récupération/confirmation du PaymentIntent {}: {}", paymentIntentId, e.getMessage());
            throw new RuntimeException("Erreur lors de la confirmation du paiement : " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PaiementDto getPaiementByReservationId(Long reservationId, Authentication authentication) {
        // Logique pour vérifier que l'utilisateur a le droit de voir ce paiement
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", reservationId));

        String userEmail = authentication.getName();
        if (!reservation.getClient().getEmail().equals(userEmail) &&
                !reservation.getService().getPrestataire().getEmail().equals(userEmail)) {
            // Ajouter une vérification pour ROLE_ADMIN si nécessaire
            throw new AccessDeniedException("Accès non autorisé à ce paiement.");
        }

        Paiement paiement = paiementRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement", "reservationId", reservationId));
        return mapToDto(paiement);
    }

    // Helper pour récupérer l'ID client (à implémenter ou utiliser une méthode existante)
    private Long getClientIdFromAuth(Authentication authentication) {
        // Vous devez implémenter la logique pour obtenir l'ID du client
        // à partir de l'objet Authentication. Cela pourrait impliquer de
        // récupérer l'objet UserDetails, de caster en votre implémentation personnalisée
        // qui contient l'ID, ou de faire un appel à ClientRepository.
        // Exemple TRÈS simplifié (NE PAS UTILISER EN PRODUCTION TEL QUEL) :
        // Supposons que le "name" dans Authentication est l'email du client.
        String clientEmail = authentication.getName();
        Client client = clientRepository.findByEmail(clientEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "email", clientEmail));
        return client.getId();
    }

    private PaiementDto mapToDto(Paiement paiement) {
        PaiementDto dto = new PaiementDto();
        dto.setId(paiement.getId());
        dto.setMontant(paiement.getMontant());
        dto.setDatePaiement(paiement.getDatePaiement());
        dto.setModePaiement(paiement.getModePaiement());
        dto.setStatut(paiement.getStatut());
        dto.setIdTransactionExterne(paiement.getIdTransactionExterne());
        dto.setDateCreation(paiement.getDateCreation());
        if (paiement.getReservation() != null) {
            dto.setReservationId(paiement.getReservation().getId());
        }
        return dto;
    }
}
