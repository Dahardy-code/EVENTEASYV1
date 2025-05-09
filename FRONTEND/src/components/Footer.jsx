// src/components/Footer.jsx
import React from 'react';

const Footer = () => {
  return (
    <footer className="bg-gray-800 text-white py-8">
      <div className="max-w-7xl mx-auto px-6 sm:px-8">
        <div className="flex justify-between items-center mb-6">
          <div className="text-lg font-semibold">
            <p>&copy; 2025 Eventeasy. Tous droits réservés.</p>
          </div>
          <div className="space-x-6">
            <a href="/about" className="hover:text-gray-400">À propos</a>
            <a href="/contact" className="hover:text-gray-400">Contact</a>
            <a href="/terms" className="hover:text-gray-400">Conditions d'utilisation</a>
          </div>
        </div>
        
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-8">
          <div>
            <h3 className="font-semibold text-lg">Contactez-nous</h3>
            <p className="mt-4 text-sm">Numéro de téléphone : +33 1 23 45 67 89</p>
            <p className="mt-2 text-sm">Email : contact@eventeasy.com</p>
            <p className="mt-2 text-sm">Adresse : 12 Rue de l'Event, 75001 Paris, France</p>
          </div>
          
          <div>
            <h3 className="font-semibold text-lg">Suivez-nous</h3>
            <div className="mt-4 space-x-4">
              <a href="https://www.facebook.com" className="text-gray-400 hover:text-white">Facebook</a>
              <a href="https://www.twitter.com" className="text-gray-400 hover:text-white">Twitter</a>
              <a href="https://www.instagram.com" className="text-gray-400 hover:text-white">Instagram</a>
            </div>
          </div>
          
          <div>
            <h3 className="font-semibold text-lg">À propos de Eventeasy</h3>
            <p className="mt-4 text-sm">Eventeasy est une plateforme facilitant la mise en relation entre les prestataires et les clients pour l'organisation d'événements.</p>
          </div>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
