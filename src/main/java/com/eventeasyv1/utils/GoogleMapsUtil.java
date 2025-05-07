package com.eventeasyv1.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
// Imports nécessaires si vous utilisez une bibliothèque client Google Maps
// ex: com.google.maps.GeoApiContext, com.google.maps.GeocodingApi, etc.

@Component
public class GoogleMapsUtil {

    @Value("${google.maps.api.key}") // Injecter la clé API depuis les propriétés
    private String apiKey;

    // TODO: Implémenter les méthodes nécessaires en utilisant l'API Google Maps
    // Exemples :
    // - Géocodage (adresse -> coordonnées lat/lng)
    // - Reverse Géocodage (lat/lng -> adresse)
    // - Calcul de distance/itinéraire
    // - Recherche de lieux à proximité (Places API)

    public String getStaticMapUrl(double lat, double lng) {
        // Exemple très simple générant une URL d'image de carte statique
        if (apiKey == null || apiKey.isEmpty()) {
            return null; // Clé API non configurée
        }
        return String.format("https://maps.googleapis.com/maps/api/staticmap?center=%f,%f&zoom=14&size=400x300&markers=color:red|%f,%f&key=%s",
                lat, lng, lat, lng, apiKey);
    }

    // public LatLng geocodeAddress(String address) { ... }
    // public List<PlaceResult> findNearbyPlaces(double lat, double lng, int radius, String type) { ... }
}