// src/pages/HomePage.jsx
import React, { useEffect, useState, useCallback } from 'react';
import { Link } from 'react-router-dom';
// Use apiClient if your endpoint requires authentication, otherwise use axios
// import { apiClient } from '../api/authApi';
import axios from 'axios'; // Assuming public browsing initially

// Placeholder for a reusable card component
const OfferCard = ({ offer }) => (
    <div className="bg-white border border-gray-200 rounded-lg shadow hover:shadow-lg transition-shadow duration-300 ease-in-out overflow-hidden">
        {/* TODO: Add an image */}
        {/* <img className="w-full h-48 object-cover" src={offer.imageUrl || '/placeholder-image.svg'} alt={offer.titre || 'Offer image'} /> */}
        <div className="p-5">
            <h3 className="text-xl font-semibold text-indigo-700 mb-2 truncate">{offer?.titre || 'Offre Sans Titre'}</h3>
            <p className="text-gray-600 text-sm mb-3 line-clamp-3">{offer?.description || 'Aucune description disponible.'}</p>
            <div className="flex justify-between items-center mt-4">
                <span className="text-lg font-bold text-gray-800">{offer?.prix ? `${offer.prix} €` : 'Prix non spécifié'}</span>
                {/* // TODO: Update link when offer detail route exists */}
                <Link
                    to={`/offers/${offer?.id}`} // Example route
                    className="text-sm font-medium text-indigo-600 hover:text-indigo-800 hover:underline"
                >
                    Voir Détails →
                 </Link>
            </div>
        </div>
    </div>
);


const HomePage = () => {
    // State for fetched offers/events/services
    const [offers, setOffers] = useState([]);
    // State for search and filters
    const [searchTerm, setSearchTerm] = useState('');
    const [serviceType, setServiceType] = useState(''); // Example filter
    const [location, setLocation] = useState('');     // Example filter
    // State for loading and errors
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    // Function to fetch offers from the backend
    const fetchOffers = useCallback(async () => {
        setLoading(true);
        setError('');
        try {
            // *** TODO: Replace '/api/offers' with your ACTUAL backend endpoint for public offers/services/events ***
            // Construct query parameters for search/filtering
            const params = new URLSearchParams();
            if (searchTerm) params.append('q', searchTerm); // Example 'q' for query
            if (serviceType) params.append('type', serviceType);
            if (location) params.append('location', location);

            // Use appropriate client (axios for public, apiClient for protected)
            const response = await axios.get(`/api/offers?${params.toString()}`);

            console.log('API Response Data for /api/offers:', response.data);

             // Ensure response.data is an array before setting state
             // Adjust 'response.data?.content' if your API uses pagination wrappers
            setOffers(Array.isArray(response.data) ? response.data : response.data?.content || []);

        } catch (err) {
            console.error("Error fetching offers:", err);
            setError("Impossible de charger les offres.");
            setOffers([]); // Ensure offers is an array even on error
        } finally {
            setLoading(false);
        }
    }, [searchTerm, serviceType, location]); // Re-fetch when search/filters change

    // Initial fetch on component mount
    useEffect(() => {
        fetchOffers();
    }, [fetchOffers]); // fetchOffers is stable due to useCallback

    // Handle form submission for search/filters
    const handleSearchSubmit = (e) => {
        e.preventDefault();
        fetchOffers(); // Trigger fetch with current state values
    };

    return (
        <div className="container mx-auto px-4 py-8">
            <div className="text-center mb-12">
                <h1 className="text-4xl font-extrabold text-gray-800 mb-3 tracking-tight">
                    Trouvez le prestataire idéal pour votre événement
                </h1>
                <p className="text-lg text-gray-600 max-w-2xl mx-auto">
                    Explorez une large gamme de services événementiels, comparez les offres et réservez en toute simplicité.
                </p>
            </div>

            {/* Search and Filter Section */}
            <form onSubmit={handleSearchSubmit} className="mb-10 p-6 bg-gray-50 rounded-lg shadow-sm border border-gray-200">
                <div className="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-4 gap-4 items-end">
                    {/* Search Input */}
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
                            className="w-full px-4 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500"
                        />
                    </div>

                    {/* Example Filter: Service Type (Replace with actual types) */}
                    <div>
                        <label htmlFor="serviceType" className="block text-sm font-medium text-gray-700 mb-1">
                            Type de service
                        </label>
                        <select
                            id="serviceType"
                            value={serviceType}
                            onChange={(e) => setServiceType(e.target.value)}
                            className="w-full px-4 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-indigo-500 focus:border-indigo-500 bg-white"
                        >
                            <option value="">Tous</option>
                            <option value="TRAITEUR">Traiteur</option>
                            <option value="DJ">DJ</option>
                            <option value="PHOTOGRAPHE">Photographe</option>
                            <option value="LIEU">Lieu</option>
                            {/* TODO: Add more relevant service types */}
                        </select>
                    </div>

                    {/* Submit Button */}
                    <div className="md:col-start-3 lg:col-start-4">
                        <button
                            type="submit"
                            className="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-2 px-4 rounded-md transition duration-300 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                            disabled={loading}
                        >
                            {loading ? 'Recherche...' : 'Rechercher'}
                        </button>
                    </div>
                </div>
                 {/* TODO: Add more filters here (location, date, price range etc.) */}
            </form>

            {/* Offers Display Area */}
            <div>
                 <h2 className="text-2xl font-semibold text-gray-700 mb-6">Offres disponibles</h2>
                {loading && (
                    <div className="text-center py-10">
                        {/* Optional Spinner */}
                        <svg className="animate-spin h-8 w-8 text-indigo-600 mx-auto" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                            <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                            <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                        </svg>
                        <p className="mt-2 text-gray-500">Chargement des offres...</p>
                    </div>
                 )}
                {error && <p className="text-center text-red-600 bg-red-100 p-4 rounded border border-red-400">{error}</p>}
                {!loading && !error && (
                    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
                        {Array.isArray(offers) && offers.length > 0 ? (
                            offers.map(offer => (
                                // Use the placeholder card component
                                <OfferCard key={offer?.id || Math.random()} offer={offer} />
                            ))
                        ) : (
                            <p className="col-span-full text-center text-gray-500 py-10">
                                Aucune offre ne correspond à votre recherche ou aucune offre n'est disponible pour le moment.
                            </p>
                        )}
                    </div>
                )}
            </div>
        </div>
    );
};

export default HomePage;