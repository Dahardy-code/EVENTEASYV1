// src/api/avisApi.js
import axios from 'axios'; // Pour les appels publics
import { apiClient } from './authApi'; // Pour les appels authentifiés (poster un avis)

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

// Instance Axios spécifique pour les appels publics aux avis (si nécessaire)
const publicAvisApiClient = axios.create({
    baseURL: `${API_BASE_URL}`, // ou API_BASE_URL si /avis est à la racine de /api
    headers: {
        'Content-Type': 'application/json',
    },
});

/**
 * Récupère tous les avis pour un service spécifique (public).
 * @param {number} serviceId - L'ID du service.
 * @returns {Promise<Array<object>>} Liste des avis pour le service.
 */
export const getAvisForService = async (serviceId) => {
    console.log("API: Récupération des avis pour le service ID:", serviceId);
    const response = await publicAvisApiClient.get(`/services/${serviceId}/avis`);
    return response.data;
};

/**
 * Permet à un client connecté de poster un avis pour un service.
 * Le backend doit récupérer l'ID du client depuis le token et vérifier
 * si le client a le droit de laisser un avis (ex: a réservé/utilisé le service).
 * @param {number} serviceId - L'ID du service sur lequel laisser un avis.
 * @param {object} avisData - Ex: { commentaire: string, note: number }
 * @returns {Promise<object>} L'avis créé.
 */
export const postMyAvis = async (serviceId, avisData) => {
    // Le backend s'attend à: { "commentaire": "Super!", "note": 5 }
    // L'ID client sera extrait du token.
    console.log("API: Envoi de mon avis pour service ID:", serviceId, "avec données:", avisData);
    const response = await apiClient.post(`/services/${serviceId}/avis`, avisData);
    return response.data;
};

/**
 * Permet à un prestataire de récupérer les avis sur ses propres services.
 * @returns {Promise<Array<object>>} Liste des avis sur les services du prestataire.
 */
export const getMyServicesAvis = async () => {
    console.log("API: Récupération des avis sur mes services (prestataire)...");
    // Cet endpoint est déjà géré par AvisController/Service au backend pour les prestataires
    const response = await apiClient.get('/prestataires/me/avis');
    return response.data;
};

// Ajouter d'autres fonctions (ex: modifier/supprimer un avis si l'utilisateur est propriétaire ou admin)