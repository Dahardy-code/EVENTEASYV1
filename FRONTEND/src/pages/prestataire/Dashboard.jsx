// src/pages/prestataire/Dashboard.jsx
import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
// *** IMPORTANT: Replace these with actual API calls using apiClient when ready ***
import { getReservations, getEvents, getStatistics } from '../../api/serviceApi'; // Currently uses mock data
import { logout } from '../../api/authApi'; // Import logout

const PrestataireDashboard = () => {
  const [reservations, setReservations] = useState([]);
  const [events, setEvents] = useState([]);
  const [statistics, setStatistics] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate(); // Hook for navigation

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      setError('');
      try {
        console.log("PrestataireDashboard: Fetching data...");
        // *** TODO: Replace these with real, authenticated API calls using apiClient ***
        // Example: const reservationsData = await apiClient.get('/api/prestataire/me/reservations');
        // Example: const eventsData = await apiClient.get('/api/prestataire/me/events');
        // Example: const statisticsData = await apiClient.get('/api/prestataire/me/stats');

        // Using mock API calls for now
        const reservationsData = await getReservations();
        const eventsData = await getEvents();
        const statisticsData = await getStatistics();

        console.log("PrestataireDashboard: Data received (mock):", { reservationsData, eventsData, statisticsData });

        // Ensure state is always set with appropriate defaults (arrays/objects)
        setReservations(reservationsData || []);
        setEvents(eventsData || []);
        setStatistics(statisticsData || {});

      } catch (err) {
        console.error("Erreur lors du chargement des données prestataire:", err);
        setError('Impossible de charger les données du tableau de bord.');
         // Handle potential auth errors if using real apiClient calls
         if (err.response?.status === 401 || err.response?.status === 403) {
            logout(); // Clear auth state
            navigate('/login'); // Redirect
         }
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  // Add navigate to dependencies ONLY if it's used within the try/catch for redirection
  }, [navigate]);

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  // --- Loading State ---
  if (loading) {
    return (
      <div className="flex justify-center items-center min-h-[calc(100vh-8rem)]">
        <p className="text-lg text-gray-600">Chargement du tableau de bord Prestataire...</p>
        <svg className="animate-spin ml-3 h-5 w-5 text-indigo-600" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle><path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
      </div>
    );
  }

  // --- Error State ---
  if (error) {
    return (
      <div className="flex flex-col items-center justify-center min-h-[calc(100vh-8rem)] text-center px-4">
        <p className="text-red-600 text-lg mb-4">{error}</p>
        <Link to="/login" className="text-indigo-600 hover:text-indigo-800 font-medium underline">
          Retourner à la connexion
        </Link>
      </div>
    );
  }

  // --- Success State ---
  return (
    <div className="container mx-auto px-4 py-8">
      {/* Welcome Header */}
      <div className="bg-gradient-to-r from-green-50 to-teal-50 border border-green-200 rounded-lg p-6 mb-8 shadow-sm">
        <h1 className="text-2xl md:text-3xl font-bold text-gray-800 mb-2">Espace Prestataire</h1>
        {/* TODO: Fetch and display Prestataire name/company */}
        <p className="text-lg text-gray-700">Gérez vos services, disponibilités et réservations.</p>
      </div>

      {/* Stats Overview Grid */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 mb-8">
        {/* Reservations Card */}
        <div className="bg-white shadow rounded-lg p-5 border border-gray-100 text-center hover:shadow-md transition-shadow">
          <h2 className="text-xl font-semibold text-gray-700 mb-2">Réservations</h2>
          <p className="text-3xl font-bold text-indigo-600">{reservations.length || 0}</p>
          {/* TODO: Update link to actual reservations management page */}
          <Link to="/prestataire/reservations" className="text-sm text-indigo-500 hover:underline mt-3 inline-block">
            Voir les réservations →
          </Link>
        </div>

        {/* Events Card */}
        <div className="bg-white shadow rounded-lg p-5 border border-gray-100 text-center hover:shadow-md transition-shadow">
          <h2 className="text-xl font-semibold text-gray-700 mb-2">Événements Associés</h2>
          {/* This might be derived from reservations or a separate endpoint */}
          <p className="text-3xl font-bold text-purple-600">{events.length || 0}</p>
          {/* TODO: Update link if needed */}
          {/* <Link to="/prestataire/evenements" className="text-sm text-purple-500 hover:underline mt-3 inline-block">
             Voir les événements →
           </Link> */}
            <p className="text-sm text-gray-400 mt-3">(Fonctionnalité à venir)</p>
        </div>

        {/* Statistics Card */}
        <div className="bg-white shadow rounded-lg p-5 border border-gray-100 text-center hover:shadow-md transition-shadow">
          <h2 className="text-xl font-semibold text-gray-700 mb-2">Statistiques</h2>
          <p className="text-sm text-gray-600">Réservations totales : {statistics?.totalReservations || 0}</p>
          <p className="text-sm text-gray-600">Événements terminés : {statistics?.completedEvents || 0}</p>
          {/* TODO: Update link to actual stats page */}
          {/* <Link to="/prestataire/statistiques" className="text-sm text-teal-500 hover:underline mt-3 inline-block">
             Voir les détails →
           </Link> */}
            <p className="text-sm text-gray-400 mt-3">(Stats détaillées à venir)</p>
        </div>
      </div>

      {/* Action Links Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
         {/* Manage Services Card */}
         <div className="bg-white shadow rounded-lg p-6 border border-gray-100 hover:shadow-lg transition-shadow">
             <h3 className="font-semibold text-lg mb-2 text-gray-800">Gérer Mes Services</h3>
             <p className="text-sm text-gray-600 mb-4">Ajoutez, modifiez ou supprimez les services que vous proposez pour les événements.</p>
             {/* TODO: Update link */}
             <Link to="/prestataire/services" className="text-indigo-600 hover:text-indigo-800 font-medium">
                 Gérer mes services →
             </Link>
         </div>
         {/* Manage Availability Card */}
          <div className="bg-white shadow rounded-lg p-6 border border-gray-100 hover:shadow-lg transition-shadow">
             <h3 className="font-semibold text-lg mb-2 text-gray-800">Gérer Mes Disponibilités</h3>
             <p className="text-sm text-gray-600 mb-4">Mettez à jour votre calendrier pour indiquer quand vous êtes disponible.</p>
              {/* TODO: Update link */}
             <Link to="/prestataire/disponibilites" className="text-indigo-600 hover:text-indigo-800 font-medium">
                 Mettre à jour le calendrier →
             </Link>
         </div>
         {/* Add more action cards: Profile, Payments, etc. */}
      </div>

      {/* Logout Button Section */}
      <div className="mt-10 text-center">
        <button
          onClick={handleLogout}
          className="bg-red-500 hover:bg-red-600 text-white font-medium py-2 px-5 rounded-md focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500 transition duration-150 ease-in-out"
        >
          Se déconnecter
        </button>
      </div>
    </div>
  );
};

export default PrestataireDashboard;