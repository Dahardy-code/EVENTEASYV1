// src/api/statistiqueApi.js

import axios from 'axios';

export const getAdminStats = async () => {
  try {
    const response = await axios.get('/api/statistiques/admin'); // L'endpoint de ton backend
    return response.data;
  } catch (error) {
    console.error("Erreur lors de la récupération des statistiques:", error);
    return null;
  }
};
