// src/pages/prestataire/Dashboard.jsx
import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
// *** IMPORTANT: Remplacer par les vrais appels API avec apiClient quand prêts ***
import { getReservations, getEvents, getStatistics } from '../../api/serviceApi'; // API MOCKÉE
import { logout } from '../../api/authApi'; // Fonction de déconnexion

// Exemple de composant carte simple pour les actions
const ActionCard = ({ title, description, linkTo, linkText = "Accéder" }) => (
     <div className="bg-white shadow rounded-lg p-6 border border-gray-100 hover:shadow-lg transition-shadow duration-200 ease-in-out flex flex-col">
         <h3 className="font-semibold text-lg mb-2 text-gray-800">{title}</h3>
         <p className="text-sm text-gray-600 mb-4 flex-grow">{description}</p>
         <Link to={linkTo} className="mt-auto text-indigo-600 hover:text-indigo-800 font-medium self-start">
             {linkText} →
         </Link>
     </div>
);

const PrestataireDashboard = () => {
  // State pour les données (initialisées comme vides/zéro)
  const [reservations, setReservations] = useState([]);
  const [events, setEvents] = useState([]); // Potentiellement dérivé des réservations
  const [statistics, setStatistics] = useState({});
  const [prestataireInfo, setPrestataireInfo] = useState(null); // Pour afficher le nom

  // State pour le chargement et les erreurs
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      setError('');
      try {
        console.log("PrestataireDashboard: Fetching initial data...");

        // --- TODO: Remplacer par de vrais appels API authentifiés ---
        // Exemple: const prestataireData = await apiClient.get('/api/prestataires/me');
        // Exemple: const reservationsData = await apiClient.get('/api/prestataires/me/reservations');
        // Exemple: const statsData = await apiClient.get('/api/prestataires/me/stats');

        // Utilisation des mocks pour l'instant
        const [reservationsData, eventsData, statisticsData] = await Promise.all([
             getReservations(),
             getEvents(),
             getStatistics()
             // Ajoutez ici l'appel pour récupérer les infos du prestataire connecté (ex: apiClient.get('/api/prestataires/me'))
        ]);
        // const prestataireData = await apiClient.get('/api/prestataires/me'); // Supposons que cela existe

        console.log("PrestataireDashboard: Data received (mock):", { reservationsData, eventsData, statisticsData });
        // console.log("PrestataireDashboard: Prestataire info:", prestataireData?.data); // Log des infos prestataire

        // Mettre à jour le state
        // setPrestataireInfo(prestataireData?.data || null);
        setReservations(reservationsData || []);
        setEvents(eventsData || []); // Adaptez si les événements viennent d'ailleurs
        setStatistics(statisticsData || {});

      } catch (err) {
        console.error("Erreur lors du chargement des données prestataire:", err);
        setError('Impossible de charger les données du tableau de bord.');
        // Gérer les erreurs d'authentification si de vrais appels apiClient sont utilisés
         if (err.response?.status === 401 || err.response?.status === 403) {
            logout();
            navigate('/login');
         }
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [navigate]); // Dépendance pour la redirection en cas d'erreur auth

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  // Rendu pendant le chargement
  if (loading) {
    return (
      <div className="flex justify-center items-center min-h-[calc(100vh-8rem)]">
        <p className="text-lg text-gray-600">Chargement de votre espace Prestataire...</p>
        <svg className="animate-spin ml-3 h-5 w-5 text-indigo-600" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle><path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
      </div>
    );
  }

  // Rendu en cas d'erreur
  if (error) {
    return (
      <div className="flex flex-col items-center justify-center min-h-[calc(100vh-8rem)] text-center px-4">
        <p className="text-red-600 text-lg mb-4">{error}</p>
        <Link to="/login" className="text-indigo-600 hover:text-indigo-800 font-medium underline">
          Se reconnecter
        </Link>
      </div>
    );
  }

  // Rendu normal du tableau de bord
  return (
    <div className="container mx-auto px-4 py-8">
      {/* En-tête de Bienvenue */}
      <div className="bg-gradient-to-r from-green-50 to-teal-50 border border-green-200 rounded-lg p-6 mb-10 shadow-sm">
        <h1 className="text-2xl md:text-3xl font-bold text-gray-800 mb-2">
            Espace Prestataire
            {prestataireInfo?.nomEntreprise && ` - ${prestataireInfo.nomEntreprise}`}
        </h1>
        <p className="text-lg text-gray-700">Gérez efficacement vos services, disponibilités et réservations.</p>
      </div>

      {/* Section Statistiques Clés */}
      <h2 className="text-xl font-semibold text-gray-700 mb-4">Aperçu Rapide</h2>
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 mb-10">
        {/* Carte Réservations */}
        <div className="bg-white shadow rounded-lg p-5 border border-gray-100 text-center">
          <h3 className="text-lg font-medium text-gray-500 mb-1">Réservations</h3>
          {/* TODO: Filtrer pour afficher les réservations 'actives' ou 'à venir' */}
          <p className="text-4xl font-bold text-indigo-600">{reservations.length || 0}</p>
          <Link to="/prestataire/reservations" className="text-sm text-indigo-500 hover:underline mt-3 inline-block">
            Voir toutes les réservations →
          </Link>
        </div>
        {/* Carte Chiffre d'Affaires (Exemple) */}
        <div className="bg-white shadow rounded-lg p-5 border border-gray-100 text-center">
          <h3 className="text-lg font-medium text-gray-500 mb-1">Revenu (Exemple)</h3>
           {/* TODO: Calculer ou récupérer le revenu */}
          <p className="text-4xl font-bold text-green-600">{statistics?.totalRevenue || 'N/A'} €</p>
          <Link to="/prestataire/paiements" className="text-sm text-green-500 hover:underline mt-3 inline-block">
            Voir les paiements →
          </Link>
        </div>
        {/* Carte Avis (Exemple) */}
         <div className="bg-white shadow rounded-lg p-5 border border-gray-100 text-center">
           <h3 className="text-lg font-medium text-gray-500 mb-1">Nouveaux Avis</h3>
           {/* TODO: Récupérer le nombre de nouveaux avis */}
           <p className="text-4xl font-bold text-yellow-500">{statistics?.newReviewsCount || 0}</p>
           <Link to="/prestataire/avis" className="text-sm text-yellow-600 hover:underline mt-3 inline-block">
             Voir les avis →
           </Link>
         </div>
      </div>

      {/* Section Actions Principales */}
      <h2 className="text-xl font-semibold text-gray-700 mb-4">Gérer Mon Activité</h2>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
        {/* Utilisation du composant ActionCard */}
        <ActionCard
          title="Mes Services / Offres"
          description="Ajoutez, modifiez ou supprimez les services que vous proposez."
          linkTo="/prestataire/services" // TODO: Créez cette route et page
          linkText="Gérer les services"
        />
        <ActionCard
          title="Mon Calendrier"
          description="Définissez vos disponibilités pour recevoir des réservations."
          linkTo="/prestataire/disponibilites" // TODO: Créez cette route et page
          linkText="Gérer les disponibilités"
        />
         <ActionCard
          title="Mon Profil Prestataire"
          description="Mettez à jour vos informations de contact, description, photos."
          linkTo="/prestataire/profil" // TODO: Créez cette route et page
          linkText="Modifier mon profil"
        />
         <ActionCard
          title="Mes Réservations"
          description="Consultez et gérez les réservations confirmées ou en attente."
          linkTo="/prestataire/reservations" // TODO: Créez cette route et page
          linkText="Voir les réservations"
        />
         {/* Ajoutez d'autres cartes pour Avis, Paiements, etc. */}
      </div>

      {/* Bouton Déconnexion */}
      <div className="mt-12 text-center">
        <button
          onClick={handleLogout}
          className="bg-gray-500 hover:bg-gray-600 text-white font-medium py-2 px-5 rounded-md focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-500 transition duration-150 ease-in-out"
        >
          Se déconnecter
        </button>
      </div>
    </div>
  );
};

export default PrestataireDashboard;