// src/components/AvisList.jsx
import React from 'react';

// Placeholder for displaying reviews
// Expects an array of 'avis' objects as a prop eventually
const AvisList = ({ avis = [] }) => {
  return (
    <div className="p-4 border rounded shadow-sm my-4 bg-white">
      <h3 className="text-lg font-semibold mb-3 border-b pb-2">Avis Clients</h3>
      {avis.length === 0 ? (
        <p className="text-gray-500 italic text-sm">Aucun avis n'a été laissé pour le moment.</p>
      ) : (
        <ul className="space-y-3">
          {/* Placeholder: Map over 'avis' array when data is passed */}
          {/* Example structure for one review: */}
          <li className="text-sm border-b pb-2">
            <p className="font-medium text-gray-800">Utilisateur Anonyme (Placeholder)</p>
            {/* Add rating component here */}
            <p className="text-gray-600 mt-1">"Ceci est un avis placeholder..."</p>
          </li>
           <li className="text-sm">
            <p className="font-medium text-gray-800">Autre Client (Placeholder)</p>
            <p className="text-gray-600 mt-1">"Excellent service!"</p>
          </li>
          {/* End Example */}
        </ul>
      )}
    </div>
  );
};

export default AvisList;