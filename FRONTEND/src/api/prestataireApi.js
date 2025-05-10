// src/api/prestataireApi.js
import { apiClient } from './authApi'; // Utilise l'instance apiClient configurée avec le token

// --- Gestion des Services du Prestataire ---
export const getMyServices = async () => {
    const response = await apiClient.get('/prestataires/me/services');
    return response.data;
};

// *** VÉRIFIEZ CETTE FONCTION ET SON EXPORT ***
export const addMyService = async (serviceData) => {
    const response = await apiClient.post('/prestataires/me/services', serviceData);
    return response.data;
};
// *******************************************

export const updateMyService = async (serviceId, serviceData) => {
    const response = await apiClient.put(`/prestataires/me/services/${serviceId}`, serviceData);
    return response.data;
};

export const deleteMyService = async (serviceId) => {
    const response = await apiClient.delete(`/prestataires/me/services/${serviceId}`);
    return response.data; // Ou juste status de succès
};

// --- Gestion des Disponibilités du Prestataire ---
export const getMyDisponibilites = async () => {
    const response = await apiClient.get('/prestataires/me/disponibilites');
    return response.data;
};

export const addMyDisponibilite = async (disponibiliteData) => {
    const response = await apiClient.post('/prestataires/me/disponibilites', disponibiliteData);
    return response.data;
};

export const deleteMyDisponibilite = async (disponibiliteId) => {
    const response = await apiClient.delete(`/prestataires/me/disponibilites/${disponibiliteId}`);
    return response.data;
};

// --- Gestion du Profil Prestataire ---
export const getMyPrestataireProfile = async () => {
    const response = await apiClient.get('/prestataires/me');
    return response.data;
};

export const updateMyPrestataireProfile = async (profileData) => {
    console.warn("updateMyPrestataireProfile: Endpoint API non implémenté.");
    return Promise.resolve({ message: "Profil mis à jour (simulation)" });
};

// --- Gestion des Réservations pour les services du Prestataire ---
export const getMyServiceReservations = async () => {
    console.warn("getMyServiceReservations: Endpoint API non implémenté, retour de données mockées.");
    return Promise.resolve([
        { id: 101, clientNom: "Client Test A", serviceTitre: "Service Alpha", dateReservation: new Date().toISOString(), statut: "CONFIRMEE"},
        { id: 102, clientNom: "Client Test B", serviceTitre: "Service Beta", dateReservation: new Date().toISOString(), statut: "EN_ATTENTE"},
    ]);
};

export const updateReservationStatus = async (reservationId, newStatus) => {
    console.warn(`updateReservationStatus pour ${reservationId}: Endpoint API non implémenté.`);
    return Promise.resolve({ message: "Statut mis à jour (simulation)"});
};

// --- Statistiques Prestataire ---
export const getMyPrestataireStatistics = async () => {
    console.warn("getMyPrestataireStatistics: Endpoint API non implémenté, retour de données mockées.");
    return Promise.resolve({
        totalServices: 5,
        totalReservations: 20,
        upcomingReservations: 3,
        averageRating: 4.7
    });
};