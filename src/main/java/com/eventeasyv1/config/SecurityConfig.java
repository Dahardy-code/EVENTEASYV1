package com.eventeasyv1.config; // Assurez-vous que c'est le bon package

import com.eventeasyv1.config.SecurityConfig; // Assurez-vous que le chemin est correct
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // Importer HttpMethod
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // Préféré à EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer; // Pour disable() moderne
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Active la sécurité au niveau des méthodes si nécessaire (@PreAuthorize, etc.)
public class SecurityConfig {

    // Field injection is acceptable here for Filters, but constructor injection is also fine
    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Configuration CORS en premier (bonne pratique)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Désactiver CSRF pour les API stateless (commun avec JWT)
                .csrf(AbstractHttpConfigurer::disable)

                // Configuration des autorisations de requêtes HTTP
                .authorizeHttpRequests(auth -> auth
                        // ---- Public Endpoints ----
                        // Permettre l'accès public explicite aux endpoints d'authentification/inscription
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/register/client").permitAll()
                        .requestMatchers("/api/auth/register/prestataire").permitAll() // <-- CORRECTION : Explicitement autorisé

                        // Permettre la consultation publique des offres/événements (si nécessaire)
                        .requestMatchers(HttpMethod.GET, "/api/offers").permitAll() // Exemple
                        .requestMatchers(HttpMethod.GET, "/api/events").permitAll()  // Exemple

                        // Permettre Swagger si utilisé
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // ---- Protected Endpoints ----
                        // Note: hasRole ajoute automatiquement "ROLE_" devant.
                        // Si vos rôles sont stockés sans "ROLE_", utilisez hasAuthority("CLIENT").
                        .requestMatchers("/api/clients/me").hasRole("CLIENT")
                        .requestMatchers("/api/prestataires/me/**").hasRole("PRESTATAIRE")
                        .requestMatchers("/api/statistiques/admin").hasRole("ADMIN")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // --- Ajoutez d'autres règles spécifiques ici ---
                        // Ex: .requestMatchers(HttpMethod.POST, "/api/reservations").hasAnyRole("CLIENT", "ADMIN")

                        // --- Fallback ---
                        .anyRequest().authenticated() // Toute autre requête nécessite une authentification
                )

                // Configuration de la gestion de session (Stateless pour JWT)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Ajout du filtre JWT avant le filtre d'authentification standard
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // --- Bean pour la Configuration CORS ---
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // ! IMPORTANT: Vérifiez le port de votre serveur de développement Vite !
        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173", // Port Vite commun
                "http://127.0.0.1:5173", // Autre adresse locale
                "http://localhost:3000", // Port vu dans le screenshot de l'erreur
                "http://127.0.0.1:3000"
        ));
        // Pour la production, remplacez par l'URL de votre frontend déployé:
        // configuration.setAllowedOrigins(List.of("https://eventeasy.votre-domaine.com"));

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD")); // Méthodes autorisées
        configuration.setAllowedHeaders(List.of("*")); // Autorise tous les en-têtes en DEV. Soyez plus spécifique en PROD.
        // En-têtes spécifiques recommandés pour PROD :
        // configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type", "X-Requested-With", "Accept", "Origin"));
        configuration.setAllowCredentials(true); // Crucial pour les tokens/cookies
        configuration.setMaxAge(3600L); // Cache preflight pendant 1 heure

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Appliquer cette configuration CORS à toutes les routes sous /api/
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
}