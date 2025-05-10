// src/pages/HomePage.jsx
import React, { useState, useEffect, useCallback } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios'; // Garder pour créer une instance publique dédiée

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

// Instance Axios pour les appels publics de cette page (si différente de apiClient de authApi)
const publicApiClient = axios.create({
  baseURL: API_BASE_URL, // Utilise la variable d'environnement
  headers: {
    'Content-Type': 'application/json',
  },
});

// Renommer en ServiceCard pour correspondre aux entités (ou EventCard si plus approprié)
const ServiceCard = ({ service }) => (
  <div className="bg-white border border-gray-200 rounded-lg shadow-md hover:shadow-xl transition-shadow duration-300 ease-in-out overflow-hidden flex flex-col h-full">
    {/* Image Placeholder - TODO: Ajouter une vraie image si disponible */}
    <div className="h-48 w-full bg-gray-200 flex items-center justify-center">
      <svg className="w-16 h-16 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z"></path></svg>
    </div>
    <div className="p-5 flex flex-col flex-grow">
      <h3 className="text-xl font-semibold text-indigo-700 mb-2 truncate" title={service?.titre || 'Service Sans Titre'}>
        {service?.titre || 'Service Sans Titre'}
      </h3>
      <p className="text-gray-600 text-sm mb-1">Catégorie: {service?.categorie || 'N/A'}</p>
      {service?.prestataireNomEntreprise && (
        <p className="text-xs text-gray-500 mb-3">Par: {service.prestataireNomEntreprise}</p>
      )}
      <p className="text-gray-600 text-sm mb-4 line-clamp-3 flex-grow">
        {service?.description || 'Aucune description disponible.'}
      </p>
      <div className="flex justify-between items-center mt-auto pt-3 border-t border-gray-100">
        <span className="text-lg font-bold text-gray-800">
          {service?.prix ? `${service.prix} €` : 'Sur devis'}
        </span>
        {/* TODO: Définir la route pour les détails du service */}
        <Link
          to={`/services/${service?.id}`}
          className="text-sm font-medium text-indigo-600 hover:text-indigo-800 hover:underline"
        >
          Voir Détails →
        </Link>
      </div>
    </div>
  </div>
);

const HomePage = () => {
  const [services, setServices] = useState([]); // Renommé en services
  const [searchTerm, setSearchTerm] = useState('');
  const [filterCategorie, setFilterCategorie] = useState(''); // Renommé pour clarté
  // const [location, setLocation] = useState(''); // Garder si vous implémentez le filtre par localisation
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [currentSlide, setCurrentSlide] = useState(0);
  const [currentPage, setCurrentPage] = useState(0); // Pour la pagination
  const [totalPages, setTotalPages] = useState(0);    // Pour la pagination

  const slides = [
    { image: '/images/slider1.jpg', text: "Trouvez des prestataires pour votre événement" },
    { image: '/images/slider2.jpg', text: "Offrez à vos invités une expérience inoubliable" },
    { image: '/images/slider3.jpg', text: "Découvrez une large gamme de services" },
    { image: '/images/slider4.jpg', text: "Facilitez la recherche de prestataires" }
  ];

  const fetchServices = useCallback(async (page = 0) => {
    setLoading(true);
    setError('');
    try {
      const params = new URLSearchParams();
      if (searchTerm) params.append('query', searchTerm); // 'query' est souvent utilisé par les backends
      if (filterCategorie) params.append('categorie', filterCategorie);
      // if (location) params.append('location', location);
      params.append('page', page);
      params.append('size', 8); // Nombre d'éléments par page

      // *** UTILISER L'ENDPOINT PUBLIC POUR LES SERVICES ***
      const response = await publicApiClient.get(`/services?${params.toString()}`);
      console.log("HomePage - Services data received:", response.data);

      // Supposer que le backend renvoie un objet Pageable de Spring Data JPA
      setServices(response.data?.content || []);
      setTotalPages(response.data?.totalPages || 0);
      setCurrentPage(response.data?.number || 0);

    } catch (err) {
      console.error("HomePage - Error fetching services:", err);
      setError("Impossible de charger les services/offres.");
      setServices([]);
      setTotalPages(0);
    } finally {
      setLoading(false);
    }
  }, [searchTerm, filterCategorie /*, location*/]); // Ajouter les dépendances du filtre

  useEffect(() => {
    fetchServices(0); // Charger la première page au montage
  }, [fetchServices]); // fetchServices est mémorisé par useCallback

  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentSlide(prevSlide => (prevSlide + 1) % slides.length);
    }, 5000);
    return () => clearInterval(interval);
  }, [slides.length]); // Dépend de slides.length au cas où il changerait

  const handleSearchSubmit = (e) => {
    e.preventDefault();
    fetchServices(0); // Relancer la recherche à partir de la page 0
  };

  const handlePageChange = (newPage) => {
    fetchServices(newPage);
  };

  return (
    <div className="relative min-h-screen overflow-hidden">
      {/* Image de fond floutée */}
      <div
        className="absolute inset-0 bg-cover bg-center"
        style={{ backgroundImage: 'url(/images/loginpic.jpg)', filter: 'blur(6px)', zIndex: -1 }}
      ></div>

      <div className="container mx-auto px-4 py-8 relative z-10">
        {/* Slider */}
        <div className="relative mb-10 h-72 md:h-96 rounded-lg overflow-hidden shadow-lg">
          {slides.map((slide, index) => (
            <div
              key={index}
              className={`absolute inset-0 transition-opacity duration-1000 ease-in-out ${index === currentSlide ? 'opacity-100' : 'opacity-0'}`}
              style={{ backgroundImage: `url(${slide.image})`, backgroundSize: 'cover', backgroundPosition: 'center' }}
            >
              <div className="absolute inset-0 bg-black opacity-40"></div>
              <div className="absolute bottom-6 md:bottom-10 left-0 right-0 text-center text-white p-4">
                <p className="text-xl sm:text-2xl md:text-3xl font-bold">{slide.text}</p>
              </div>
            </div>
          ))}
          <button onClick={() => setCurrentSlide((currentSlide - 1 + slides.length) % slides.length)} className="absolute left-2 md:left-4 top-1/2 transform -translate-y-1/2 text-white text-2xl md:text-3xl bg-black bg-opacity-30 p-2 rounded-full hover:bg-opacity-50">❮</button>
          <button onClick={() => setCurrentSlide((currentSlide + 1) % slides.length)} className="absolute right-2 md:right-4 top-1/2 transform -translate-y-1/2 text-white text-2xl md:text-3xl bg-black bg-opacity-30 p-2 rounded-full hover:bg-opacity-50">❯</button>
        </div>

        {/* Titre et Intro */}
        <div className="text-center mb-12">
          <h1 className="text-3xl md:text-4xl font-extrabold text-gray-100 mb-3 tracking-tight">Trouvez le prestataire idéal pour votre événement</h1>
          <p className="text-md md:text-lg text-gray-200 max-w-2xl mx-auto">Explorez une large gamme de services événementiels et réservez en toute simplicité.</p>
        </div>

        {/* Section de recherche */}
        <form onSubmit={handleSearchSubmit} className="mb-10 p-6 bg-white bg-opacity-90 rounded-lg shadow-lg border border-gray-200">
          <div className="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-3 gap-4 items-end"> {/* Ajusté pour 3 colonnes */}
            <div className="lg:col-span-1"> {/* Ajusté */}
              <label htmlFor="search" className="block text-sm font-medium text-gray-700 mb-1">Rechercher</label>
              <input type="text" id="search" value={searchTerm} onChange={(e) => setSearchTerm(e.target.value)} placeholder="Ex: DJ, Traiteur..." className="w-full px-4 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500"/>
            </div>
            <div>
              <label htmlFor="filterCategorie" className="block text-sm font-medium text-gray-700 mb-1">Type de service</label>
              <select id="filterCategorie" value={filterCategorie} onChange={(e) => setFilterCategorie(e.target.value)} className="w-full px-4 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500 bg-white">
                <option value="">Tous</option>
                <option value="TRAITEUR">Traiteur</option>
                <option value="DJ">DJ</option>
                <option value="PHOTOGRAPHE">Photographe</option>
                <option value="LIEU">Lieu</option>
                {/* TODO: Charger dynamiquement les catégories depuis le backend */}
              </select>
            </div>
            <div> {/* Ajusté pour être sur la même ligne */}
              <button type="submit" className="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-2 px-4 rounded-md transition duration-300 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500" disabled={loading}>
                {loading ? 'Recherche...' : 'Rechercher'}
              </button>
            </div>
          </div>
        </form>

        {/* Zone d'affichage des services */}
        <div>
          <h2 className="text-2xl font-semibold text-white mb-6">Services Disponibles</h2>
          {loading && <div className="text-center py-10 text-white"><p>Chargement...</p>{/* Spinner */}</div>}
          {error && <p className="text-center text-red-300 bg-red-800 bg-opacity-50 p-4 rounded border border-red-400">{error}</p>}
          {!loading && !error && (
            <>
              <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
                {Array.isArray(services) && services.length > 0 ? (
                  services.map((service) => <ServiceCard key={service?.id || Math.random()} service={service} />)
                ) : (
                  <p className="col-span-full text-center text-gray-300 py-10">Aucun service ne correspond à votre recherche ou aucun service n'est disponible.</p>
                )}
              </div>
              {/* Pagination Controls */}
              {totalPages > 1 && (
                <div className="flex justify-center items-center mt-8 space-x-2">
                  <button onClick={() => handlePageChange(currentPage - 1)} disabled={currentPage === 0 || loading} className="px-4 py-2 bg-indigo-500 text-white rounded disabled:opacity-50">Précédent</button>
                  <span className="text-white">Page {currentPage + 1} sur {totalPages}</span>
                  <button onClick={() => handlePageChange(currentPage + 1)} disabled={currentPage >= totalPages - 1 || loading} className="px-4 py-2 bg-indigo-500 text-white rounded disabled:opacity-50">Suivant</button>
                </div>
              )}
            </>
          )}
        </div>
      </div>
    </div>
  );
};

export default HomePage;