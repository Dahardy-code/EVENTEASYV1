import axios from 'axios';

// Configurez l'URL de base de votre API backend
const API_URL = 'http://localhost:8080/api/auth'; // Adaptez le port si nécessaire

// Crée une instance axios pour l'authentification
const authApiClient = axios.create({
    baseURL: API_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

// Crée une instance axios pour les appels API authentifiés
const apiClient = axios.create({
    baseURL: 'http://localhost:8080/api', // URL de base pour les autres endpoints
    headers: {
        'Content-Type': 'application/json',
    },
});

// Intercepteur pour ajouter le token JWT à chaque requête de apiClient
apiClient.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('authToken'); // Ou sessionStorage
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// Fonction pour l'inscription d'un client
export const registerClient = async (userData) => {
    try {
        const response = await authApiClient.post('/register/client', userData);
        if (response.data && response.data.token) {
            // Stocker le token après inscription réussie
            localStorage.setItem('authToken', response.data.token);
            localStorage.setItem('userRole', response.data.role);
            localStorage.setItem('userEmail', response.data.email);
            localStorage.setItem('userId', response.data.userId);
        }
        return response.data; // Contient { token, email, role, userId }
    } catch (error) {
        console.error("Erreur d'inscription:", error.response?.data || error.message);
        throw error.response?.data || new Error("Erreur lors de l'inscription");
    }
};

// Fonction pour la connexion
export const login = async (credentials) => {
    try {
        const response = await authApiClient.post('/login', credentials);
        if (response.data && response.data.token) {
            // Stocker le token et les infos utilisateur
            localStorage.setItem('authToken', response.data.token);
            localStorage.setItem('userRole', response.data.role);
            localStorage.setItem('userEmail', response.data.email);
            localStorage.setItem('userId', response.data.userId);
             // Vous pouvez stocker d'autres infos si nécessaire
        }
        return response.data; // Contient { token, email, role, userId }
    } catch (error) {
        console.error("Erreur de connexion:", error.response?.data || error.message);
        throw error.response?.data || new Error("Erreur lors de la connexion");
    }
};

// Fonction pour la déconnexion
export const logout = () => {
    // Supprimer les informations de l'utilisateur du localStorage
    localStorage.removeItem('authToken');
    localStorage.removeItem('userRole');
    localStorage.removeItem('userEmail');
    localStorage.removeItem('userId');
    // Rediriger vers la page de connexion ou d'accueil
    // (généralement géré dans le composant où logout est appelé)
};

// --- Fonctions pour les appels API authentifiés ---

// Fonction pour récupérer les infos du client connecté
export const getCurrentClientInfo = async () => {
    try {
        const response = await apiClient.get('/clients/me'); // Utilise apiClient avec l'intercepteur
        return response.data;
    } catch (error) {
        console.error("Erreur lors de la récupération des infos client:", error.response?.data || error.message);
         if (error.response?.status === 401 || error.response?.status === 403) {
             // Token invalide ou expiré, déconnecter l'utilisateur
             logout();
             // Rediriger vers login peut être fait ici ou dans le composant appelant
             window.location.href = '/login'; // Redirection simple pour l'exemple
         }
        throw error.response?.data || new Error("Impossible de récupérer les informations utilisateur");
    }
};

// Exportez l'instance apiClient si vous en avez besoin ailleurs
export { apiClient };