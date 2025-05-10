// src/api/reservationApi.js
import { apiClient } from './authApi'; // Utilise l'instance configurée avec le token

/**
 * Crée une nouvelle réservation pour le client connecté.
 * Le backend doit récupérer l'ID du client depuis le token.
 * @param {object} reservationData - Ex: { serviceId: number, dateReservation: string (ISO) }
 * @returns {Promise<object>} La réservation créée.
 */
export const createMyReservation = async (reservationData) => {
    // Le backend s'attend à un objet comme :
    // { "serviceId": 123, "dateReservation": "2024-12-25T14:30:00" }
    // L'ID client sera extrait du token par le backend.
    console.log("API: Création de réservation avec données:", reservationData);
    const response = await apiClient.post('/reservations', reservationData);
    return response.data;
};

/**
 * Récupère toutes les réservations du client actuellement connecté.
 * @returns {Promise<Array<object>>} Liste des réservations du client.
 */
export const getMyReservations = async () => {
    console.log("API: Récupération de mes réservations...");
    // Cet endpoint est déjà géré par ClientController/Service au backend
    const response = await apiClient.get('/clients/me/reservations');
    return response.data;
};

/**
 * Récupère une réservation spécifique par son ID pour le client connecté.
 * Le backend doit vérifier que la réservation appartient bien au client.
 * @param {number} reservationId - L'ID de la réservation.
 * @returns {Promise<object>} Les détails de la réservation.
 */
export const getMyReservationById = async (reservationId) => {
    console.log("API: Récupération de ma réservation ID:", reservationId);
    // TODO: Assurez-vous que cet endpoint existe et vérifie la propriété
    // Exemple d'endpoint: /reservations/{id} (avec vérification de propriété côté backend)
    const response = await apiClient.get(`/reservations/${reservationId}`);
    return response.data;
};

/**
 * Annule une réservation pour le client connecté.
 * @param {number} reservationId - L'ID de la réservation à annuler.
 * @returns {Promise<object>} Confirmation ou la réservation mise à jour.
 */
export const cancelMyReservation = async (reservationId) => {
    console.log("API: Annulation de ma réservation ID:", reservationId);
    // TODO: Définir cet endpoint au backend (ex: PATCH /reservations/{id}/cancel)
    // Le backend doit vérifier que la réservation appartient au client et peut être annulée.
    // const response = await apiClient.patch(`/reservations/${reservationId}/cancel`);
    // return response.data;
    console.warn(`cancelMyReservation pour ${reservationId}: Endpoint API non implémenté.`);
    return Promise.resolve({ message: "Réservation annulée (simulation)" }); // Placeholder
};

// Vous pouvez ajouter d'autres fonctions liées aux réservations client ici