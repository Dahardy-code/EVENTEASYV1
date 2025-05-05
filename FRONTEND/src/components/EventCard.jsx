// src/components/EventCard.jsx
import React from 'react';
import { Link } from 'react-router-dom';

// Reusable card component for displaying events, offers, or services
// Accepts an 'item' prop which should be an object with id, name/title, description, price, imageUrl etc.
const EventCard = ({ item }) => {
  // Default values and optional chaining for safety
  const id = item?.id || Math.random(); // Use random as fallback ONLY if no ID, prefer real ID
  const title = item?.titre || item?.name || 'Élément'; // Accept 'titre' or 'name'
  const description = item?.description || 'Découvrez cet élément.';
  const price = item?.prix;
  const imageUrl = item?.imageUrl || '/img/placeholder-event.jpg'; // Provide a real placeholder image path

  // Determine the correct link based on item type (heuristic, adjust as needed)
  // This is just an example, your routing/data structure might be different
  let detailLink = `/offers/${id}`; // Default assumption
  if (item?.type === 'event') { // Example: Check for a type property
      detailLink = `/events/${id}`;
  } else if (item?.type === 'service') {
      detailLink = `/services/${id}`;
  }


  return (
    <div className="bg-white border border-gray-200 rounded-lg shadow-md hover:shadow-xl transition-shadow duration-300 ease-in-out overflow-hidden flex flex-col h-full">
      {/* Image Section */}
      <div className="h-48 w-full bg-gray-100 flex items-center justify-center overflow-hidden">
        <img
            className="w-full h-full object-cover transition-transform duration-300 hover:scale-105"
            src={imageUrl}
            alt={`Image pour ${title}`}
            loading="lazy" // Basic lazy loading
            onError={(e) => { e.target.onerror = null; e.target.src = '/img/placeholder-fallback.jpg'; }} // Fallback image
        />
      </div>

      {/* Content Section */}
      <div className="p-5 flex flex-col flex-grow">
        <h3 className="text-lg font-semibold text-gray-800 mb-2 truncate" title={title}>
            {title}
        </h3>
        <p className="text-gray-600 text-sm mb-4 line-clamp-3 flex-grow"> {/* line-clamp for fixed lines */}
            {description}
        </p>
        {/* Footer of the card */}
        <div className="flex justify-between items-center mt-auto pt-3 border-t border-gray-100">
            <span className="text-md font-bold text-indigo-700">
                {/* Display price or alternative text */}
                {price !== undefined && price !== null ? `${price} €` : 'Sur devis'}
            </span>
            <Link
                to={detailLink}
                className="text-sm font-medium text-indigo-600 hover:text-indigo-800 hover:underline"
            >
                Voir Détails →
             </Link>
        </div>
      </div>
    </div>
  );
};

export default EventCard;