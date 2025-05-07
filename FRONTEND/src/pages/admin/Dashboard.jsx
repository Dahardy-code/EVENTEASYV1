// src/pages/admin/Dashboard.jsx
import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
// *** IMPORTANT: Ensure getAdminStats uses the authenticated apiClient from authApi.js ***
import { getAdminStats } from '../../api/statistiqueApi';
import { logout } from '../../api/authApi'; // Import logout

const AdminDashboard = () => {
  const [stats, setStats] = useState(null); // Initialize as null or {}
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate(); // Hook for navigation

  useEffect(() => {
    const fetchStats = async () => {
      setLoading(true);
      setError(''); // Reset error before fetching
      try {
        // This API call MUST use the authenticated apiClient internally
        console.log("AdminDashboard: Fetching admin stats...");
        const data = await getAdminStats();
        console.log("AdminDashboard: Stats received:", data);

        if (data) {
          setStats(data);
        } else {
          // Handle cases where the API might return 200 OK but empty data
          setError('Aucune donnée de statistique disponible.');
          setStats({}); // Set to empty object to avoid render errors on null
        }
      } catch (err) {
        console.error('Erreur lors du chargement des statistiques admin:', err);
        setError(err.message || 'Erreur lors du chargement des statistiques.');
        // Handle potential auth errors (401/403)
         if (err.status === 401 || err.status === 403 || err.message?.includes('401') || err.message?.includes('403')) {
             console.warn("AdminDashboard: Unauthorized access detected, logging out.");
             logout(); // Clear auth state
             navigate('/login'); // Redirect
         }
         setStats({}); // Set to empty object on error too
      } finally {
        setLoading(false);
      }
    };
    fetchStats();
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
        <p className="text-lg text-gray-600">Chargement du tableau de bord Admin...</p>
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
       <div className="bg-gradient-to-r from-red-50 to-orange-50 border border-red-200 rounded-lg p-6 mb-8 shadow-sm">
         <h1 className="text-2xl md:text-3xl font-bold text-gray-800 mb-2">Panneau d'Administration</h1>
         <p className="text-lg text-gray-700">Gestion et supervision de la plateforme Eventeasy.</p>
       </div>

      {/* Stats Overview - Render only if stats is not null */}
      {stats && (
         <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mb-8">
            {/* Users Card */}
            <div className="bg-white shadow rounded-lg p-5 border border-gray-100 hover:shadow-md transition-shadow">
                <h2 className="text-xl font-semibold text-gray-700 mb-2">Utilisateurs</h2>
                {/* TODO: Ensure backend provides these specific stats */}
                <p className="text-3xl font-bold text-blue-600">{stats.totalUsers || 'N/A'}</p>
                <p className="text-sm text-gray-500">Clients: {stats.totalClients || 'N/A'}</p>
                <p className="text-sm text-gray-500">Prestataires: {stats.totalPrestataires || 'N/A'}</p>
                <Link to="/admin/manage-users" className="text-sm text-blue-500 hover:underline mt-3 inline-block">
                    Gérer les utilisateurs →
                </Link>
            </div>
            {/* Reservations Card */}
             <div className="bg-white shadow rounded-lg p-5 border border-gray-100 hover:shadow-md transition-shadow">
                <h2 className="text-xl font-semibold text-gray-700 mb-2">Réservations Totales</h2>
                <p className="text-3xl font-bold text-green-600">{stats.totalReservations || 0}</p>
                 <Link to="/admin/manage-reservations" className="text-sm text-green-500 hover:underline mt-3 inline-block">
                    Voir les réservations →
                 </Link>
             </div>
             {/* Popular Services Card */}
              <div className="bg-white shadow rounded-lg p-5 border border-gray-100 hover:shadow-md transition-shadow">
                <h2 className="text-xl font-semibold text-gray-700 mb-2">Services Populaires</h2>
                {/* TODO: Display popular services better (maybe a list?) */}
                <p className="text-sm text-gray-600 truncate">{stats.popularServices || 'Données indisponibles'}</p>
                 <Link to="/admin/manage-offers" className="text-sm text-purple-500 hover:underline mt-3 inline-block">
                    Gérer les offres →
                 </Link>
             </div>
             {/* Add more stat cards if needed */}
         </div>
      )}

        {/* Admin Actions/Links */}
        <div className="bg-white shadow rounded-lg p-6 border border-gray-100">
            <h2 className="text-xl font-semibold text-gray-700 mb-4 border-b pb-2">Outils d'Administration</h2>
            <ul className="space-y-3 list-disc list-inside">
                 <li><Link to="/admin/manage-users" className="text-indigo-600 hover:text-indigo-800 font-medium">Gestion des Utilisateurs</Link></li>
                 <li><Link to="/admin/manage-offers" className="text-indigo-600 hover:text-indigo-800 font-medium">Gestion des Offres/Services</Link></li>
                 <li><Link to="/admin/manage-reservations" className="text-indigo-600 hover:text-indigo-800 font-medium">Suivi des Réservations</Link></li>
                 <li><Link to="/admin/reports" className="text-indigo-600 hover:text-indigo-800 font-medium">Rapports et Statistiques</Link></li>
                 <li><Link to="/admin/settings" className="text-indigo-600 hover:text-indigo-800 font-medium">Paramètres de la Plateforme</Link></li>
                 {/* TODO: Add more relevant admin links */}
             </ul>
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

export default AdminDashboard;