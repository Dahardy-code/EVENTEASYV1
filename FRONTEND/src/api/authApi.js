import axios from 'axios';

// Use environment variable for the API base URL
// Ensure you have a .env file in your project root with:
// VITE_API_BASE_URL=http://localhost:8080/api
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

if (!API_BASE_URL) {
    console.error("Fatal Error: VITE_API_BASE_URL is not defined in your environment variables (.env file).");
    // You might want to throw an error or show a message to the user
}

// --- Axios Instance Configuration ---

// Instance for Authentication endpoints (register, login)
const authApiClient = axios.create({
    baseURL: `${API_BASE_URL}/auth`, // Specific path for auth routes
    headers: {
        'Content-Type': 'application/json',
    },
});

// Instance for Authenticated API calls (requires JWT)
const apiClient = axios.create({
    baseURL: API_BASE_URL, // Base URL for other API endpoints
    headers: {
        'Content-Type': 'application/json',
    },
});

// --- Helper Function to Store Auth Data ---

const storeAuthData = (data) => {
    if (data && data.token) {
        localStorage.setItem('authToken', data.token);
        localStorage.setItem('userRole', data.role);
        localStorage.setItem('userEmail', data.email);
        localStorage.setItem('userId', data.userId);
        // Store any other relevant user info if needed
        console.log("Auth data stored:", { role: data.role, email: data.email, userId: data.userId });
    } else {
        console.warn("Attempted to store auth data, but token was missing.", data);
    }
};

// --- Axios Interceptor for JWT ---

// Add JWT token to every request made with 'apiClient'
apiClient.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('authToken');
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        // console.log('Sending request with token:', !!token); // For debugging
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// --- Authentication API Functions ---

/**
 * Registers a new client user.
 * @param {object} userData - { nom, prenom, email, password, role: 'CLIENT' }
 * @returns {Promise<object>} Backend response data including token and user info.
 */
export const registerClient = async (userData) => {
    try {
        console.log("Calling /register/client with data:", userData);
        const response = await authApiClient.post('/register/client', userData);
        storeAuthData(response.data); // Store token and user info on success
        return response.data;
    } catch (error) {
        const errorData = error.response?.data || { message: error.message };
        console.error("Erreur d'inscription Client:", errorData);
        throw errorData; // Re-throw backend error or a generic one
    }
};

/**
 * Registers a new prestataire user.
 * @param {object} userData - { nom, prenom, email, password, role: 'PRESTATAIRE', ...other fields? }
 * @returns {Promise<object>} Backend response data including token and user info.
 */
export const registerPrestataire = async (userData) => {
    try {
        // *** IMPORTANT: Verify '/register/prestataire' is your correct backend endpoint ***
        console.log("Calling /register/prestataire with data:", userData);
        const response = await authApiClient.post('/register/prestataire', userData);
        storeAuthData(response.data); // Store token and user info on success
        return response.data;
    } catch (error) {
        const errorData = error.response?.data || { message: error.message };
        console.error("Erreur d'inscription Prestataire:", errorData);
        throw errorData; // Re-throw backend error or a generic one
    }
};


/**
 * Logs in a user.
 * @param {object} credentials - { email, password }
 * @returns {Promise<object>} Backend response data including token and user info.
 */
export const login = async (credentials) => {
    try {
        const response = await authApiClient.post('/login', credentials);
        storeAuthData(response.data); // Store token and user info on success
        return response.data;
    } catch (error) {
        const errorData = error.response?.data || { message: error.message };
        console.error("Erreur de connexion:", errorData);
        // Provide a more user-friendly default message
        throw errorData.message ? errorData : new Error("Échec de la connexion. Vérifiez vos identifiants.");
    }
};

/**
 * Logs out the current user by clearing localStorage.
 */
export const logout = () => {
    console.log("Logging out - clearing auth data.");
    localStorage.removeItem('authToken');
    localStorage.removeItem('userRole');
    localStorage.removeItem('userEmail');
    localStorage.removeItem('userId');
    // Navigation should be handled by the component calling logout
};

// --- Authenticated API Call Functions ---

/**
 * Fetches information for the currently logged-in client.
 * Requires a valid JWT token.
 * @returns {Promise<object>} Client information.
 */
export const getCurrentClientInfo = async () => {
    try {
        const response = await apiClient.get('/clients/me'); // Uses interceptor
        return response.data;
    } catch (error) {
        const errorData = error.response?.data || { message: error.message };
        console.error("Erreur lors de la récupération des infos client:", errorData);
        if (error.response?.status === 401 || error.response?.status === 403) {
            console.warn("Unauthorized access or expired token detected. Logging out.");
            logout(); // Clear stale auth data
            // Removed window.location.href redirect.
            // The calling component should handle navigation based on the error.
            // Example in component: catch(err) { if (err.status === 401) navigate('/login'); }
        }
        // Re-throw the error so the calling component can handle it (e.g., show message, redirect)
        throw errorData.message ? errorData : new Error("Impossible de récupérer les informations utilisateur.");
    }
};

// --- Export Axios instance if needed elsewhere ---
export { apiClient }; // Export the instance configured with the interceptor