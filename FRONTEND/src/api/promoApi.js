// src/api/promoApi.js
import axios from 'axios';
import { apiClient } from './authApi'; // Pour les actions admin authentifiées

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

const publicPromoApiClient = axios.create({
    baseURL: `${API_BASE_URL}/promos`,
    headers: {
        'Content-Type': 'application/json',
    },
});

/**
 * Récupère toutes les promotions actuellement actives (public).
 * @returns {Promise<Array<object>>} Liste des promotions actives.
 */
export const getActivePromos = async () => {
    console.log("API: Récupération des promotions actives...");
    const response = await publicPromoApiClient.get('/actives');
    return response.data;
};

/**
 * Récupère une promotion par son code (public).
 * @param {string} codePromo - Le code de la promotion.
 * @returns {Promise<object>} Les détails de la promotion.
 */
export const getPromoByCode = async (codePromo) => {
    console.log("API: Récupération de la promotion par code:", codePromo);
    // TODO: Définir cet endpoint au backend si nécessaire (ex: /promos/code/{codePromo})
    // const response = await publicPromoApiClient.get(`/code/${codePromo}`);
    // return response.data;
    console.warn(`getPromoByCode pour ${codePromo}: Endpoint API non implémenté.`);
    return Promise.reject({ message: "Fonctionnalité non disponible."}); // Placeholder
};


// --- Fonctions Admin (nécessitent authentification Admin) ---

/**
 * Crée une nouvelle promotion (Admin).
 * @param {object} promoData - Données de la promotion.
 * @returns {Promise<object>} La promotion créée.
 */
export const createPromo = async (promoData) => {
    console.log("API Admin: Création d'une promotion:", promoData);
    const response = await apiClient.post('/admin/promos', promoData); // Assumer un endpoint admin
    return response.data;
};

/**
 * Récupère toutes les promotions (Admin).
 * @returns {Promise<Array<object>>} Liste de toutes les promotions.
 */
export const getAllPromosAdmin = async () => {
    console.log("API Admin: Récupération de toutes les promotions...");
    const response = await apiClient.get('/admin/promos'); // Assumer un endpoint admin
    return response.data;
};

// Ajouter updatePromo, deletePromo pour l'admin...