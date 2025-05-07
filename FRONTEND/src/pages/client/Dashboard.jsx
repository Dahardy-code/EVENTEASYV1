// src/pages/client/Dashboard.jsx
import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { getCurrentClientInfo, logout } from '../../api/authApi';

function ClientDashboard() {
    const [clientInfo, setClientInfo] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchClientData = async () => {
            setLoading(true);
            setError('');
            try {
                console.log("ClientDashboard: Fetching client info...");
                const data = await getCurrentClientInfo();
                console.log("ClientDashboard: Client info received:", data);
                setClientInfo(data);
            } catch (err) {
                console.error("Erreur chargement dashboard client:", err);
                const errorMsg = err.message || "Impossible de charger vos informations.";
                setError(errorMsg);
                // Check if the error indicates unauthorized (API should handle logout)
                 if (err.status === 401 || err.status === 403 || err.message?.includes('401') || err.message?.includes('403')) {
                     // Navigate only if logout didn't already redirect (unlikely but safe)
                     if (localStorage.getItem('authToken')) {
                        logout(); // Ensure cleanup just in case
                     }
                     navigate('/login');
                 }
            } finally {
                setLoading(false);
            }
        };

        fetchClientData();
    }, [navigate]); // Dependency on navigate needed if used in catch block

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    // Loading State
    if (loading) {
        return (
            <div className="flex justify-center items-center min-h-[calc(100vh-8rem)]">
                <p className="text-lg text-gray-600">Chargement du tableau de bord...</p>
                {/* Optional: Add a spinner */}
                 <svg className="animate-spin ml-3 h-5 w-5 text-indigo-600" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle><path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
            </div>
        );
    }

    // Error State
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

    // Success State (Client Info Loaded)
    if (clientInfo) {
        return (
            <div className="container mx-auto px-4 py-8">
                {/* Welcome Section */}
                <div className="bg-gradient-to-r from-blue-50 to-purple-50 border border-blue-200 rounded-lg p-6 mb-8 shadow-sm">
                    <h1 className="text-2xl md:text-3xl font-bold text-gray-800 mb-2">Mon Espace Client</h1>
                    <p className="text-lg text-gray-700">
                        Bienvenue, <span className="font-semibold">{clientInfo.prenom} {clientInfo.nom}</span> !
                    </p>
                    <p className="text-sm text-gray-600 mt-1">
                        Membre depuis le: {clientInfo.dateInscription ? new Date(clientInfo.dateInscription).toLocaleDateString() : 'N/A'}
                    </p>
                </div>

                {/* Dashboard Content Grid */}
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mb-8">
                    {/* Quick Navigation Card */}
                    <div className="bg-white shadow rounded-lg p-6 border border-gray-100">
                        <h2 className="text-xl font-semibold text-gray-700 mb-4 border-b pb-2">Accès Rapide</h2>
                        <ul className="space-y-2">
                            <li>
                                <Link to="/" className="flex items-center text-indigo-600 hover:text-indigo-800 hover:underline transition duration-150 ease-in-out">
                                     {/* Optional Icon */}
                                     <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}><path strokeLinecap="round" strokeLinejoin="round" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" /></svg>
                                    Explorer les offres
                                </Link>
                            </li>
                            <li>
                                <Link to="/client/reservations" className="flex items-center text-indigo-600 hover:text-indigo-800 hover:underline transition duration-150 ease-in-out">
                                     {/* Optional Icon */}
                                    <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}><path strokeLinecap="round" strokeLinejoin="round" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" /></svg>
                                    Mes Réservations
                                </Link>
                            </li>
                            {/* TODO: Add link to Mes Invitations if applicable */}
                            {/* <li><Link to="/client/invitations" className="text-indigo-600 hover:text-indigo-800 hover:underline">Mes Invitations</Link></li> */}
                            <li>
                                <Link to="/client/profile" className="flex items-center text-indigo-600 hover:text-indigo-800 hover:underline transition duration-150 ease-in-out">
                                    {/* Optional Icon */}
                                    <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}><path strokeLinecap="round" strokeLinejoin="round" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" /></svg>
                                    Mon Profil
                                </Link>
                            </li>
                        </ul>
                    </div>

                    {/* Upcoming Reservations Card */}
                    <div className="bg-white shadow rounded-lg p-6 border border-gray-100">
                         <h2 className="text-xl font-semibold text-gray-700 mb-4 border-b pb-2">Prochaines réservations</h2>
                         {/* TODO: Fetch and display upcoming reservations here */}
                         <p className="text-gray-500 italic mt-2">Aucune réservation à venir pour le moment.</p>
                         {/* Example list item: */}
                         {/* <div className="mt-3 border-t pt-3"> */}
                         {/*   <p className="font-medium">Mariage Dupont - Traiteur</p> */}
                         {/*   <p className="text-sm text-gray-600">Date: 25 Dec 2024</p> */}
                         {/*   <Link to="/reservations/123" className="text-sm text-indigo-500 hover:underline">Voir détails</Link> */}
                         {/* </div> */}
                    </div>

                    {/* Maybe Messages/Notifications Card */}
                     <div className="bg-white shadow rounded-lg p-6 border border-gray-100">
                         <h2 className="text-xl font-semibold text-gray-700 mb-4 border-b pb-2">Notifications</h2>
                         {/* TODO: Fetch and display notifications/messages here */}
                         <p className="text-gray-500 italic mt-2">Aucune nouvelle notification.</p>
                     </div>
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
    }

    // Fallback if clientInfo is null after loading and no error (should be rare)
    return (
        <div className="flex justify-center items-center min-h-[calc(100vh-8rem)]">
             <p className="text-gray-600">Une erreur inattendue s'est produite.</p>
        </div>
    );
}

export default ClientDashboard;