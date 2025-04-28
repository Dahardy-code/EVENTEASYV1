package com.eventeasyv1.service;

import com.eventeasyv1.dao.ClientRepository;
import com.eventeasyv1.dao.UtilisateurRepository;
import com.eventeasyv1.dto.AuthResponse;
import com.eventeasyv1.dto.LoginRequest;
import com.eventeasyv1.dto.RegisterRequest;
import com.eventeasyv1.entities.Client;
import com.eventeasyv1.entities.Utilisateur;
import com.eventeasyv1.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private ClientRepository clientRepository; // Injectez ClientRepository

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Transactional
    public AuthResponse registerClient(RegisterRequest registerRequest) {
        if (utilisateurRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Erreur: Cet email est déjà utilisé!");
        }

        Client client = new Client();
        client.setNom(registerRequest.getNom());
        client.setPrenom(registerRequest.getPrenom());
        client.setEmail(registerRequest.getEmail());
        client.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        // client.setRole("CLIENT"); // Normalement géré par @DiscriminatorValue, pas besoin de le setter explicitement
        client.setDateInscription(new Date()); // Date d'inscription

        Client savedClient = clientRepository.save(client);

        // Authentifier l'utilisateur après l'inscription pour générer le token
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(registerRequest.getEmail(), registerRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtUtil.generateToken(userDetails);

        return new AuthResponse(jwt, savedClient.getEmail(), "CLIENT", savedClient.getId());
    }

    public AuthResponse loginUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtUtil.generateToken(userDetails);

        // Récupérer l'utilisateur pour obtenir son rôle et ID
        Utilisateur utilisateur = utilisateurRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé après authentification"));


        return new AuthResponse(jwt, utilisateur.getEmail(), utilisateur.getRole(), utilisateur.getId());
    }
}