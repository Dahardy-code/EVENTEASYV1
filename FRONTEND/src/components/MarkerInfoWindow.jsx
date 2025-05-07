// src/components/MarkerInfoWindow.jsx
import React from 'react';

// Placeholder for an InfoWindow content, typically used with a map library like @react-google-maps/api
// Expects a 'place' object prop with details like name, address etc.
const MarkerInfoWindow = ({ place }) => {
  // Don't render anything if no place data is provided
  if (!place) {
    return null;
  }

  const { name = 'Lieu sélectionné', address = 'Adresse non disponible', website, phone } = place;

  // Basic search URL for Google Maps
  const mapsUrl = `https://www.google.com/maps/search/?api=1&query=${encodeURIComponent(name)},${encodeURIComponent(address)}`;

  return (
    <div className="p-2 max-w-xs bg-white rounded shadow-lg text-sm font-sans">
      <h4 className="font-bold text-gray-900 mb-1 text-base">{name}</h4>
      <p className="text-gray-700 mb-1">{address}</p>
      {phone && <p className="text-gray-600 text-xs mb-1">Tel: {phone}</p>}
      {website && (
        <a
          href={website}
          target="_blank"
          rel="noopener noreferrer"
          className="text-blue-600 hover:underline text-xs block mb-1"
          title={`Visiter le site web de ${name}`}
        >
          Site Web
        </a>
      )}
      <a
        href={mapsUrl}
        target="_blank"
        rel="noopener noreferrer"
        className="text-blue-600 hover:underline text-xs block mt-1"
        title={`Voir ${name} sur Google Maps`}
      >
        Voir sur Google Maps →
      </a>
       {/* TODO: Add more details like rating, opening hours, photos etc. */}
    </div>
  );
};

export default MarkerInfoWindow;