import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';

const OfferCard = ({ offer }) => (
  <div className="bg-white border border-gray-200 rounded-lg shadow hover:shadow-lg transition-shadow duration-300 ease-in-out overflow-hidden">
    <div className="p-5">
      <h3 className="text-xl font-semibold text-indigo-700 mb-2 truncate">{offer?.titre || 'Offre Sans Titre'}</h3>
      <p className="text-gray-600 text-sm mb-3 line-clamp-3">{offer?.description || 'Aucune description disponible.'}</p>
      <div className="flex justify-between items-center mt-4">
        <span className="text-lg font-bold text-gray-800">{offer?.prix ? `${offer.prix} €` : 'Prix non spécifié'}</span>
        <Link
          to={`/offers/${offer?.id}`} 
          className="text-sm font-medium text-indigo-600 hover:text-indigo-800 hover:underline"
        >
          Voir Détails →
        </Link>
      </div>
    </div>
  </div>
);

const HomePage = () => {
  const [offers, setOffers] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [serviceType, setServiceType] = useState('');
  const [location, setLocation] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [currentSlide, setCurrentSlide] = useState(0);

  const slides = [
    { image: '/images/slider1.jpg', text: "Trouvez des prestataires pour votre événement" },
    { image: '/images/slider2.jpg', text: "Offrez à vos invités une expérience inoubliable" },
    { image: '/images/slider3.jpg', text: "Découvrez une large gamme de services" },
    { image: '/images/slider4.jpg', text: "Facilitez la recherche de prestataires" }
  ];

  const fetchOffers = async () => {
    setLoading(true);
    setError('');
    try {
      const params = new URLSearchParams();
      if (searchTerm) params.append('q', searchTerm);
      if (serviceType) params.append('type', serviceType);
      if (location) params.append('location', location);

      const response = await axios.get(`/api/offers?${params.toString()}`);
      setOffers(Array.isArray(response.data) ? response.data : response.data?.content || []);
    } catch (err) {
      setError("Impossible de charger les offres.");
      setOffers([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchOffers();
  }, []);

  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentSlide(prevSlide => (prevSlide + 1) % slides.length);
    }, 5000);
    return () => clearInterval(interval);
  }, []);

  const handleSearchSubmit = (e) => {
    e.preventDefault();
    fetchOffers();
  };

  return (
    <div className="relative min-h-screen overflow-hidden">
      {/* Image de fond floutée */}
      <div
        className="absolute inset-0 bg-cover bg-center"
        style={{
          backgroundImage: 'url(/images/loginpic.jpg)',
          filter: 'blur(6px)',
          zIndex: -1
        }}
      ></div>

      {/* Contenu principal avec z-index supérieur */}
      <div className="container mx-auto px-4 py-8 relative z-10">
        {/* Slider Section */}
        <div className="relative mb-10">
          <div
            className="w-full h-96 rounded-lg overflow-hidden relative"
            style={{
              backgroundImage: `url(${slides[currentSlide].image})`,
              backgroundSize: 'cover',
              backgroundPosition: 'center'
            }}
          >
            <div className="absolute inset-0 bg-black opacity-40"></div>
            <div className="absolute bottom-10 left-0 right-0 text-center text-white">
              <p className="text-2xl sm:text-3xl font-bold">{slides[currentSlide].text}</p>
            </div>
          </div>

          <button
            onClick={() => setCurrentSlide((currentSlide - 1 + slides.length) % slides.length)}
            className="absolute left-4 top-1/2 transform -translate-y-1/2 text-white text-3xl"
          >
            &#10094;
          </button>
          <button
            onClick={() => setCurrentSlide((currentSlide + 1) % slides.length)}
            className="absolute right-4 top-1/2 transform -translate-y-1/2 text-white text-3xl"
          >
            &#10095;
          </button>
        </div>

        <div className="text-center mb-12">
          <h1 className="text-4xl font-extrabold text-gray-100 mb-3 tracking-tight">
            Trouvez le prestataire idéal pour votre événement
          </h1>
          <p className="text-lg text-gray-200 max-w-2xl mx-auto">
            Explorez une large gamme de services événementiels, comparez les offres et réservez en toute simplicité.
          </p>
        </div>

        {/* Section de recherche */}
        <form onSubmit={handleSearchSubmit} className="mb-10 p-6 bg-white bg-opacity-90 rounded-lg shadow-sm border border-gray-200">
          <div className="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-4 gap-4 items-end">
            <div className="lg:col-span-2">
              <label htmlFor="search" className="block text-sm font-medium text-gray-700 mb-1">
                Rechercher (par mot-clé, nom...)
              </label>
              <input
                type="text"
                id="search"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                placeholder="Ex: DJ, Traiteur, Salle..."
                className="w-full px-4 py-2 border border-gray-300 rounded-md shadow-sm"
              />
            </div>

            <div>
              <label htmlFor="serviceType" className="block text-sm font-medium text-gray-700 mb-1">
                Type de service
              </label>
              <select
                id="serviceType"
                value={serviceType}
                onChange={(e) => setServiceType(e.target.value)}
                className="w-full px-4 py-2 border border-gray-300 rounded-md shadow-sm"
              >
                <option value="">Tous</option>
                <option value="TRAITEUR">Traiteur</option>
                <option value="DJ">DJ</option>
                <option value="PHOTOGRAPHE">Photographe</option>
                <option value="LIEU">Lieu</option>
              </select>
            </div>

            <div className="md:col-start-3 lg:col-start-4">
              <button
                type="submit"
                className="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-2 px-4 rounded-md transition duration-300"
                disabled={loading}
              >
                {loading ? 'Recherche...' : 'Rechercher'}
              </button>
            </div>
          </div>
        </form>

        {/* Zone d'affichage des offres */}
        <div>
          <h2 className="text-2xl font-semibold text-white mb-6">Offres disponibles</h2>
          {loading && (
            <div className="text-center py-10">
              <svg className="animate-spin h-8 w-8 text-indigo-500 mx-auto" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              <p className="mt-2 text-white">Chargement des offres...</p>
            </div>
          )}
          {error && <p className="text-center text-red-600 bg-red-100 p-4 rounded border border-red-400">{error}</p>}
          {!loading && !error && (
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
              {Array.isArray(offers) && offers.length > 0 ? (
                offers.map((offer) => <OfferCard key={offer?.id || Math.random()} offer={offer} />)
              ) : (
                <p className="col-span-full text-center text-white py-10">
                  Aucune offre ne correspond à votre recherche ou aucune offre n'est disponible pour le moment.
                </p>
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default HomePage;
