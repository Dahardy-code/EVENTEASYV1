// src/api/invitationApi.js
import { apiClient } from './authApi'; // Authentifié car un client envoie pour son événement

/**
 * Envoie une invitation pour un événement spécifique.
 * Le backend doit vérifier que l'événement appartient au client connecté.
 * @param {number} eventId - L'ID de l'événement.
 * @param {object} invitationData - Ex: { email: string (destinataire), message: string (optionnel) }
 * @returns {Promise<object>} Confirmation ou l'invitation créée.
 */
export const sendEventInvitation = async (eventId, invitationData) => {
    // Le backend s'attend à { "email": "invite@example.com", "message": "Venez à ma fête !" }
    // L'ID client sera extrait du token.
    console.log("API: Envoi d'une invitation pour l'événement ID:", eventId, "avec données:", invitationData);
    const response = await apiClient.post(`/evenements/${eventId}/invitations`, invitationData);
    return response.data;
};

/**
 * Récupère toutes les invitations envoyées par le client connecté pour un événement spécifique.
 * @param {number} eventId - L'ID de l'événement.
 * @returns {Promise<Array<object>>} Liste des invitations.
 */
export const getMyEventInvitations = async (eventId) => {
    console.log("API: Récupération de mes invitations pour l'événement ID:", eventId);
    // TODO: Définir cet endpoint au backend (ex: /clients/me/evenements/{eventId}/invitations)
    // const response = await apiClient.get(`/clients/me/evenements/${eventId}/invitations`);
    // return response.data;
    console.warn(`getMyEventInvitations pour ${eventId}: Endpoint API non implémenté.`);
    return Promise.resolve([]); // Placeholder
};

// Ajouter d'autres fonctions (ex: révoquer une invitation)