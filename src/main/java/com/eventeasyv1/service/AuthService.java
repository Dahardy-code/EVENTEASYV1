package com.eventeasyv1.service;

import com.eventeasyv1.dao.ClientRepository;
import com.eventeasyv1.dao.PrestataireRepository; // Injection confirmée comme nécessaire
import com.eventeasyv1.dao.UtilisateurRepository;
import com.eventeasyv1.dto.AuthResponse;
import com.eventeasyv1.dto.LoginRequest;
import com.eventeasyv1.dto.RegisterRequest; // Ce DTO inclut maintenant les champs Prestataire
import com.eventeasyv1.entities.Client;
import com.eventeasyv1.entities.Prestataire;
import com.eventeasyv1.entities.Utilisateur;
import com.eventeasyv1.security.UserDetailsImpl; // Importé car utilisé dans loginUser
import com.eventeasyv1.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import pour @Transactional

import java.util.Date;

/**
 * Service central gérant l'authentification (connexion) et l'enregistrement
 * des différents types d'utilisateurs (Client, Prestataire).
 */
@Service
public class AuthService {

    // Logger pour le suivi et le débogage
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    // Déclaration finale des dépendances injectées via le constructeur
    private final AuthenticationManager authenticationManager;
    private final UtilisateurRepository utilisateurRepository;
    private final ClientRepository clientRepository;
    private final PrestataireRepository prestataireRepository; // Repository pour les prestataires
    private final PasswordEncoder passwordEncoder; // Pour hacher les mots de passe
    private final JwtUtil jwtUtil; // Pour générer les tokens JWT

    /**
     * Constructeur pour l'injection des dépendances (préféré à @Autowired sur les champs).
     *
     * @param authenticationManager Le gestionnaire d'authentification de Spring Security.
     * @param utilisateurRepository Repository pour les opérations générales sur les Utilisateurs.
     * @param clientRepository      Repository spécifique aux Clients.
     * @param prestataireRepository Repository spécifique aux Prestataires.
     * @param passwordEncoder       L'encodeur de mot de passe (BCrypt).
     * @param jwtUtil               L'utilitaire pour la gestion des JWT.
     */
    @Autowired
    public AuthService(AuthenticationManager authenticationManager,
                       UtilisateurRepository utilisateurRepository,
                       ClientRepository clientRepository,
                       PrestataireRepository prestataireRepository, // Injection ajoutée
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.utilisateurRepository = utilisateurRepository;
        this.clientRepository = clientRepository;
        this.prestataireRepository = prestataireRepository; // Affectation ajoutée
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Enregistre un nouveau Client dans le système.
     * Inclut la vérification de l'email, le hachage du mot de passe,
     * la sauvegarde, et une authentification optionnelle post-inscription.
     *
     * @param registerRequest DTO contenant les informations du client à enregistrer.
     * @return AuthResponse contenant le token JWT et les informations de base de l'utilisateur.
     * @throws RuntimeException si l'email fourni est déjà utilisé.
     */
    @Transactional // Garantit que toutes les opérations DB sont atomiques
    public AuthResponse registerClient(RegisterRequest registerRequest) {
        log.info("Tentative d'inscription d'un client avec l'email: {}", registerRequest.getEmail());

        // 1. Vérification de l'unicité de l'email
        if (utilisateurRepository.existsByEmail(registerRequest.getEmail())) {
            log.warn("Échec de l'inscription: Email {} déjà utilisé.", registerRequest.getEmail());
            throw new RuntimeException("Erreur: Cet email est déjà utilisé!");
        }

        // 2. Création de l'instance de l'entité Client
        Client client = new Client();

        // 3. Remplissage des champs hérités de Utilisateur
        client.setNom(registerRequest.getNom());
        client.setPrenom(registerRequest.getPrenom());
        client.setEmail(registerRequest.getEmail());

        // 4. Hachage et définition du mot de passe (TRÈS IMPORTANT)
        client.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        // 5. Remplissage des champs spécifiques à Client
        client.setDateInscription(new Date());
        // Note: Le rôle 'CLIENT' est défini par @DiscriminatorValue ou la stratégie JOINED, pas besoin de le setter ici.

        // 6. Sauvegarde de l'entité Client (cascade les opérations sur Utilisateur grâce à JOINED)
        Client savedClient = clientRepository.save(client);
        log.info("Client enregistré avec succès: ID={}, Email={}", savedClient.getId(), savedClient.getEmail());

        // 7. Authentification optionnelle post-inscription pour retourner un token immédiatement
        Authentication authentication = authenticateUser(registerRequest.getEmail(), registerRequest.getPassword());

        // 8. Mise à jour du contexte de sécurité
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 9. Récupération des détails pour la génération du token
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 10. Génération du token JWT
        String jwt = jwtUtil.generateToken(userDetails);
        log.info("Token JWT généré pour le nouveau client: {}", savedClient.getEmail());

        // 11. Construction et retour de la réponse
        return new AuthResponse(jwt, savedClient.getEmail(), "CLIENT", savedClient.getId());
    }


    /**
     * Enregistre un nouveau Prestataire dans le système.
     * Inclut la vérification de l'email, le hachage du mot de passe,
     * le remplissage des champs spécifiques, la sauvegarde,
     * et une authentification optionnelle post-inscription.
     *
     * @param registerRequest DTO contenant les informations (communes + spécifiques) du prestataire.
     * @return AuthResponse contenant le token JWT et les informations de base de l'utilisateur.
     * @throws RuntimeException si l'email fourni est déjà utilisé.
     */
    @Transactional // Garantit l'atomicité
    public AuthResponse registerPrestataire(RegisterRequest registerRequest) {
        log.info("Tentative d'inscription d'un prestataire avec l'email: {}", registerRequest.getEmail());

        // 1. Vérification de l'unicité de l'email
        if (utilisateurRepository.existsByEmail(registerRequest.getEmail())) {
            log.warn("Échec de l'inscription: Email {} déjà utilisé.", registerRequest.getEmail());
            throw new RuntimeException("Erreur: Cet email est déjà utilisé!");
        }

        // 2. Création de l'instance de l'entité Prestataire
        Prestataire prestataire = new Prestataire();

        // 3. Remplissage des champs hérités de Utilisateur
        prestataire.setNom(registerRequest.getNom()); // Nom du contact? Adaptez si nécessaire.
        prestataire.setPrenom(registerRequest.getPrenom()); // Prénom du contact? Adaptez si nécessaire.
        prestataire.setEmail(registerRequest.getEmail());

        // 4. Hachage et définition du mot de passe (TRÈS IMPORTANT et identique à Client)
        prestataire.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        // 5. Remplissage des champs spécifiques au Prestataire (depuis le DTO mis à jour)
        prestataire.setNomEntreprise(registerRequest.getNomEntreprise());
        prestataire.setCategorieService(registerRequest.getCategorieService());
        prestataire.setAdresse(registerRequest.getAdresse());
        prestataire.setNumeroTel(registerRequest.getNumeroTel());
        // Note: Le rôle 'PRESTATAIRE' est défini par @DiscriminatorValue ou la stratégie JOINED.

        // 6. Sauvegarde de l'entité Prestataire (utilise le repository spécifique)
        Prestataire savedPrestataire = prestataireRepository.save(prestataire);
        log.info("Prestataire enregistré avec succès: ID={}, Email={}", savedPrestataire.getId(), savedPrestataire.getEmail());

        // 7. Authentification optionnelle post-inscription
        Authentication authentication = authenticateUser(registerRequest.getEmail(), registerRequest.getPassword());

        // 8. Mise à jour du contexte de sécurité
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 9. Récupération des détails pour la génération du token
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 10. Génération du token JWT
        String jwt = jwtUtil.generateToken(userDetails);
        log.info("Token JWT généré pour le nouveau prestataire: {}", savedPrestataire.getEmail());

        // 11. Construction et retour de la réponse
        return new AuthResponse(jwt, savedPrestataire.getEmail(), "PRESTATAIRE", savedPrestataire.getId());
    }


    /**
     * Authentifie un utilisateur existant en utilisant son email et son mot de passe.
     *
     * @param loginRequest DTO contenant l'email et le mot de passe fournis.
     * @return AuthResponse contenant le token JWT et les informations de base de l'utilisateur connecté.
     * @throws BadCredentialsException Si les informations d'identification sont incorrectes.
     * @throws RuntimeException        Pour d'autres erreurs lors de l'authentification.
     */
    public AuthResponse loginUser(LoginRequest loginRequest) {
        log.info("Tentative de connexion pour l'utilisateur: {}", loginRequest.getEmail());

        // 1. Déléguer l'authentification à Spring Security AuthenticationManager
        Authentication authentication = authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());

        // 2. Mettre à jour le contexte de sécurité avec l'utilisateur authentifié
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Obtenir l'objet UserDetails représentant l'utilisateur connecté
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 4. Générer le token JWT pour la session API stateless
        String jwt = jwtUtil.generateToken(userDetails);
        log.info("Connexion réussie et token JWT généré pour: {}", userDetails.getUsername());

        // 5. Extraire l'ID et le rôle pour les inclure dans la réponse
        Long userId;
        String role;

        // Essayer d'utiliser notre UserDetailsImpl pour plus d'efficacité
        if (userDetails instanceof UserDetailsImpl customUserDetails) {
            userId = customUserDetails.getId();
            // Extrait le rôle ("CLIENT", "PRESTATAIRE") depuis les autorités ("ROLE_CLIENT", "ROLE_PRESTATAIRE")
            role = customUserDetails.getAuthorities().stream()
                    .findFirst() // Prend la première (suppose un seul rôle principal)
                    .map(grantedAuthority -> grantedAuthority.getAuthority().replace("ROLE_", "")) // Enlève le préfixe
                    .orElse("UNKNOWN"); // Valeur par défaut si aucune autorité n'est trouvée
            log.info("ID ({}) et rôle ({}) extraits de UserDetailsImpl pour {}", userId, role, userDetails.getUsername());
        } else {
            // Si UserDetails n'est pas notre implémentation, recharger l'entité
            log.warn("UserDetails Principal n'est pas UserDetailsImpl. Rechargement depuis la DB pour ID/rôle de {}", userDetails.getUsername());
            Utilisateur utilisateur = utilisateurRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> {
                        // Ceci ne devrait normalement pas arriver si l'authentification a réussi
                        log.error("ERREUR CRITIQUE: Utilisateur {} authentifié mais introuvable dans la DB!", userDetails.getUsername());
                        return new UsernameNotFoundException("Utilisateur authentifié introuvable: " + userDetails.getUsername());
                    });
            userId = utilisateur.getId();
            role = determineRole(utilisateur); // Utilise la méthode helper avec instanceof
        }

        log.info("Réponse de connexion préparée pour User ID={}, Role={}", userId, role);

        // 6. Retourner la réponse finale avec le token et les infos utilisateur
        return new AuthResponse(jwt, userDetails.getUsername(), role, userId);
    }

    /**
     * Méthode privée pour encapsuler la logique d'authentification via AuthenticationManager.
     *
     * @param email    L'email de l'utilisateur.
     * @param password Le mot de passe en clair fourni.
     * @return L'objet Authentication si l'authentification réussit.
     * @throws BadCredentialsException Si les identifiants sont incorrects.
     * @throws RuntimeException        Pour d'autres erreurs internes.
     */
    private Authentication authenticateUser(String email, String password) {
        try {
            // Tente l'authentification en utilisant le provider configuré (DaoAuthenticationProvider)
            // qui utilise UserDetailsService et PasswordEncoder.
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
        } catch (BadCredentialsException e) {
            log.warn("Échec d'authentification pour {}: {}", email, e.getMessage());
            // Renvoyer telle quelle pour être gérée par le contrôleur (-> 401)
            throw e;
        } catch (Exception e) {
            // Gérer d'autres exceptions possibles pendant l'authentification
            log.error("Erreur système lors de la tentative d'authentification pour {}: {}", email, e.getMessage(), e);
            throw new RuntimeException("Erreur système lors de l'authentification.", e);
        }
    }

    /**
     * Méthode privée helper pour déterminer le rôle ("CLIENT", "PRESTATAIRE", etc.)
     * basé sur le type d'instance réel de l'objet Utilisateur.
     * Principalement utilisée comme fallback lorsque UserDetailsImpl n'est pas disponible.
     *
     * @param utilisateur L'entité utilisateur chargée.
     * @return La chaîne de caractères représentant le rôle.
     */
    private String determineRole(Utilisateur utilisateur) {
        if (utilisateur instanceof Client) {
            return "CLIENT";
        } else if (utilisateur instanceof Prestataire) {
            return "PRESTATAIRE";
        }
        // Ajoutez ici d'autres types si nécessaire (ex: Administrateur)
        // else if (utilisateur instanceof Administrateur) {
        //     return "ADMIN";
        // }
        else {
            log.warn("Impossible de déterminer le rôle pour l'utilisateur ID {}. Type d'instance: {}",
                    utilisateur.getId(), utilisateur.getClass().getName());
            return "UNKNOWN"; // Rôle inconnu ou non géré
        }
    }
}