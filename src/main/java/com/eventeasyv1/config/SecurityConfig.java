package com.eventeasyv1.config;

import com.eventeasyv1.config.JwtFilter; // Assurez-vous que JwtFilter est un @Component
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
@EnableMethodSecurity // Active la sécurité au niveau des méthodes (pour @PreAuthorize)
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter; // S'assurer que JwtFilter est un bean @Component

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
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Appliquer la configuration CORS définie ci-dessous
                .csrf(AbstractHttpConfigurer::disable) // Désactiver CSRF pour les API REST stateless
                .authorizeHttpRequests(auth -> auth
                        // ---- Endpoints Publics ----
                        .requestMatchers("/api/auth/**").permitAll() // Login, Register Client, Register Prestataire
                        .requestMatchers(HttpMethod.GET, "/api/services").permitAll()       // Liste publique des services
                        .requestMatchers(HttpMethod.GET, "/api/services/**").permitAll()  // Détail public d'un service ET LES AVIS (GET /api/services/{id}/avis)
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // Pour la documentation API Swagger/OpenAPI
                        .requestMatchers(HttpMethod.GET, "/api/promos/active").permitAll() // Endpoint public pour les promos actives
                        .requestMatchers(HttpMethod.GET, "/api/promos/validate/{codePromo}").permitAll() // Endpoint public pour valider un code
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // ---- Fallback ----
                        // La sécurité plus granulaire pour les autres endpoints (comme POST /api/services/{id}/avis)
                        // est gérée par @PreAuthorize dans les controllers grâce à @EnableMethodSecurity
                        .anyRequest().authenticated() // Toutes les autres requêtes nécessitent une authentification
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Pas de session côté serveur (pour JWT)
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // Ajouter notre filtre JWT

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // IMPORTANT : VÉRIFIEZ LE PORT EXACT sur lequel tourne votre frontend Vite
        // (Ex: 3000, 3001, 5173 - regardez les logs de `npm run dev`)
        configuration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:3000/",
                "http://127.0.0.1:3000",
                "http://localhost:3001",   // Si Vite utilise ce port
                "http://127.0.0.1:3001",  // Si Vite utilise ce port
                "http://localhost:5173"    // Autre port courant pour Vite/React
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        // configuration.setMaxAge(3600L); // Optionnel

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Appliquer à tous les chemins
        return source;
    }
}