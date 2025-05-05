package com.eventeasyv1.controller; // Ou com.eventeasyv1.controller

import com.eventeasyv1.dto.AuthResponse;
import com.eventeasyv1.dto.LoginRequest;
import com.eventeasyv1.dto.RegisterRequest;
import com.eventeasyv1.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;


/**
 * Contrôleur REST pour gérer les points d'accès liés à l'authentification et à l'enregistrement.
 */
@RestController
@RequestMapping("/api/auth") // Préfixe commun pour toutes les routes de ce contrôleur
// @CrossOrigin(origins = "*", maxAge = 3600) // Alternative simple à la config CORS, mais moins sécurisée/flexible
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    // Injection par constructeur
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Point d'accès pour la connexion des utilisateurs.
     *
     * @param loginRequest DTO contenant l'email et le mot de passe.
     * @return ResponseEntity contenant AuthResponse (avec JWT) en cas de succès,
     *         ou une réponse d'erreur (401 ou 500) en cas d'échec.
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        log.info("Requête de connexion reçue pour l'email: {}", loginRequest.getEmail());
        try {
            // Appelle le service pour tenter l'authentification et obtenir la réponse
            AuthResponse authResponse = authService.loginUser(loginRequest);
            log.info("Connexion réussie pour: {}", loginRequest.getEmail());
            // Retourne 200 OK avec la réponse d'authentification (contenant le token)
            return ResponseEntity.ok(authResponse);
        } catch (BadCredentialsException e) {
            // Capturer spécifiquement l'échec d'authentification (mauvais identifiants)
            log.warn("Échec de la connexion pour {}: {}", loginRequest.getEmail(), e.getMessage());
            // Retourner 401 Unauthorized avec un message d'erreur clair
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Échec de la connexion. Vérifiez vos identifiants.");
        } catch (Exception e) {
            // Capturer toute autre exception inattendue pendant le processus
            log.error("Erreur inattendue lors de la connexion pour {}: {}", loginRequest.getEmail(), e.getMessage(), e);
            // Retourner 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur interne est survenue lors de la tentative de connexion.");
        }
    }

    /**
     * Point d'accès pour l'enregistrement d'un nouveau Client.
     *
     * @param registerRequest DTO contenant les informations d'inscription du client.
     * @return ResponseEntity contenant AuthResponse (avec JWT) en cas de succès,
     *         ou une réponse d'erreur (409 ou 500) en cas d'échec.
     */
    @PostMapping("/register/client")
    public ResponseEntity<?> registerClient(@RequestBody RegisterRequest registerRequest) {
        log.info("Requête d'inscription reçue pour un client: {}", registerRequest.getEmail());
        try {
            // Appelle le service pour enregistrer le client
            AuthResponse authResponse = authService.registerClient(registerRequest);
            log.info("Inscription client réussie pour: {}", registerRequest.getEmail());
            // Retourne 200 OK (ou 201 Created) avec la réponse d'authentification
            return ResponseEntity.ok(authResponse);
            // Alternativement, pour respecter les codes HTTP:
            // return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
        } catch (RuntimeException e) {
            // Capturer l'exception si l'email existe déjà (levée par AuthService)
            if (e.getMessage() != null && e.getMessage().contains("email est déjà utilisé")) {
                log.warn("Échec de l'inscription client pour {}: {}", registerRequest.getEmail(), e.getMessage());
                // Retourner 409 Conflict
                return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
            } else {
                // Capturer d'autres RuntimeException potentielles
                log.error("Erreur RuntimeException lors de l'inscription client pour {}: {}", registerRequest.getEmail(), e.getMessage(), e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Une erreur interne est survenue lors de l'inscription.");
            }
        } catch (Exception e) {
            // Capturer toute autre exception inattendue
            log.error("Erreur Exception générale lors de l'inscription client pour {}: {}", registerRequest.getEmail(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur interne générale est survenue lors de l'inscription.");
        }
    }


    /**
     * Point d'accès pour l'enregistrement d'un nouveau Prestataire.
     *
     * @param registerRequest DTO contenant les informations d'inscription du prestataire.
     *                        (Ce DTO pourrait devoir être différent ou plus riche que pour Client).
     * @return ResponseEntity contenant AuthResponse (avec JWT) en cas de succès,
     *         ou une réponse d'erreur (409 ou 500) en cas d'échec.
     */
    @PostMapping("/register/prestataire")
    public ResponseEntity<?> registerPrestataire(@RequestBody RegisterRequest registerRequest) {
        log.info("Requête d'inscription reçue pour un prestataire: {}", registerRequest.getEmail());
        try {
            // Appelle le service pour enregistrer le prestataire
            AuthResponse authResponse = authService.registerPrestataire(registerRequest);
            log.info("Inscription prestataire réussie pour: {}", registerRequest.getEmail());
            // Retourne 200 OK (ou 201 Created)
            return ResponseEntity.ok(authResponse);
            // return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
        } catch (RuntimeException e) {
            // Gestion des erreurs similaire à registerClient
            if (e.getMessage() != null && e.getMessage().contains("email est déjà utilisé")) {
                log.warn("Échec de l'inscription prestataire pour {}: {}", registerRequest.getEmail(), e.getMessage());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
            } else {
                log.error("Erreur RuntimeException lors de l'inscription prestataire pour {}: {}", registerRequest.getEmail(), e.getMessage(), e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Une erreur interne est survenue lors de l'inscription.");
            }
        } catch (Exception e) {
            log.error("Erreur Exception générale lors de l'inscription prestataire pour {}: {}", registerRequest.getEmail(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur interne générale est survenue lors de l'inscription.");
        }
    }

}