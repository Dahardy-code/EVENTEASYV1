// src/components/PromoBanner.jsx
import React, { useState } from 'react';

// Placeholder for a dismissible promotional banner
const PromoBanner = ({
  message = "🎉 Offre Spéciale du Moment !",
  ctaText = "Découvrir",
  ctaLink = "#", // Link for the call-to-action button
  backgroundColor = "bg-gradient-to-r from-pink-500 via-purple-500 to-indigo-500", // Example gradient
  textColor = "text-white",
  buttonClasses = "bg-white text-purple-700 hover:bg-gray-100",
}) => {
  const [isVisible, setIsVisible] = useState(true); // State to control visibility

  if (!isVisible) {
    return null; // Don't render if dismissed
  }

  return (
    <div className={`relative ${backgroundColor} ${textColor} p-4 text-center my-4 rounded-lg shadow-md overflow-hidden`}>
      <div className="flex flex-col sm:flex-row items-center justify-center space-y-2 sm:space-y-0 sm:space-x-4">
        <span className="font-medium">{message}</span>
        {ctaLink && ctaText && (
          <a
            href={ctaLink}
            target="_blank" // Consider target based on link type
            rel="noopener noreferrer"
            className={`inline-block font-bold py-1 px-4 rounded-full mt-2 sm:mt-0 text-sm transition duration-150 ease-in-out ${buttonClasses}`}
          >
            {ctaText}
          </a>
        )}
      </div>
      {/* Dismiss Button */}
      <button
        onClick={() => setIsVisible(false)}
        className="absolute top-1 right-1 p-1 text-sm opacity-70 hover:opacity-100 focus:outline-none focus:ring-1 focus:ring-white rounded-full"
        aria-label="Fermer la bannière"
      >
        {/* Simple 'X' icon */}
        <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18L18 6M6 6l12 12"></path></svg>
      </button>
    </div>
  );
};

export default PromoBanner;